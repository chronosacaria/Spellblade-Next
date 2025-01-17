package net.spellbladenext.mixin;

import net.minecraft.server.level.ServerWorld;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.spell_engine.entity.SpellProjectile;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.entities.IceThornSpellProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class ThornMixin {
    @Inject(at = @At("HEAD"), method = "addFreshEntity", cancellable = true)
    private void setBounding(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if(entity instanceof SpellProjectile spellProjectile && !(entity instanceof IceThornSpellProjectile)){

            if(spellProjectile.getItem().getItem().equals(Items.SNOWBALL) && spellProjectile.getOwner() instanceof PlayerEntity playerEntity) {
                for(int i = 0; i < 3; i++) {
                    IceThornSpellProjectile amethyst = new IceThornSpellProjectile(SpellbladeNext.ICE_THORN_ENTITY_TYPE, entity.getWorld(), player);
                    amethyst.setPos(player.getEyePosition().add(player.getViewVector(1).normalize()));
                    //amethyst.setDeltaMovement(player.getViewVector(1).multiply(0.5, 0.5, 0.5));
                    amethyst.setOwner(player);
                    SpellPower.Vulnerability vulnerability = SpellPower.Vulnerability.none;

                    SpellPower.Result power = SpellPower.getSpellPower(MagicSchool.FROST, (LivingEntity) spellProjectile.getOwner());
                    amethyst.power = spellProjectile.getImpactContext().power();
                    amethyst.spell = spellProjectile.getSpell();
                    amethyst.context = spellProjectile.getImpactContext().channeled(1);

                    entity.getWorld().addFreshEntity(amethyst);

                }
                info.cancel();
            }
        }
    }
}