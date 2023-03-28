package net.spellbladenext.entities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.utils.SoundHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.blocks.blockentities.NetherPortalFrame;
import net.spellbladenext.interfaces.IPiglinSummon;
import net.spellbladenext.items.spellbladeitems.SpellbladeItem;
import net.spellbladenext.items.spellbladeitems.SpellbladeItems;
import org.jetbrains.annotations.Nullable;
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
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Predicate;

public class Reaver extends PathAwareEntity implements InventoryOwner, IAnimatable, Merchant {
    public PlayerEntity nemesis;
    public boolean isthinking = false;
    public boolean isScout = false;
    private boolean hasntthrownitems = true;
    private boolean firstattack = false;
    private boolean secondattack = false;
    private boolean isstopped = false;
    boolean isCaster = false;
    private PlayerEntity tradingplayer;
    float damagetakensincelastthink = 0;

    public Reaver(EntityType<? extends Reaver> entityType, World world) {
        super(entityType, world);
    }
    private final SimpleInventory inventory = new SimpleInventory(8);
    private static final Set<Item> WANTED_ITEMS = ImmutableSet.of(Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS);
    public boolean returningHome = false;
    public boolean isleader = false;
    public int homecount = 0;
    public int homecount2 = 0;
    public PlayerEntity hero = null;
    public boolean canGiveGifts = false;
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final RawAnimation ATTACK = new RawAnimation("animation.hexblade.new", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    public static final RawAnimation ATTACK2 = new RawAnimation("animation.hexblade.new2", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    public static final RawAnimation WALK = new RawAnimation("animation.hexblade.walk", ILoopType.EDefaultLoopTypes.LOOP);
    public static final RawAnimation WALK2 = new RawAnimation("animation.hexblade.walk2", ILoopType.EDefaultLoopTypes.LOOP);
    public static final RawAnimation IDLE = new RawAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);
    public static final RawAnimation IDLE1 = new RawAnimation("idle", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.CELEBRATE_LOCATION, MemoryModuleType.DANCING, MemoryModuleType.HUNTED_RECENTLY, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.RIDE_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.ATE_RECENTLY, MemoryModuleType.NEAREST_REPELLENT);
    protected static final ImmutableList<SensorType<? extends Sensor<? super Reaver>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_SPECIFIC_SENSOR);

    @Override
    public void setEquipmentDropChance(EquipmentSlot equipmentSlot, float f) {
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
    }

    @Override
    protected float getDropChance(EquipmentSlot equipmentSlot) {
        return 0;
    }

    public boolean isCaster(){
        return this.isCaster;
    }
    public boolean isScout(){
        return this.isScout;
    }

    public boolean canGiveGifts(){
        return this.canGiveGifts;
    }
    private static Optional<? extends LivingEntity> getPreferredTarget(Reaver piglin) {
        Brain<?> brain = piglin.getBrain();
            Optional<LivingEntity> optional = LookTargetUtil.getEntity(piglin, MemoryModuleType.ANGRY_AT);
            if (optional.isPresent() && Sensor.testAttackableTargetPredicateIgnoreVisibility(piglin, optional.get())) {
                return optional;
            } else {
                Optional optional2;
                if (brain.hasMemoryModule(MemoryModuleType.UNIVERSAL_ANGER)) {
                    optional2 = brain.getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
                    if (optional2.isPresent()) {
                        return optional2;
                    }
                }
                optional2 = brain.getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
                    return optional2;

            }
    }

    @Override
    public Brain<?> getBrain() {
        return super.getBrain();
    }

    // NOTE: Is this mob intended to be a type of Piglin?
    static boolean getPreferredTarget(Reaver piglin, LivingEntity livingEntity) {
        return getPreferredTarget(piglin).filter((livingEntity2) -> livingEntity2 == livingEntity).isPresent();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbtCompound) {
        nbtCompound.putBoolean("Caster", this.isCaster);
        nbtCompound.putBoolean("Scout", this.isScout);
        super.writeCustomDataToNbt(nbtCompound);

    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbtCompound) {
        this.isCaster = nbtCompound.getBoolean("Caster");
        this.isScout = nbtCompound.getBoolean("Scout");
        super.readCustomDataFromNbt(nbtCompound);

    }

    @Override
    public int getMaxLookYawChange() {
        return 9999999;
    }

    protected boolean isImmuneToZombification() {
        return true;
    }
    public boolean attacking = false;
    public boolean justattacked = false;
    public int attackTicker = 0;
    public int attackTime = 0;

