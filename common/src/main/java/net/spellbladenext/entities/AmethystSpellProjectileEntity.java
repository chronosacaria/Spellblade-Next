package net.spellbladenext.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_power.api.SpellPower;

public class AmethystSpellProjectileEntity extends SpellProjectile implements FlyingItemEntity {
    public LivingEntity target;
    public double damage = 1;
    public SpellPower.Result power;
    public Spell spell;
    public SpellHelper.ImpactContext context;


    public AmethystSpellProjectileEntity(EntityType<? extends AmethystSpellProjectileEntity> entityType, World world, PlayerEntity playerEntity) {
        super(entityType, world);
        this.setOwner(playerEntity);
        Vec3d playerEntityRotationVec = playerEntity.getRotationVec(0);
        this.setNoGravity(true);

        this.setYaw(((float) (MathHelper.atan2(playerEntityRotationVec.getX(),playerEntityRotationVec.getZ()) * (double) (180F / (float) Math.PI))));
        this.setPitch((float) (MathHelper.atan2(playerEntityRotationVec.getY(), playerEntityRotationVec.horizontalLength()) * (double) (180F / (float) Math.PI)));
        this.prevYaw = ((float) (MathHelper.atan2(playerEntityRotationVec.getX(), playerEntityRotationVec.getZ()) * (double) (180F / (float) Math.PI)));
        this.prevPitch = (float) (MathHelper.atan2(playerEntityRotationVec.getY(), playerEntityRotationVec.horizontalLength()) * (double) (180F / (float) Math.PI));

    }
    public AmethystSpellProjectileEntity(EntityType<? extends AmethystSpellProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);

    }
    @Override
    public Behaviour behaviour() {
        return Behaviour.FLY;
    }
    @Override
    public void tick() {
        if(this.age > 80 && !this.getWorld().isClient()) {
            this.discard();
        }
        super.tick();
        Vec3d velocityVec = this.getVelocity();

        this.setYaw(((float) (MathHelper.atan2(velocityVec.getX(), velocityVec.getZ()) * (double) (180F / (float) Math.PI))));
        this.setPitch((float) (MathHelper.atan2(velocityVec.getY(), velocityVec.horizontalLength()) * (double) (180F / (float) Math.PI)));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if(this.getOwner() instanceof LivingEntity owner && this.power != null && this.spell != null && this.context != null && entityHitResult.getEntity().timeUntilRegen <= 10) {
            if(SpellHelper.performImpacts(this.getWorld(), owner, entityHitResult.getEntity(), this.spell,this.context)) {
                entityHitResult.getEntity().timeUntilRegen = 20;
            }
        }
        this.discard();
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
}
