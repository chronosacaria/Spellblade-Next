package net.spellbladenext.entities;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellHelper;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;

public class ExplosionDummy extends AbstractArrow implements ItemSupplier {
    public float range;
    public Spell spell;
    public SpellHelper.ImpactContext context;
    public SpellPower.Result power;
    private Entity followedTarget;
    public Vec3 previousVelocity;
    public int vertical = 0;
    public int horizontal = 0;
    private Spell.ProjectileData clientSyncedData;
    public ExplosionDummy(EntityType<? extends ExplosionDummy> entityType, Level level) {
        super(entityType, level);
    }
    public ExplosionDummy(EntityType<? extends ExplosionDummy> entityType, Level level, Player owner) {
        super(entityType, level);
        this.setOwner(owner);
    }

    @Override
    public void tick() {
        if(this.firstTick){
            this.playSound(SoundEvents.BLAZE_SHOOT);
        }
        if(this.tickCount >= 40 && !this.getLevel().isClientSide() && this.context != null && this.getOwner() instanceof PlayerEntity playerEntity){
            this.discard();
            this.getLevel().explode(player,this.getX(),this.getY(),this.getZ(),(float)SpellPower.getSpellPower(MagicSchool.FIRE, player).nonCriticalValue()/3.666666F,false, Explosion.BlockInteraction.NONE);
        }
        if(this.tickCount >= 60 && !this.getLevel().isClientSide()) {
            this.discard();
        }
        this.firstTick = false;
    }

    @Override
    public ItemStack getItem() {
        return SpellbladeNext.REALEXPLOSION.getDefaultInstance();
    }

    @Override
    protected boolean tryPickup(PlayerEntity playerEntity) {
        return false;
    }

    @Override
    protected ItemStack getPickupItem() {
        return Items.AIR.getDefaultInstance();
    }
}
