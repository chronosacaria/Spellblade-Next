package net.spellbladenext.mixin;

import net.minecraft.server.level.ServerWorld;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.spell_engine.entity.SpellProjectile;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.entities.FlameWindsSpellProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class FlameWindsMixin {
    @Inject(at = @At("HEAD"), method = "addFreshEntity", cancellable = true)
    private void setFlaming(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if(entity instanceof SpellProjectile spellProjectile && !(entity instanceof FlameWindsSpellProjectile)){

            if(spellProjectile.getItem().getItem().equals(Items.MAGMA_CREAM) && spellProjectile.getOwner() instanceof PlayerEntity playerEntity) {
                    FlameWindsSpellProjectile amethyst = new FlameWindsSpellProjectile(SpellbladeNext.FLAME_WINDS_ENTITY_ENTITY_TYPE, entity.getWorld(), player);
                    amethyst.setPos(player.getEyePosition().add(player.getViewVector(1).normalize()));
                    amethyst.setDeltaMovement(player.getViewVector(1).multiply(1, 1, 1));
                    amethyst.setOwner(player);
                amethyst.spell = spellProjectile.getSpell();
                amethyst.context = spellProjectile.getImpactContext().channeled(1);
                    SpellPower.Vulnerability vulnerability = SpellPower.Vulnerability.none;


                    SpellPower.Result power = SpellPower.getSpellPower(MagicSchool.FIRE, (LivingEntity) spellProjectile.getOwner());
                    amethyst.power = power;
                    entity.getWorld().addFreshEntity(amethyst);

                info.cancel();
            }
        }
    }
}