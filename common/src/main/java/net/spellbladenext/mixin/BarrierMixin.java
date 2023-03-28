package net.spellbladenext.mixin;

import net.minecraft.server.level.ServerWorld;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.spell_engine.entity.SpellProjectile;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.entities.AmethystPersistentProjectileEntity;
import net.spellbladenext.entities.IcicleBarrierEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class BarrierMixin {
    @Inject(at = @At("HEAD"), method = "addFreshEntity", cancellable = true)
    private void setBounding(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if(entity instanceof SpellProjectile spellProjectile && !(entity instanceof AmethystPersistentProjectileEntity)){

            if(spellProjectile.getItem().getItem().equals(SpellbladeNext.ICICLE_2.get()) && spellProjectile.getOwner() instanceof PlayerEntity playerEntity) {
                    IcicleBarrierEntity amethyst = new IcicleBarrierEntity(SpellbladeNext.ICICLE_BARRIER_ENTITY_ENTITY_TYPE, entity.getWorld(), player);
                    amethyst.setPos(player.getEyePosition().add(player.getViewVector(1).normalize().multiply(4,4,4)));
                    amethyst.setOwner(player);
                    SpellPower.Vulnerability vulnerability = SpellPower.Vulnerability.none;


                    SpellPower.Result power = SpellPower.getSpellPower(MagicSchool.FROST, (LivingEntity) spellProjectile.getOwner());
                    amethyst.power = power;
                amethyst.spell = spellProjectile.getSpell();
                amethyst.context = spellProjectile.getImpactContext().channeled(1);

                entity.getWorld().addFreshEntity(amethyst);

                info.cancel();
            }
        }
    }
}