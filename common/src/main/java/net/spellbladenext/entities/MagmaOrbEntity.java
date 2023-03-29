package net.spellbladenext.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;
import net.spellbladenext.items.FriendshipBracelet;

import java.util.List;
import java.util.function.Predicate;

public class MagmaOrbEntity extends SpellProjectile implements FlyingItemEntity {
    public SpellPower.Result power;
    private int count;
    public Spell spell;
    public SpellHelper.ImpactContext context;

    public MagmaOrbEntity(EntityType<? extends MagmaOrbEntity> entityType, World world) {
        super(entityType, world);
    }
    public MagmaOrbEntity(EntityType<? extends MagmaOrbEntity> entityType, World world, PlayerEntity playerEntity) {
        super(entityType, world);
        this.setOwner(playerEntity);
    }

    int changetime = 0;

    @Override
    public Behaviour behaviour() {
        return Behaviour.FLY;
    }

    @Override
    public void tick() {

        if(firstUpdate){
            SoundEvent soundEvent = SoundEvents.ENTITY_BLAZE_SHOOT;
            this.playSound(soundEvent, 0.25F, 1F);
        }

        super.baseTick();
        this.lastRenderX = this.getX();
        this.lastRenderY = this.getY();
        this.lastRenderZ = this.getZ();
        if(this.getOwner() == null && !this.getWorld().isClient){
            this.discard();
        }
        if(this.changetime > 0) {
            this.changetime--;
        }
        HitResult hitresult = ProjectileUtil.getCollision(this, this::canHit);
        boolean flag = false;
        if (hitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)hitresult).getBlockPos();
            BlockState blockstate = this.getWorld().getBlockState(blockpos);
            if (blockstate.isOf(Blocks.NETHER_PORTAL)) {
                this.setInNetherPortal(blockpos);
                flag = true;
            } else if (blockstate.isOf(Blocks.END_GATEWAY)) {
                BlockEntity blockentity = this.getWorld().getBlockEntity(blockpos);
                if (blockentity instanceof EndGatewayBlockEntity && EndGatewayBlockEntity.canTeleport(this)) {
                    EndGatewayBlockEntity.tryTeleportingEntity(this.getWorld(), blockpos, blockstate, this, (EndGatewayBlockEntity)blockentity);
                }
                flag = true;
            }
        }

        if (hitresult.getType() == HitResult.Type.BLOCK && !this.getWorld().isClient()  && !flag ) {
            this.onCollision(hitresult);
        }

        this.tryCheckBlockCollision();
        Vec3d velocityVec = this.getVelocity();
        double d2 = this.getX() + velocityVec.getX();
        double d0 = this.getY() + velocityVec.getY();
        double d1 = this.getZ() + velocityVec.getZ();
        this.updateRotation();
        float f;
        if (this.isTouchingWater()) {
            for(int i = 0; i < 4; ++i) {
                float f1 = 0.25F;
                this.getWorld().addParticle(
                        ParticleTypes.BUBBLE,
                        d2 - velocityVec.getX() * 0.25D,
                        d0 - velocityVec.getY() * 0.25D,
                        d1 - velocityVec.getZ() * 0.25D,
                        velocityVec.getX(),
                        velocityVec.getY(),
                        velocityVec.getZ());
            }

            f = 0.8F;
        } else {
            f = 0.99F;
        }
        if(!this.getWorld().isClient()) {
            this.setVelocity(velocityVec.multiply(f));
            if (!this.hasNoGravity()) {
                this.setVelocity(velocityVec.getX(), velocityVec.getY() - (double) this.getGravity(), velocityVec.getZ());
            }
        }

        this.setPos(d2, d0, d1);

    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if(hitResult instanceof BlockHitResult result && !this.getWorld().isClient()) {
            final int NUM_POINTS = 96;
            final double RADIUS = 4d;
            if (!this.getWorld().isClient()) {
                if ((result.getSide() == Direction.NORTH) || result.getSide() == Direction.SOUTH) {
                    this.setVelocity(1 * this.getVelocity().getX(), 1 * this.getVelocity().getY(), -1 * this.getVelocity().getZ());
                }
                if ((result.getSide() == Direction.EAST || result.getSide() == Direction.WEST)) {
                    this.setVelocity(-1 * this.getVelocity().getX(), 1 * this.getVelocity().getY(), 1 * this.getVelocity().getZ());
                }
                if (result.getSide() == Direction.UP || result.getSide() == Direction.DOWN ) {
                    this.setVelocity(1 * this.getVelocity().getX(), -1 * this.getVelocity().getY(), 1 * this.getVelocity().getZ());
                    if (result.getSide() == Direction.UP && this.getOwner() instanceof PlayerEntity owner) {
                        if (this.getVelocity().getY() < 0.2) {
                            this.setVelocity(this.getVelocity().getX(), 0.2, this.getVelocity().getZ());
                        }
                        for (int i = 0; i < NUM_POINTS; ++i) {
                            final double angle = Math.toRadians(((double) i / NUM_POINTS) * 360d);

                            double x = Math.cos(angle) * RADIUS;
                            double y = Math.sin(angle) * RADIUS;

                            if(!this.getWorld().isClient())
                                ((ServerWorld) this.world).spawnParticles(ParticleTypes.FLAME, this.getX(), this.getBodyY(0.5), this.getZ(), 1, x, 0.0D, y, 0.2D);
                        }
                        Predicate<Entity> selectionPredicate = (target) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, owner, target)
                                && FriendshipBracelet.PlayerFriendshipPredicate(owner,target));
                        Spell.Release.Target.Area area = new Spell.Release.Target.Area();
                        area.angle_degrees = 360;
                        List<Entity> list = TargetHelper.targetsFromArea(this, result.getPos().add(0,(double)this.getHeight() * 0.5,0), 4, area, selectionPredicate);
                        for(Entity living : list){
                            SpellHelper.performImpacts(this.getWorld(), owner, living, this.spell, this.context);
                        }
                        SoundEvent soundEvent = SoundEvents.ENTITY_BLAZE_SHOOT;
                        this.playSound(soundEvent, 1F, 1F);
                        if (count >= 4)
                            this.discard();
                        this.count++;
                    }
                }
            }
        }
    }
    @Override
    public boolean isAttackable() {
        return false;
    }

    protected float getGravity() {
        return 0.06F;
    }

    @Override
    public ItemStack getStack() {
        return Items.FIRE_CHARGE.getDefaultStack();
    }
}
