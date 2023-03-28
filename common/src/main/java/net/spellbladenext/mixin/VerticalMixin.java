package net.spellbladenext.mixin;

import net.minecraft.server.level.ServerWorld;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.spell_engine.entity.SpellProjectile;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.spellbladenext.entities.EruptionSpellProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class VerticalMixin {
    @Inject(at = @At("HEAD"), method = "addFreshEntity", cancellable = true)
    private void setFlaming(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if(entity instanceof SpellProjectile spellProjectile && !(entity instanceof EruptionSpellProjectile)){

            if(spellProjectile.getItem().getItem().equals(Items.CAMPFIRE) && spellProjectile.getOwner() instanceof PlayerEntity playerEntity) {
                    EruptionSpellProjectile amethyst = new EruptionSpellProjectile(spellProjectile.getWorld(),player,player.getX(),player.getEyeY(),player.getZ(),spellProjectile.behaviour(),spellProjectile.getSpell(),spellProjectile.getFollowedTarget(),spellProjectile.getImpactContext());
                    amethyst.setOwner(player);
                    amethyst.setPos(player.getEyePosition().add(player.getViewVector(1).normalize()));
                amethyst.setDeltaMovement(player.getViewVector(1).multiply(1, 1, 1).scale(0.666));
                    amethyst.setOwner(player);
                    SpellPower.Vulnerability vulnerability = SpellPower.Vulnerability.none;
                    amethyst.vertical = 1;

                    SpellPower.Result power = SpellPower.getSpellPower(MagicSchool.FIRE, (LivingEntity) spellProjectile.getOwner());
                    entity.getWorld().addFreshEntity(amethyst);

                info.cancel();
            }
            if(spellProjectile.getItem().getItem().equals(Items.SOUL_CAMPFIRE) && spellProjectile.getOwner() instanceof PlayerEntity playerEntity) {
                EruptionSpellProjectile amethyst = new EruptionSpellProjectile(spellProjectile.getWorld(),player,player.getX(),player.getEyeY(),player.getZ(),spellProjectile.behaviour(),spellProjectile.getSpell(),spellProjectile.getFollowedTarget(),spellProjectile.getImpactContext());
                amethyst.setOwner(player);
                amethyst.setPos(player.getEyePosition().add(player.getViewVector(1).normalize()));
                amethyst.setDeltaMovement(player.getViewVector(1).multiply(1, 1, 1).scale(0.666));
                amethyst.setOwner(player);
                SpellPower.Vulnerability vulnerability = SpellPower.Vulnerability.none;
                amethyst.horizontal = 1;

                SpellPower.Result power = SpellPower.getSpellPower(MagicSchool.FIRE, (LivingEntity) spellProjectile.getOwner());
                entity.getWorld().addFreshEntity(amethyst);

                info.cancel();
            }
        }
    }
}