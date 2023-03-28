package net.spellbladenext.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.items.FriendshipBracelet;

import java.util.List;
import java.util.function.Predicate;

public class AmethystPersistentProjectileEntity extends PersistentProjectileEntity implements FlyingItemEntity {
    public boolean inGround = false;
    public double damage = 1;
    public SpellPower.Result power;
    int i = 0;
    List<Entity> detected;
    public SpellHelper.ImpactContext context;
    public Spell spell;


    public AmethystPersistentProjectileEntity(EntityType<? extends AmethystPersistentProjectileEntity> entityType, World world, PlayerEntity playerEntity) {
        super(entityType, world);
        this.setOwner(playerEntity);
        Vec3d playerEntityRotationVec = playerEntity.getRotationVec(0);
        this.setNoGravity(true);

        this.setYaw(((float) (MathHelper.atan2(playerEntity.getRotationVec(0).getX(), playerEntity.getRotationVec(0).z) * (double) (180F / (float) Math.PI))));
        this.setPitch((float) (MathHelper.atan2(playerEntity.getRotationVec(0).getY(), playerEntityRotationVec.horizontalLength()) * (double) (180F / (float) Math.PI)));
        this.prevYaw = ((float) (MathHelper.atan2(playerEntity.getRotationVec(0).getX(), playerEntity.getRotationVec(0).z) * (double) (180F / (float) Math.PI)));
        this.prevPitch = (float) (MathHelper.atan2(playerEntity.getRotationVec(0).getY(), playerEntityRotationVec.horizontalLength()) * (double) (180F / (float) Math.PI));
    }

    public AmethystPersistentProjectileEntity(EntityType<? extends AmethystPersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        if(this.age > 200 && !this.getWorld().isClient()){
            this.discard();
        }
        ParticleHelper.play(this.getWorld(),this,this.getPitch(),this.getYaw(), new ParticleBatch("spell_engine:arcane_spell", ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, ParticleBatch.Rotation.LOOK,3,0,0,0));
        if(this.firstUpdate){
            this.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 1.0F, 1.0F);
        }
        if(this.age <= 20 && !this.getWorld().isClient()){
            this.setVelocity(this.getVelocity().multiply(0.8,0.8,0.8));
        }
        if(this.age > 20) {
            this.setNoGravity(false);
        }
        if(this.age > 20) {
            this.prevPitch = this.getPitch();

            this.setPitch(this.prevPitch + 144);
            this.setPitch(MathHelper.lerp(0.2F, this.prevPitch, this.getPitch()));

            if(this.getOwner() instanceof LivingEntity && !this.getWorld().isClient()) {
                if (this.getOwner() instanceof PlayerEntity playerEntity && this.detected != null) {
                    if (!this.detected.isEmpty() && ((int) (this.i / 4D)) < this.detected.toArray().length && this.i / 4D < 10 && this.power != null) {
                        AmethystSpellProjectileEntity amethystSpellProjectileEntity = new AmethystSpellProjectileEntity(SpellbladeNext.AMETHYST_SPELL_PROJECTILE_ENTITY, this.getWorld(), playerEntity);
                        Vec3d normalize = (this.detected.get((int) (this.i / 4D))).getPos().add(0, this.detected.get((int) (this.i / 4D)).getBoundingBox().getYLength() / 2, 0).subtract(this.getPos()).normalize();
                        amethystSpellProjectileEntity.power = this.power;
                        amethystSpellProjectileEntity.context = this.context;
                        amethystSpellProjectileEntity.spell = this.spell;
                        amethystSpellProjectileEntity.setPosition(this.getPos());
                        amethystSpellProjectileEntity.setVelocity(normalize.getX(), normalize.getY(), normalize.getZ(), 2, 0);
                        this.getWorld().spawnEntity(amethystSpellProjectileEntity);
                        this.i = this.i + 1;
                    } else if (!this.getWorld().isClient()) {
                        this.discard();
                    }
                }
                if (this.detected == null && !this.getWorld().isClient()) {
                    this.discard();
                }
            }
        } else {
            super.baseTick();
            this.setPosition(this.getPos().add(this.getVelocity()));
            if(!this.inGround && !this.getWorld().isClient()) {
                this.prevPitch = this.getPitch();
                this.setPitch(this.prevPitch + 144);
            }
            this.setPitch(MathHelper.lerp(0.2F, this.prevPitch, this.getPitch()));

            Vec3d velocityVec = this.getVelocity();
            Vec3d startVec = this.getPos();
            Vec3d endVec = startVec.add(velocityVec);
            HitResult hitresult = this.getWorld().raycast(new RaycastContext(startVec, endVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));

            if (hitresult.getType() == HitResult.Type.BLOCK) {
                endVec = hitresult.getPos();
                this.discard();
            }
        }
        if(this.getOwner() instanceof PlayerEntity playerEntity && !this.getWorld().isClient()) {
            if (this.age == 20) {
                if(this.detected == null) {
                    Predicate<Entity> selectionPredicate = (target) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, playerEntity, target)
                            && FriendshipBracelet.PlayerFriendshipPredicate(playerEntity,target) && target instanceof LivingEntity);

                    Spell.Release.Target.Area area = new Spell.Release.Target.Area();
                    area.angle_degrees = 360;
                    this.detected = TargetHelper.targetsFromArea(this, this.getPos(),16F, area,  selectionPredicate);

                }
            }
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return Items.AIR.getDefaultStack();
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
    }
    @Override
    protected boolean tryPickup(PlayerEntity playerEntity) {
        return false;
    }
    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {

        this.discard();
        super.onBlockHit(blockHitResult);
    }

    @Override
    public ItemStack getStack() {
        return Items.AMETHYST_SHARD.getDefaultStack();
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}
