package net.spellbladenext.mixin;

import net.minecraft.server.level.ServerWorld;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.spell_engine.entity.SpellProjectile;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.entities.EndersGazeCirclingSpellProjectile;
import net.spellbladenext.entities.EndersGazeSpellProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class EyeMixin {
    @Inject(at = @At("HEAD"), method = "addFreshEntity", cancellable = true)
    private void setBounding(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if(entity instanceof SpellProjectile spellProjectile && !(entity instanceof EndersGazeSpellProjectile) && !(entity instanceof EndersGazeCirclingSpellProjectile)){

            if(spellProjectile.getItem().getItem().equals(Items.ENDER_EYE) && spellProjectile.getOwner() instanceof PlayerEntity playerEntity) {
                    EndersGazeSpellProjectile amethyst = new EndersGazeSpellProjectile(SpellbladeNext.ENDERS_GAZE_ENTITY_ENTITY_TYPE, entity.getWorld(), player);
                    amethyst.setPos(player.getEyePosition().add(player.getViewVector(1).normalize()));
                    amethyst.setDeltaMovement(player.getViewVector(1).multiply(1, 1, 1));
                    amethyst.setOwner(player);
                amethyst.spell = spellProjectile.getSpell();
                amethyst.context = spellProjectile.getImpactContext();
                    SpellPower.Vulnerability vulnerability = SpellPower.Vulnerability.none;


                    SpellPower.Result power = SpellPower.getSpellPower(MagicSchool.ARCANE, (LivingEntity) spellProjectile.getOwner());
                    amethyst.power = power;
                    entity.getWorld().addFreshEntity(amethyst);

                info.cancel();
            }
        }
    }
}