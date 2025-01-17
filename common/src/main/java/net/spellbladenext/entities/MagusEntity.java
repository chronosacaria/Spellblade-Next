package net.spellbladenext.entities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerWorld;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.SoundHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.items.spellbladeitems.SpellbladeItem;
import net.spellbladenext.items.spellbladeitems.SpellbladeItems;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.builder.RawAnimation;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class MagusEntity extends PathAwareEntity implements InventoryOwner, IAnimatable {
    public Player nemesis;
    public boolean isthinking = false;
    public boolean isScout = false;
    private boolean hasntthrownitems = true;
    private boolean firstattack = false;
    private boolean secondattack = false;
    private boolean isstopped = false;
    public static EntityDataAccessor<Integer> modifier;
    boolean isCaster = false;
    private Player tradingplayer;
    float damagetakensincelastthink = 0;
    int invisibletime = 0;
    public Vec3 speed = Vec3.ZERO;
    private final SimpleContainer inventory = new SimpleContainer(8);
    private static final Set<Item> WANTED_ITEMS = ImmutableSet.of( Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS);
    public boolean returninghome = false;
    public boolean isleader = false;
    public int homecount = 0;
    public int homecount2 = 0;
    public Player hero = null;
    public boolean casting = false;
    public boolean canGiveGifts = false;
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final RawAnimation ATTACK = new RawAnimation("animation.hexblade.new", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    public static final RawAnimation ATTACK2 = new RawAnimation("animation.hexblade.new2", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    public static final RawAnimation WALK = new RawAnimation("animation.hexblade.walk", ILoopType.EDefaultLoopTypes.LOOP);
    public static final RawAnimation WALK2 = new RawAnimation("animation.hexblade.walk2", ILoopType.EDefaultLoopTypes.LOOP);
    public static final RawAnimation FLYINGANIM = new RawAnimation("animation.hexblade.dash", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
    public static final RawAnimation FLOATINGANIM = new RawAnimation("animation.model.floattowards", ILoopType.EDefaultLoopTypes.LOOP);
    public static final RawAnimation RAISINGANIM = new RawAnimation("animation.model.raise", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
    public static final RawAnimation JUMPINGANIM = new RawAnimation("animation.model.jump", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);

    public static final RawAnimation IDLE = new RawAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);
    public static final RawAnimation IDLE1 = new RawAnimation("idle", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.CELEBRATE_LOCATION, MemoryModuleType.DANCING, MemoryModuleType.HUNTED_RECENTLY, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.RIDE_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.ATE_RECENTLY);
    protected static final ImmutableList<SensorType<? extends Sensor<? super MagusEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY);
    private boolean rising;
    private int risingtime = 0;
    private boolean dashing = false;
    public boolean spawnedfromitem = false;
    public int tier = 0;
    public static final TrackedData<Integer> TIER;
    public static final TrackedData<Boolean> FLOATING;
    public static final TrackedData<Boolean> FLYING;
    public static final TrackedData<Boolean> RAISING;
    public static final TrackedData<Boolean> JUMPING;

    static {
        TIER = SynchedEntityData.defineId(MagusEntity.class, EntityDataSerializers.INT);
        modifier = SynchedEntityData.defineId(MagusEntity.class, EntityDataSerializers.INT);

        FLOATING = SynchedEntityData.defineId(MagusEntity.class, EntityDataSerializers.BOOLEAN);
        RAISING = SynchedEntityData.defineId(MagusEntity.class, EntityDataSerializers.BOOLEAN);
        FLYING = SynchedEntityData.defineId(MagusEntity.class, EntityDataSerializers.BOOLEAN);
        JUMPING = SynchedEntityData.defineId(MagusEntity.class, EntityDataSerializers.BOOLEAN);

    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    private int dashingtime = 0;

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TIER, 0);
        this.entityData.define(modifier, 0);

        this.entityData.define(FLOATING, false);
        this.entityData.define(FLYING, false);
        this.entityData.define(RAISING, false);
        this.entityData.define(JUMPING, false);

    }



    public void addAdditionalSaveData(CompoundTag compoundTag) {


        compoundTag.putInt("Tier", (Integer) this.entityData.get(TIER));
        compoundTag.putInt("Modifier", (Integer) this.entityData.get(modifier));

        compoundTag.putBoolean("Item1",this.spawnedfromitem);
        compoundTag.putBoolean("floating",this.entityData.get(FLOATING));
        compoundTag.putBoolean("flying",this.entityData.get(FLYING));

        compoundTag.putBoolean("raising",this.entityData.get(RAISING));
        compoundTag.putBoolean("jumping",this.entityData.get(JUMPING));

        super.addAdditionalSaveData(compoundTag);

    }


    public void readAdditionalSaveData(CompoundTag compoundTag) {

        if (compoundTag.contains("Tier")) {
            this.entityData.set(TIER, compoundTag.getInt("Tier"));
        }
        if (compoundTag.contains("Modifier")) {
            this.entityData.set(modifier, compoundTag.getInt("Modifier"));
        }
        if (compoundTag.contains("flying")) {
            this.entityData.set(FLYING, compoundTag.getBoolean("flying"));
        }
        if (compoundTag.contains("raising")) {
            this.entityData.set(RAISING, compoundTag.getBoolean("raising"));
        }
        if (compoundTag.contains("floating")) {
            this.entityData.set(FLOATING, compoundTag.getBoolean("floating"));
        }
        if (compoundTag.contains("jumping")) {
            this.entityData.set(JUMPING, compoundTag.getBoolean("jumping"));
        }
        if (compoundTag.contains("Item1")) {
            this.spawnedfromitem = compoundTag.getBoolean("Item1");
        }
        super.readAdditionalSaveData(compoundTag);

    }
    @Override
    public void setDropChance(EquipmentSlot equipmentSlot, float f) {
    }

    @Override
    public int getHeadRotSpeed() {
        return 9999999;
    }


    @Override
    protected float getEquipmentDropChance(EquipmentSlot equipmentSlot) {
        return 0;
    }

    int thinktime = 0;
    public MagusEntity(EntityType<? extends MagusEntity> p_34652_, World p_34653_) {
        super(p_34652_, p_34653_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 400.0D).add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D).add(Attributes.ATTACK_DAMAGE, 7.0D).add(Attributes.KNOCKBACK_RESISTANCE,1);
    }

    @Override
    public ItemStack getMainHandItem() {
        if(this.getEntityData().get(TIER) % 3 == 0){
            return new ItemStack(SpellbladeItems.arcaneBlade.item());
        }
        if(this.getEntityData().get(TIER) % 3 == 1){
            return new ItemStack(SpellbladeItems.fireBlade.item());
        }
        if(this.getEntityData().get(TIER) % 3 == 2){
            return new ItemStack(SpellbladeItems.frostBlade.item());
        }
        return super.getMainHandItem();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if(damageSource.getEntity() instanceof LivingEntity living && EnchantmentHelper.getEnchantmentWorld(Enchantments.SMITE, living) > 0) {
            this.entityData.set(modifier, this.entityData.get(modifier)+1);
        }
        double damagemodifier = Math.min(1,0.05+(double)this.entityData.get(modifier)/100);
        if(damageSource instanceof SpellDamageSource damageSource1 && damageSource1.getMagicSchool() == this.getMagicSchool()){
            damagetakensincelastthink += f;
            return super.hurt(damageSource, (float) (f));

        }
        else{
            damagetakensincelastthink += f*(damagemodifier);
            return super.hurt(damageSource, (float) (damagemodifier));
        }
    }



    public MagicSchool getMagicSchool(){
        if(this.getMainHandItem().getItem() instanceof SpellbladeItem spellbladeItem){
            if(spellbladeItem.getMagicSchools().stream().anyMatch(asdf -> MagicSchool.fromAttributeId(new ResourceLocation(SpellPowerMod.ID,asdf.name)).equals(MagicSchool.FIRE))){
                return MagicSchool.FIRE;
            }
            if(spellbladeItem.getMagicSchools().stream().anyMatch(asdf -> MagicSchool.fromAttributeId(new ResourceLocation(SpellPowerMod.ID,asdf.name)).equals(MagicSchool.FROST))){
                return MagicSchool.FROST;
            }
            if(spellbladeItem.getMagicSchools().stream().anyMatch(asdf -> MagicSchool.fromAttributeId(new ResourceLocation(SpellPowerMod.ID,asdf.name)).equals(MagicSchool.ARCANE))){
                return MagicSchool.ARCANE;
            }
        }
        return MagicSchool.ARCANE;
    }


    @Override
    public void tick() {
        super.tick();
        if(this.isOnGround()){
            this.getEntityData().set(JUMPING,false);
        }
        if (this.tickCount % 5 == 0 && this.getWorld() instanceof ServerWorld level) {
            List<Entity> magi =  StreamSupport.stream(level.getAllEntities().spliterator(),true).filter(entity -> entity instanceof MagusEntity).toList();
            if(magi.size() > 1 && magi.stream().anyMatch(asdf -> asdf != this && this.tickCount <= asdf.tickCount)){
                if(this.spawnedfromitem) {
                    ItemEntity entity = new ItemEntity(level, this.getBoundingBox().getCenter().x, this.getBoundingBox().getCenter().y, this.getBoundingBox().getCenter().z, new ItemStack(ExampleModFabric.PRISMATICEFFIGY.get()));
                    level.addFreshEntity(entity);
                }
                this.discard();
                return;
            }
           /* for (ServerPlayer serverPlayer : PlayerLookup.tracking(this)) {

                if (getMagicSchool() != MagicSchool.ARCANE && !serverPlayer.getActiveEffectsMap().keySet().containsAll(List.of(SpellbladeNext.DOUSED.get(),SpellbladeNext.MELTED.get()))) {
                    serverPlayer.addEffect(new MobEffectInstance(SpellbladeNext.INERT.get(), 10, 0));
                }
                if (getMagicSchool() != MagicSchool.FIRE && !serverPlayer.getActiveEffectsMap().keySet().containsAll(List.of(SpellbladeNext.INERT.get(),SpellbladeNext.MELTED.get()))) {
                    serverPlayer.addEffect(new MobEffectInstance(SpellbladeNext.DOUSED.get(), 10, 0));
                }
                if (getMagicSchool() != MagicSchool.FROST && !serverPlayer.getActiveEffectsMap().keySet().containsAll(List.of(SpellbladeNext.INERT.get(),SpellbladeNext.DOUSED.get()))) {
                    serverPlayer.addEffect(new MobEffectInstance(SpellbladeNext.MELTED.get(), 10, 0));
                }


            }*/

        }
        if (isInvisible()) {
            if (invisibletime > 60) {
                if (this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
                    Vec3 vec3 = this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get().position().add(new Vec3(1 - 2 * random.nextFloat(), 0, 1 - 2 * random.nextFloat()).normalize());
                    this.teleportTo(vec3.x, vec3.y, vec3.z);
                } else if (this.getWorld().getNearestPlayer(this, 32) != null) {
                    Vec3 vec3 = this.getWorld().getNearestPlayer(this, 32).position().add(new Vec3(1 - 2 * random.nextFloat(), 0, 1 - 2 * random.nextFloat()).normalize());
                    this.teleportTo(vec3.x, vec3.y, vec3.z);
                }
            }
            invisibletime++;
        }
        if (this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
            this.lookAt(this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get(), 999, 999);
            if (this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get().closerThan(this, 2)) {
                this.setInvisible(false);
                invisibletime = 0;
                if (this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get().closerThan(this, 1)) {
                    this.getEntityData().set(FLYING,false);

                    this.dashing = false;
                    this.casting = false;
                    this.noPhysics = false;
                }
            }
        }

        if (this.casting && getMagicSchool() == MagicSchool.ARCANE && this.onGround) {
            this.setInvisible(true);
            this.casting = false;
        }
        if (this.casting && getMagicSchool() == MagicSchool.FIRE && this.onGround && !this.rising) {
            this.rising = true;
            this.setNoGravity(true);
            this.risingtime = 0;
            this.getEntityData().set(RAISING,true);
        }
        if (this.rising) {

            if (this.risingtime < 20) {
                this.setDeltaMovement(0, 0.3, 0);

            }
            if (this.risingtime == 20 && this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
                this.speed = this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get().position().subtract(this.position()).normalize();
                this.getEntityData().set(RAISING,false);
                this.getEntityData().set(FLOATING,true);

                this.setDeltaMovement(speed);

            } else if (this.risingtime == 20 && this.getWorld().getNearestPlayer(this, 32) != null) {
                this.speed = this.getWorld().getNearestPlayer(this, 32).position().subtract(this.position()).normalize();
                this.getEntityData().set(RAISING,false);
                this.getEntityData().set(FLOATING,true);

                this.setDeltaMovement(speed);
            }
            if (this.risingtime > 20) {
                this.setDeltaMovement(speed);
            }
            if ((this.onGround && this.risingtime > 20) || this.risingtime > 40) {
                Spell spell = SpellRegistry.getSpell(new ResourceLocation(SpellbladeNext.MOD_ID, "magus_firenova"));
                if(!this.level.isClientSide()) {
                    ParticleHelper.sendBatches(this, spell.release.particles);
                }

                List<Entity> entities = this.getWorld().getEntitiesOfClass(Entity.class,this.getBoundingBox().inflate(6,2,6),asdf -> asdf != this);
                for(Entity entity : entities){
                    entity.hurt(SpellDamageSource.mob(MagicSchool.FIRE,this),(float)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                }
                this.casting = false;
                this.setNoGravity(false);
                this.rising = false;
                this.risingtime = 0;
                this.getEntityData().set(RAISING,false);
                this.getEntityData().set(FLOATING,false);

            }
            this.risingtime++;
        } else {
            this.setNoGravity(false);
        }
        if (this.casting && this.onGround && this.getMagicSchool() == MagicSchool.FROST) {
            this.noPhysics = true;
            this.dashing = true;
        }
        if (this.dashing) {
            this.getEntityData().set(FLYING,true);

            this.noPhysics = true;
            if (this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
                this.speed = this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get().getBoundingBox().getCenter().subtract(this.position()).normalize();
            } else if (this.getWorld().getNearestPlayer(this, 32) != null) {
                this.speed = this.getWorld().getNearestPlayer(this, 32).getBoundingBox().getCenter().subtract(this.position()).normalize();

            }
            this.setDeltaMovement(speed);
            this.dashingtime++;
        }
        if (this.dashingtime > 40) {
            this.getEntityData().set(FLYING,false);

            this.dashing = false;
            this.casting = false;
            this.noPhysics = false;
            this.dashingtime = 0;
        }


        if (this.swingTime == 12) {
            SoundHelper.playSoundEvent(this.getWorld(), this, SoundEvents.PLAYER_ATTACK_SWEEP);
            Spell.Release.Target.Area area = new Spell.Release.Target.Area();
            area.angle_degrees = 180;
            Predicate<Entity> selectionPredicate = (target) -> {
                return !(target instanceof ReaverEntity);
            };
            List<Entity> list = TargetHelper.targetsFromArea(this, this.getBoundingBox().getCenter(), 2.5F, area, selectionPredicate);
            for (Entity entity : list) {
                if (entity.hurt(SpellDamageSource.mob(getMagicSchool(), this), (float)((float) 2F* this.getAttributeValue(Attributes.ATTACK_DAMAGE) / 3F))) {
                    entity.invulnerableTime = 0;
                    entity.hurt(DamageSource.mobAttack(this), (float) ( 1F * this.getAttributeValue(Attributes.ATTACK_DAMAGE) / 3F));
                }
            }
        }
        if (this.isthinking) {
            thinktime++;
        }
        if (this.thinktime > 40) {
            this.isthinking = false;
            this.thinktime = 0;
        }
    }

    public Brain<MagusEntity> getBrain() {
        return (Brain<MagusEntity>) this.brain;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return false;
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return MagusAI.makeBrain(this,brainProvider().makeBrain(dynamic));
    }
    protected Brain.Provider<MagusEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    public void tickMovement() {
        updateSwingTime();
        super.tickMovement();
    }
    protected void updateSwingTime() {
        int i = 18;

        if (this.swinging) {
            ++this.swingTime;
            if (this.swingTime >= i) {
                this.swingTime = 0;
                this.swinging = false;
            }
        } else {
            this.swingTime = 0;
        }

        this.attackAnim = (float)this.swingTime / (float)i;
    }
    @Override
    public void swing(InteractionHand interactionHand, boolean bl) {
        if (!this.swinging || this.swingTime >= 18 || this.swingTime < 0) {

            this.swingTime = -1;
            this.swinging = true;
            this.swingingArm = interactionHand;
            if (this.level instanceof ServerWorld) {
                ClientboundAnimatePacket clientboundAnimatePacket = new ClientboundAnimatePacket(this, interactionHand == InteractionHand.MAIN_HAND ? 0 : 3);
                ServerChunkCache serverChunkCache = ((ServerWorld)this.level).getChunkSource();
                if (bl) {
                    serverChunkCache.broadcastAndSend(this, clientboundAnimatePacket);
                } else {
                    serverChunkCache.broadcast(this, clientboundAnimatePacket);
                }
            }
        }

    }
    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("magusBrain");
        this.getBrain().tick((ServerWorld)this.level, this);
        this.level.getProfiler().pop();
        MagusAI.updateActivity(this);
        super.customServerAiStep();

    }
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        boolean second = this.random.nextBoolean();
        if(this.swinging && !second) {
            event.getController().markNeedsReload();
            AnimationBuilder asdf3 = new AnimationBuilder();
            asdf3.getRawAnimationList().add(ATTACK);
            event.getController().setAnimation(asdf3);
            this.secondattack = true;
            this.swinging = false;
            return PlayState.CONTINUE;
        }
        if(this.swinging) {
            event.getController().markNeedsReload();
            AnimationBuilder asdf3 = new AnimationBuilder();
            asdf3.getRawAnimationList().add(ATTACK2);
            event.getController().setAnimation(asdf3);
            this.secondattack = false;
            this.swinging = false;

            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;

    }
    private <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        if(this.getEntityData().get(FLYING)){
            AnimationBuilder asdf3 = new AnimationBuilder();
            asdf3.getRawAnimationList().add(FLYINGANIM);
            event.getController().setAnimation(asdf3);
            return PlayState.CONTINUE;
        }
        if(this.getEntityData().get(FLOATING)){
            AnimationBuilder asdf3 = new AnimationBuilder();
            asdf3.getRawAnimationList().add(FLOATINGANIM);
            event.getController().setAnimation(asdf3);
            return PlayState.CONTINUE;
        }
        if(this.getEntityData().get(RAISING)){
            AnimationBuilder asdf3 = new AnimationBuilder();
            asdf3.getRawAnimationList().add(RAISINGANIM);
            event.getController().setAnimation(asdf3);
            return PlayState.CONTINUE;
        }
        if(this.getEntityData().get(JUMPING)){
            AnimationBuilder asdf3 = new AnimationBuilder();
            asdf3.getRawAnimationList().add(JUMPINGANIM);
            event.getController().setAnimation(asdf3);
            return PlayState.CONTINUE;
        }
        if(event.isMoving()){
            if(this.isAggressive()){
                AnimationBuilder asdf3 = new AnimationBuilder();
                asdf3.getRawAnimationList().add(WALK2);
                event.getController().setAnimation(asdf3);
                return PlayState.CONTINUE;

            }
            AnimationBuilder asdf2 = new AnimationBuilder();
            asdf2.getRawAnimationList().add(WALK);
            event.getController().setAnimation(asdf2);
            return PlayState.CONTINUE;

        }
        AnimationBuilder asdf2 = new AnimationBuilder();
        asdf2.getRawAnimationList().add(IDLE);
        event.getController().setAnimation(asdf2);
        return PlayState.CONTINUE;
    }



    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<MagusEntity>(this,"walk",0,this::predicate2));

        animationData.addAnimationController(new AnimationController<MagusEntity>(this,"attack",0,this::predicate));


    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
    public SimpleContainer getInventory() {
        return this.inventory;
    }

}
