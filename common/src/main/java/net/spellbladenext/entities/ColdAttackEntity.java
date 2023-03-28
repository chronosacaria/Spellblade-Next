package net.spellbladenext.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
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
import java.util.stream.IntStream;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ColdAttackEntity extends ReaverEntity implements InventoryOwner, IAnimatable {
    private LivingEntity caster = null;
    private SimpleInventory inventory;
    public float range;
    private Spell spell;
    public int life = 20;
    private SpellHelper.ImpactContext context;
    private Spell.ProjectileData clientSyncedData;
    private static  final TrackedData<Boolean> ATTACKING;
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final RawAnimation DASH1 = new RawAnimation("animation.hexblade.dash", ILoopType.EDefaultLoopTypes.LOOP);
    public static final RawAnimation ATTACK = new RawAnimation("animation.hexblade.dash2", ILoopType.EDefaultLoopTypes.LOOP);
    private boolean secondPhase = false;

    public ColdAttackEntity(EntityType<? extends ColdAttackEntity> entityType, World world) {
        super(entityType, world);
        this.getBrain().clear();
        this.range = 128.0F;
        this.setAiDisabled(true);
        this.getDataTracker().set(ATTACKING, false);
    }
    static {
        ATTACKING = DataTracker.registerData(ColdAttackEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(ATTACKING, false);
    }
    public ColdAttackEntity(World world, LivingEntity owner) {
        super(ExampleModFabric.COLDATTACK, world);
        this.getBrain().clear();
        this.range = 128.0F;
        this.caster = owner;
        this.setAiDisabled(true);
        this.getDataTracker().set(ATTACKING, false);

    }
    public ColdAttackEntity(World world, LivingEntity caster, double x, double y, double z, SpellProjectile.Behaviour behaviour, Spell spell, Entity target, SpellHelper.ImpactContext context) {
        this(world, caster);
        this.setPos(x, y, z);
        this.setPitch(caster.getPitch(1));

        this.setYaw(caster.getYaw(1));
        this.setBodyYaw(caster.getYaw(1));
        this.setHeadYaw(caster.getYaw(1));

        this.spell = spell;
        this.context = context;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity playerEntity, Hand hand) {
        return ActionResult.FAIL;
    }

    @Override
    public boolean damage(DamageSource damageSource, float f) {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public LookControl getLookControl() {
        return null;
    }
    public static void rotateTowardsMovement(Entity entity, float f) {
        Vec3d velocityVec = entity.getVelocity();
        if (velocityVec.lengthSquared() != 0.0D) {
            double d = velocityVec.horizontalLength();
            entity.setYaw((float)(MathHelper.atan2(velocityVec.getZ(), velocityVec.getX()) * 57.2957763671875D) - 90.0F);
            entity.setPitch((float)(MathHelper.atan2(d, velocityVec.getY()) * 57.2957763671875D) - 90.0F);

            while(entity.getPitch() - entity.prevPitch < -180.0F) {
                entity.prevPitch -= 360.0F;
            }

            while(entity.getPitch() - entity.prevPitch >= 180.0F) {
                entity.prevPitch += 360.0F;
            }

            while(entity.getYaw() - entity.prevYaw < -180.0F) {
                entity.prevYaw -= 360.0F;
            }

            while(entity.getYaw() - entity.prevYaw >= 180.0F) {
                entity.prevYaw += 360.0F;
            }

            entity.getPitch(MathHelper.lerp(f, entity.prevPitch, entity.getPitch()));
            entity.getYaw(MathHelper.lerp(f, entity.prevYaw, entity.getYaw()));
            entity.getYaw(MathHelper.lerp(f, entity.prevYaw, entity.getYaw()));
            entity.setBodyYaw(MathHelper.lerp(f, -entity.prevYaw, entity.getYaw()));
            entity.setHeadYaw(MathHelper.lerp(f, -entity.prevYaw, entity.getYaw()));
        }
    }
    @Override
    public void tick() {
        rotateTowardsMovement(this,1);
        this.noClip = false;
        this.prevYaw = this.getYaw();
        this.prevBodyYaw = this.getBodyYaw();
        LivingEntity entity = this.getWorld().getClosestEntity(LivingEntity.class, TargetPredicate.createNonAttackable(),this,this.getX(),this.getY(),this.getZ(),this.getBoundingBox().stretch(this.getVelocity()));
        if((this.age > this.life || (entity != null && entity != this.caster && !(entity instanceof ColdAttackEntity)) || this.getWorld().getStatesInBox(this.getBoundingBox().stretch(this.getVelocity())).anyMatch((asdf -> asdf.getMaterial().blocksMovement()))) && !this.getWorld().isClient() ){
            this.secondPhase = true;
            List<ColdAttackEntity> coldAttackEntityList = this.getWorld().getTargets(ColdAttackEntity.class,TargetPredicate.createNonAttackable(),this,this.getBoundingBox().expand(4));
            if(coldAttackEntityList.stream().anyMatch(coldAttack -> coldAttack.caster == this.caster && coldAttack.secondPhase && coldAttack.distanceTo(this) < 3) ){
                this.discard();
                return;
            }
            this.getDataTracker().set(ATTACKING, true);
        }
        if(this.secondPhase){

            this.setVelocity(Vec3d.ZERO);
            double number2 = 0;
            float f7 = 0;
            float f = 0;
            float f1 = -MathHelper.sin(f7 * ((float) Math.PI / 180F)) * MathHelper.cos(f * ((float) Math.PI / 180F));
            float f2 = -MathHelper.sin(f * ((float) Math.PI / 180F));
            float f3 = MathHelper.cos(f7 * ((float) Math.PI / 180F)) * MathHelper.cos(f * ((float) Math.PI / 180F));
            int i = this.random.nextInt(50)*20;

            double[] indices = IntStream.rangeClosed(0, (1000))
                    .mapToDouble(x -> x).toArray();
            if(i < 0 && !this.getWorld().isClient()) {
                this.discard();
                return;
            }
            if(i < 0) {
                return;
            }
                double phi = Math.acos(1 - 2 * indices[i] / 1000);
            double theta = Math.PI * (1 + Math.pow(5, 0.5) * indices[i]);
            double x = cos(theta) * sin(phi);
            double y = -cos(phi);
            double z = Math.sin(theta) * sin(phi);

            Vec3d center = new Vec3d(
                    this.getCameraPosVec(1.0f).getX() + 4 * x + number2 * f1,
                    this.getCameraPosVec(1.0f).getY() + 4 * y + number2 * f2,
                    this.getCameraPosVec(1.0f).getZ() + 4 * z + number2 * f3
            );
            if(spell != null && this.age % 10 == 5) {
                this.getWorld().getNonSpectatingEntities(LivingEntity.class, Box.of(this.getCameraPosVec(1.0f), 8, 8, 8))
                        .forEach(asdf -> SpellHelper.performImpacts(this.getWorld(), this.caster, asdf, this.spell, this.context));
            }
            if (world.getServer() != null) {

                for (ServerPlayerEntity serverPlayerEntity : ((ServerWorld) world).getPlayers(serverPlayer -> serverPlayer.canSee(this))) {
                    ((ServerWorld) world).spawnParticles(ParticleTypes.SWEEP_ATTACK, center.getX(), center.getY(), center.getZ(), 1, 0, 0, 0, 0);
                }
            }
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.25F, 1.0F);

        }
        if(this.age > 60 && !this.getWorld().isClient()) {
            this.discard();
        }
        this.setPosition(this.getVelocity().add(this.getPos()));

    }

    @Override
    public Arm getMainArm() {
        return null;
    }

    @Override
    public SimpleInventory getInventory() {
        return this.inventory;
    }

    private <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        if(             this.getDataTracker().get(ATTACKING)){
            AnimationBuilder animationBuilder = new AnimationBuilder();
            animationBuilder.getRawAnimationList().add(ATTACK);
            event.getController().setAnimation(animationBuilder);
            return PlayState.CONTINUE;

        }
        AnimationBuilder asdf2 = new AnimationBuilder();
        asdf2.getRawAnimationList().add(DASH1);
        event.getController().setAnimation(asdf2);
        return PlayState.CONTINUE;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean collidesWith(Entity entity) {
        return false;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbtCompound) {
        super.writeCustomDataToNbt(nbtCompound);
        nbtCompound.putBoolean("Attacking", this.getDataTracker().get(ATTACKING));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbtCompound) {
        super.readCustomDataFromNbt(nbtCompound);
        if (nbtCompound.contains("Attacking"))
            this.getDataTracker().set(ATTACKING, nbtCompound.getBoolean("Attacking"));
    }
    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(
                new AnimationController<>(this, "attack1", 0, this::predicate2)
        );
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean isLeveledMerchant() {
        return false;
    }
}
