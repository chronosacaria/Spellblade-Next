package net.spellbladenext.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.spell_engine.SpellEngineMod;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.items.FriendshipBracelet;

public class CleansingFlameEntity extends SpellProjectile implements FlyingItemEntity {
    public Entity target;
    public SpellPower.Result power;
    public SpellHelper.ImpactContext context;
    public Spell spell;
public int life = 0;
    public CleansingFlameEntity(EntityType<? extends CleansingFlameEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ItemStack getStack() {
        return SpellbladeNext.EXPLOSION.getDefaultStack();
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public Behaviour behaviour() {
        return Behaviour.FLY;
    }

    @Override
    public void tick() {
        this.setNoGravity(true);
        if(this.firstUpdate && Registry.SOUND_EVENT.get(new Identifier(SpellEngineMod.ID,"generic_fire_release")) != null)
            playSound(Registry.SOUND_EVENT.get(new Identifier(SpellEngineMod.ID,"generic_fire_release")),0.1F,1F);
        if(this.target != null && this.getOwner() != null && this.power != null && this.context != null && this.getOwner() instanceof LivingEntity living) {
            Vec3d vec3 = this.target.getEyePos().subtract(this.getPos());
            this.setVelocity(this.getVelocity().multiply(0.95D).add(vec3.normalize().multiply(0.15)));
            if(this.getBoundingBox().stretch(this.getVelocity()).intersects(this.target.getBoundingBox())) {

            }
        }
        if(this.age > 40 && !this.getWorld().isClient){
            this.discard();
        }
        if(this.life > 0 && this.age > this.life && !this.getWorld().isClient){
            this.discard();
        }
        this.setPos(this.getPos().add(this.getVelocity()).getX(), this.getPos().add(this.getVelocity()).getY(), this.getPos().add(this.getVelocity()).getZ());
        super.tick();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if(this.getOwner() instanceof PlayerEntity living && this.spell != null && this.context != null && FriendshipBracelet.PlayerFriendshipPredicate(living,entityHitResult.getEntity())) {
            if (entityHitResult.getEntity().timeUntilRegen <= 10 && SpellHelper.performImpacts(this.getWorld(), living, entityHitResult.getEntity(), this.spell, this.context)) {
                entityHitResult.getEntity().timeUntilRegen = 20;
            }
        }
        if(!this.getWorld().isClient()) {
            this.discard();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.discard();
        super.onBlockHit(blockHitResult);
    }
}