    @Override
    public boolean shouldRenderName() {
        return false;
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public void tickMovement() {
        updateSwingTime();

        super.tickMovement();
    }
    @Override
    public void tick() {
        super.tick();


        if(this instanceof SpinAttack || this instanceof ColdAttack){
            return;
        }
        if (this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
            this.lookAtEntity(this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get(), 999, 999);
        }
        if(homecount2 > 1000){
        this.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
        this.discard();
        }
        List<Reaver> piglins = this.getWorld().getEntitiesByClass(Reaver.class, this.getBoundingBox().expand(32), piglin -> !piglin.getBrain().hasActivity(Activity.IDLE) || piglin.age < 1000);
        if(piglins.isEmpty() && this.age % 10 == 0){
            List<Reaver> piglins2 = this.getWorld().getEntitiesByClass(Reaver.class,this.getBoundingBox().expand(32), piglin -> true);
            for(Reaver piglin : piglins2){
                piglin.returningHome = true;
            }
        }
        List<Reaver> piglins2 = this.getWorld().getEntitiesByClass(Reaver.class,this.getBoundingBox().expand(32), piglin -> piglin.isleader);
        if(piglins2.isEmpty()){
            this.isleader = true;
            this.homecount = -200;
        }
        if(returningHome && isleader){
            homecount++;
        }
        if(returningHome){
            homecount2++;
        }
        if(this.handSwingTicks == 12){
            SoundHelper.playSoundEvent(this.getWorld(),this, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP);
            Spell.Release.Target.Area area = new Spell.Release.Target.Area();
            area.angle_degrees = 180;
            Predicate<Entity> selectionPredicate = (target) -> !(target instanceof Reaver);
            List<Entity> list = TargetHelper.targetsFromArea(this, this.getBoundingBox().getCenter(),2.5F, area,  selectionPredicate);
            for(Entity entity : list){
                if(entity.damage(SpellDamageSource.mob(getMagicSchool(),this),(float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / 2)) {
                    entity.timeUntilRegen = 0;
                    entity.damage(DamageSource.mob(this), (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / 2);
                }
            }
        }
        this.attackTime++;
        if(this.attackTime > 18){
            this.justattacked = false;
            this.attackTime = 0;
            this.firstattack = false;
            this.secondattack = false;
        }

        if(this.homecount % 200 == 1){
            Optional<NetherPortalFrame> frame = IPiglinSummon.summonNetherPortal(this.getWorld(),this,true);
            int ii = 0;
            while(frame.isEmpty() && ii < 10){
                frame = IPiglinSummon.summonNetherPortal(this.getWorld(),this,true);
                ii++;
            }
        }


    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return null;
    }

    protected SoundEvent getDeathSound() {
        return null;
    }

    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.ENTITY_PIGLIN_BRUTE_STEP, 0.15F, 1.0F);
    }

    protected void playAngrySound() {
    }
    public MagicSchool getMagicSchool(){
        if(this.getMainHandStack().getItem() instanceof SpellbladeItem spellbladeItem){
            if(spellbladeItem.getMagicSchools().stream().anyMatch(asdf -> MagicSchool.fromAttributeId(new Identifier(SpellPowerMod.ID,asdf.name)).equals(MagicSchool.FIRE))){
                return MagicSchool.FIRE;
            }
            if(spellbladeItem.getMagicSchools().stream().anyMatch(asdf -> MagicSchool.fromAttributeId(new Identifier(SpellPowerMod.ID,asdf.name)).equals(MagicSchool.FROST))){
                return MagicSchool.FROST;
            }
            if(spellbladeItem.getMagicSchools().stream().anyMatch(asdf -> MagicSchool.fromAttributeId(new Identifier(SpellPowerMod.ID,asdf.name)).equals(MagicSchool.ARCANE))){
                return MagicSchool.ARCANE;
            }
        }
        return MagicSchool.ARCANE;
    }

    @Override
    protected boolean shouldDropLoot() {
        return true;
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
        if(damageSource.getAttacker() instanceof PlayerEntity playerEntity
                && playerEntity.hasStatusEffect(ExampleModFabric.HEX.get())){
            playerEntity.removeStatusEffect(ExampleModFabric.HEX.get());
        }
        super.dropLoot(damageSource, causedByPlayer);
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        return super.canSpawn(world, spawnReason);
    }

    @Override
    public boolean damage(DamageSource damageSource, float f) {
        if(damageSource.getSource() instanceof PlayerEntity
                && this.isScout()
                && this.getHealth() / this.getMaxHealth()
                <= 0.5 && this.getMainHandStack().isEmpty()){
           this.tryEquip(new ItemStack(SpellbladeItems.entries.get(this.random.nextInt(SpellbladeItems.entries.size())).item()));
        }
        return super.damage(damageSource, f);

    }

    public static DefaultAttributeContainer.Builder createHostileAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3499999940395355D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,0.5);
    }
    protected void updateSwingTime() {
        int i = 18;

        if (this.handSwinging) {
            ++this.handSwingTicks;
            if (this.handSwingTicks >= i) {
                this.handSwingTicks = 0;
                this.handSwinging = false;
            }
        } else {
            this.handSwingTicks = 0;
        }

        this.handSwingProgress = (float)this.handSwingTicks / (float)i;
    }


    @Override
    public void swingHand(Hand hand, boolean fromServerPlayer) {
        if (!this.handSwinging || this.handSwingTicks >= 18 || this.handSwingTicks < 0) {

            this.handSwingTicks = -1;
            this.handSwinging = true;
            this.preferredHand = hand;
            if (this.getWorld() instanceof ServerWorld) {
                EntityAnimationS2CPacket entityAnimationS2CPacket = new EntityAnimationS2CPacket(this, hand == InteractionHand.MAIN_HAND ? 0 : 3);
                ServerChunkManager serverChunkManager = ((ServerWorld)this.getWorld()).getChunkManager();
                if (fromServerPlayer) {
                    serverChunkManager.sendToNearbyPlayers(this, entityAnimationS2CPacket);
                } else {
                    serverChunkManager.sendToOtherNearbyPlayers(this, entityAnimationS2CPacket);
                }
            }
        }

    }
    @Override
    public boolean tryAttack(Entity entity) {
        return false;
    }

    protected static boolean canHarvest(Reaver piglin){
        return piglin.getMainHandStack().getItem() instanceof HoeItem;
    }



    @Override
    protected void loot(ItemEntity itemEntity) {

    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return ReaverAI.makeBrain(this, brainProvider().deserialize(dynamic));
    }
    protected Brain.Profile<Reaver> brainProvider() {
        return Brain.createProfile(MEMORY_TYPES, SENSOR_TYPES);
    }
    @Override
    protected void mobTick() {
        this.getWorld().getProfiler().push("reaverBrain");
        this.getBrain().tick((ServerWorld) this.getWorld(), this);
        this.getWorld().getProfiler().pop();
        ReaverAI.updateActivity(this);
        super.mobTick();
    }
    @Override
    public SimpleInventory getInventory() {
        return this.inventory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        boolean second = this.random.nextBoolean();
        if(this.handSwinging && !second) {
            event.getController().markNeedsReload();
            AnimationBuilder asdf3 = new AnimationBuilder();
            asdf3.getRawAnimationList().add(ATTACK);
            event.getController().setAnimation(asdf3);
            this.secondattack = true;
            this.handSwinging = false;
            return PlayState.CONTINUE;
        }
        if(this.handSwinging) {
            event.getController().markNeedsReload();
            AnimationBuilder asdf3 = new AnimationBuilder();
            asdf3.getRawAnimationList().add(ATTACK2);
            event.getController().setAnimation(asdf3);
            this.secondattack = false;
            this.handSwinging = false;

            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;

    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.getOffers().isEmpty() || !this.getMainHandStack().isEmpty()) {
            return ActionResult.FAIL;
        } else {
            this.setCustomer(player);
            this.openTradingScreen(player, Text.translatable("Protection comes at a price"), 1);

            return ActionResult.SUCCESS;
        }
    }

    private <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        if(event.isMoving()){
            if(this.isAttacking()){
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
        animationData.addAnimationController(new AnimationController<Reaver>(this,"walk",0,this::predicate2));

        animationData.addAnimationController(new AnimationController<Reaver>(this,"attack",0,this::predicate));


    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public Text getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity playerEntity) {
        this.tradingplayer = playerEntity;
    }

    @Nullable
    @Override
    public PlayerEntity getCustomer() {
        return this.tradingplayer;
    }

    @Override
    public TradeOfferList getOffers() {
        TradeOfferList offers = new TradeOfferList();
        ItemStack offering = new ItemStack(ExampleModFabric.OFFERING.get());
        offers.add(new TradeOffer(
                new ItemStack(SpellbladeNext.RUNEBLAZE_PLATING.get(),8),
                offering,10,8,0.02F));
        offers.add(new TradeOffer(
                new ItemStack(SpellbladeNext.RUNEGLINTPLATING.get(),8),
                offering,10,8,0.02F));
        offers.add(new TradeOffer(
                new ItemStack(SpellbladeNext.RUNEFROSTED_INGOT.get(),8),
                offering,10,8,0.02F));
        for(var entry : SpellbladeItems.entries) {
            offers.add(new TradeOffer(
                    new ItemStack(entry.item(), 8),
                    offering, 10, 8, 0.02F));
        }

        return offers;
    }

    @Override
    public void setOffersFromServer(TradeOfferList merchantOffers) {

    }

    @Override
    public void trade(TradeOffer merchantOffer) {

    }

    @Override
    public void onSellingItem(ItemStack itemStack) {

    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public void setExperienceFromServer(int i) {

    }

    @Override
    public boolean isLeveledMerchant() {
        return false;
    }

    @Override
    public SoundEvent getYesSound() {
        return null;
    }

    @Override
    public boolean canRefreshTrades() {
        return Merchant.super.canRefreshTrades();
    }

    @Override
    public void sendOffers(PlayerEntity player, Text test, int levelProgress) {
        Merchant.super.sendOffers(player, test, levelProgress);
    }

    public void openTradingScreen(PlayerEntity playerEntity, Text text, int i) {
        OptionalInt optionalInt = playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((ix, inventory, playerx) -> new MerchantScreenHandler(ix, inventory, this), text));
        if (optionalInt.isPresent() && this.getMainHandStack().isEmpty()) {
            TradeOfferList merchantOffers = this.getOffers();
            if (!merchantOffers.isEmpty()) {
                playerEntity.sendTradeOffers(optionalInt.getAsInt(), merchantOffers, i, this.getExperience(), this.isLeveledMerchant(), this.canRefreshTrades());
            }
        }

    }

    @Override
    public boolean isClient() {
        return false;
    }
}

