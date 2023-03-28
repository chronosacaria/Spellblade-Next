package net.spellbladenext.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellHelper;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;

public class ExplosionPersistentProjectileEntity extends PersistentProjectileEntity implements FlyingItemEntity {
    public float range;
    public Spell spell;
    public SpellHelper.ImpactContext context;
    public SpellPower.Result power;
    public ExplosionPersistentProjectileEntity(EntityType<? extends ExplosionPersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    public ExplosionPersistentProjectileEntity(EntityType<? extends ExplosionPersistentProjectileEntity> entityType, World world, PlayerEntity owner) {
        super(entityType, world);
        this.setOwner(owner);
    }

    @Override
    public void tick() {
        if(this.firstUpdate){
            this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
        }
        if(this.age >= 40 && !this.getWorld().isClient() && this.context != null && this.getOwner() instanceof PlayerEntity playerEntity){
            this.discard();
            this.getWorld().createExplosion(playerEntity,this.getX(),this.getY(),this.getZ(),(float)SpellPower.getSpellPower(MagicSchool.FIRE, playerEntity).nonCriticalValue()/3.666666F,false, Explosion.DestructionType.NONE);
        }
        if(this.age >= 60 && !this.getWorld().isClient()) {
            this.discard();
        }
        this.firstUpdate = false;
    }

    @Override
    public ItemStack getStack() {
        return SpellbladeNext.REAL_EXPLOSION.getDefaultStack();
    }

    @Override
    protected boolean tryPickup(PlayerEntity playerEntity) {
        return false;
    }

    @Override
    protected ItemStack asItemStack() {
        return Items.AIR.getDefaultStack();
    }
}
