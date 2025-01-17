package net.spellbladenext.mixin;

import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerWorld;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.utils.TargetHelper;
import net.spellbladenext.entities.AmethystPersistentProjectileEntity;
import net.spellbladenext.items.FriendshipBracelet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(ServerWorld.class)
public class EchoMixin {
    @Inject(at = @At("HEAD"), method = "addFreshEntity", cancellable = true)
    private void setEntityTecho(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if(entity instanceof SpellProjectile spellProjectile && !(entity instanceof AmethystPersistentProjectileEntity)){

            if(spellProjectile.getItem().getItem().equals(Items.ECHO_SHARD) && spellProjectile.getOwner() instanceof PlayerEntity playerEntity) {
                Position pos1 = (player.getEyePosition().add(player.getViewVector(1).x * 40, player.getViewVector(1).y * 40, player.getViewVector(1).z * 40));

                Predicate<Entity> selectionPredicate = (target) -> {
                    return (TargetHelper.actionAllowed(TargetHelper.TargetingMode.DIRECT, TargetHelper.Intent.HARMFUL, player, target)
                            && FriendshipBracelet.PlayerFriendshipPredicate(player,target));
                };
                Spell.Release.Target.Area area = new Spell.Release.Target.Area();
                List<Entity> list = TargetHelper.targetsFromRaycast(player,40,selectionPredicate);
                for(Entity entity1 : list) {
                    SpellHelper.performImpacts(spellProjectile.getWorld(),player,entity1,spellProjectile.getSpell(),spellProjectile.getImpactContext());
                }
                if(player.getWorld() instanceof ServerWorld level){
                    //level.playSound(null,player, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS,3.0F,1);
                    int num_pts_line = 40;
                    for (int iii = 0; iii < num_pts_line; iii++) {
                        double X = player.getX() + (pos1.x() - player.getX() ) * ((double)iii / (num_pts_line));
                        double Y = player.getEyeY() + (pos1.y() - player.getEyeY() ) * ((double)iii / (num_pts_line));
                        double Z = player.getZ() + (pos1.z() - player.getZ() ) * ((double)iii / (num_pts_line));

                        int num_pts = 10;
                        Vec3 targetcenter = new Vec3(X,Y,Z);
                        for(ServerPlayerEntity playerEntity1 : level.players()) {
                            level.sendParticles(player1,ParticleTypes.SONIC_BOOM, true, targetcenter.x, targetcenter.y, targetcenter.z,1, 0, 0, 0, 0F);
                        }
                    }
                }
                info.cancel();
            }
        }
    }
}