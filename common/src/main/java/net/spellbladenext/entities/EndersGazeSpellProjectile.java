package net.spellbladenext.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.items.FriendshipBracelet;

public class EndersGazeSpellProjectile extends SpellProjectile implements FlyingItemEntity {
    public SpellPower.Result power;
    public Spell spell;
    public SpellHelper.ImpactContext context;

    public EndersGazeSpellProjectile(EntityType<? extends EndersGazeSpellProjectile> entityType, World level) {
        super(entityType, level);
    }
    public EndersGazeSpellProjectile(EntityType<? extends EndersGazeSpellProjectile> entityType, World level, PlayerEntity playerEntity) {
        super(entityType, level);
        this.setOwner(playerEntity);
    }
    @Override
    public SpellProjectile.Behaviour behaviour() {
        return SpellProjectile.Behaviour.FLY;
    }
    @Override
    public boolean isAttackable() {
        return false;
    }
    @Override
    public void tick() {
        this.setNoGravity(true);
        if(this.age > 10 && !this.getWorld().isClient()){
            this.discard();
        }
        if(this.firstUpdate){
            this.playSound(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, 1.0F, 1.0F);
        }
        ParticleHelper.play(this.getWorld(),this,this.getPitch(),this.getYaw(), new ParticleBatch("spell_engine:arcane_spell", ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, ParticleBatch.Rotation.LOOK,3,0,0,0));

        super.tick();
    }


    @Override
    public ItemStack getStack() {
        return Items.ENDER_PEARL.getDefaultStack();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.discard();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if(this.getOwner() instanceof PlayerEntity playerEntity 
                && entityHitResult.getEntity() instanceof LivingEntity livingEntity 
                && TargetHelper.actionAllowed(
                        TargetHelper.TargetingMode.DIRECT, 
                        TargetHelper.Intent.HARMFUL, 
                        playerEntity, 
                        entityHitResult.getEntity()
                ) 
                && FriendshipBracelet.PlayerFriendshipPredicate(playerEntity,livingEntity)) {
            for(int i = 1; i < 6; i++) {
                EndersGazeCirclingSpellProjectile endersGazeCirclingSpellProjectile = new EndersGazeCirclingSpellProjectile(SpellbladeNext.ENDERS_GAZE_ENTITY_TYPE, this.getWorld(), playerEntity, entityHitResult.getEntity(), i);
                endersGazeCirclingSpellProjectile.setPosition(entityHitResult.getEntity().getCameraPosVec(1.0f));
                endersGazeCirclingSpellProjectile.power = this.power;
                endersGazeCirclingSpellProjectile.spell = this.spell;
                endersGazeCirclingSpellProjectile.context = this.context;
                if (!this.getWorld().isClient()) {
                    this.getWorld().spawnEntity(endersGazeCirclingSpellProjectile);
                    this.discard();
                }
            }
        }
    }
}
