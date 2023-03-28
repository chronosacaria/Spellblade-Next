package net.spellbladenext.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerWorld;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.entities.AmethystPersistentProjectileEntity;
import net.spellbladenext.entities.AmethystSpellProjectileEntity;
import net.spellbladenext.entities.EndersGazeCirclingSpellProjectile;
import net.spellbladenext.fabric.ColdAttack;
import net.spellbladenext.fabric.SpinAttack;
import net.spellbladenext.items.FriendshipBracelet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

import static net.spellbladenext.fabric.ExampleModFabric.*;

@Mixin(ServerWorld.class)
public class SpellMixin {
    @Inject(at = @At("HEAD"), method = "addFreshEntity", cancellable = true)
    private void setEntity(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if(entity instanceof SpellProjectile spellProjectile && !(entity instanceof AmethystSpellProjectileEntity)){

            if(spellProjectile.getItem().getItem().equals(Items.AMETHYST_SHARD) && spellProjectile.getOwner() instanceof PlayerEntity playerEntity) {
                for(int ii = 0; ii < 4; ii++) {
                    AmethystPersistentProjectileEntity amethyst = new AmethystPersistentProjectileEntity(SpellbladeNext.AMETHYST, entity.getWorld(), player);
                    amethyst.setPos(player.getEyePosition().add(player.getViewVector(1).normalize()));
                    amethyst.setDeltaMovement(player.getViewVector(1).add(-0.5 + 1 * player.getRandom().nextDouble(), -0.5 + 1 * player.getRandom().nextDouble(), -0.5 + 1 * player.getRandom().nextDouble()).normalize().multiply(2, 2, 2));
                    amethyst.setOwner(player);
                    amethyst.spell = spellProjectile.getSpell();
                    amethyst.context = spellProjectile.getImpactContext();

                    SpellPower.Vulnerability vulnerability = SpellPower.Vulnerability.none;


                    SpellPower.Result power = SpellPower.getSpellPower(MagicSchool.ARCANE, (LivingEntity) spellProjectile.getOwner());
                    amethyst.power = power;
                    entity.getWorld().addFreshEntity(amethyst);
                }
                info.cancel();
            }
        }
        if(entity instanceof SpellProjectile spellProjectile && spellProjectile.getSpell() != null && !(entity instanceof AmethystSpellProjectileEntity)){

            if(spellProjectile.getItem().getItem().equals(Items.AMETHYST_CLUSTER) && spellProjectile.getOwner() instanceof PlayerEntity playerEntity) {
                SpinAttack amethyst = new SpinAttack(spellProjectile.getWorld(),player,player.getX(),player.getY(),player.getZ(), SpellProjectile.Behaviour.FLY,spellProjectile.getSpell(),spellProjectile.getFollowedTarget(),spellProjectile.getImpactContext());
                //amethyst.hero = player;
                //amethyst.setInvisible(true);
                //amethyst.setCustomName(Component.translatable("invisible"));
                entity.getWorld().addFreshEntity(amethyst);
                SpinAttack amethyst2 = new SpinAttack(spellProjectile.getWorld(),player,player.getX(),player.getY(),player.getZ(), SpellProjectile.Behaviour.FLY,spellProjectile.getSpell(),spellProjectile.getFollowedTarget(),spellProjectile.getImpactContext());
                //amethyst.hero = player;
                if(ExampleModFabric.getPlayerPOVHitResult(player.getWorld(),player, ClipContext.Fluid.NONE) != null) {
                    if(ExampleModFabric.getPlayerPOVHitResult(player.getWorld(),player, ClipContext.Fluid.NONE).getType() == BlockHitResult.Type.MISS) {
                        amethyst2.setPos(ExampleModFabric.getPlayerPOVHitResult(player.getWorld(), player, ClipContext.Fluid.NONE).getLocation().subtract(0,1,0));
                    }
                    else {
                        amethyst2.setPos(ExampleModFabric.getPlayerPOVHitResult(player.getWorld(), player, ClipContext.Fluid.NONE).getLocation());
                    }
                    if(spellProjectile.getFollowedTarget() != null){
                        amethyst2.setPos(spellProjectile.getFollowedTarget().position());
                    }
                    if(player.getWorld() instanceof ServerWorld level){
                        //level.playSound(null,player, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS,3.0F,1);
                        int num_pts_line = 40;
                        for(int ii = 0; ii < 4; ii++) {
                            for (int iii = 0; iii < num_pts_line; iii++) {
                                if(iii % 2 == player.getRandom().nextInt(2)) {
                                    double X = player.getX() + (amethyst2.getBoundingBox().getCenter().x - player.getX()) * ((double) iii / (num_pts_line));
                                    double Y = player.getEyeY() + (amethyst2.getBoundingBox().getCenter().y - player.getEyeY()) * ((double) iii / (num_pts_line));
                                    double Z = player.getZ() + (amethyst2.getBoundingBox().getCenter().z - player.getZ()) * ((double) iii / (num_pts_line));

                                    int num_pts = 10;
                                    Vec3 targetcenter = new Vec3(X, Y, Z);
                                    for (ServerPlayerEntity playerEntity1 : level.players()) {
                                        level.sendParticles(player1, ParticleTypes.SMOKE, true, targetcenter.x + -0.5 + player.getRandom().nextDouble(), targetcenter.y+ -0.5 + player.getRandom().nextDouble(), targetcenter.z+ -0.5 + player.getRandom().nextDouble(), 1, 0, 0, 0, 0F);
                                    }
                                }
                                if(iii % 3 == player.getRandom().nextInt(3) && ii % 4 == 1) {
                                    double X = player.getX() + (amethyst2.getBoundingBox().getCenter().x - player.getX()) * ((double) iii / (num_pts_line));
                                    double Y = player.getEyeY() + (amethyst2.getBoundingBox().getCenter().y - player.getEyeY()) * ((double) iii / (num_pts_line));
                                    double Z = player.getZ() + (amethyst2.getBoundingBox().getCenter().z - player.getZ()) * ((double) iii / (num_pts_line));

                                    int num_pts = 10;
                                    Vec3 targetcenter = new Vec3(X, Y, Z);
                                    for (ServerPlayerEntity playerEntity1 : level.players()) {
                                        level.sendParticles(player1, ParticleTypes.ELECTRIC_SPARK, true, targetcenter.x+ -0.25 + 0.5*player.getRandom().nextDouble(), targetcenter.y+ -0.25 + 0.5*player.getRandom().nextDouble(), targetcenter.z+ -0.25 + 0.5*player.getRandom().nextDouble(), 1, 0, 0, 0, 0F);
                                    }
                                }
                            }
                        }
                    }
                    entity.getWorld().addFreshEntity(amethyst2);
                }
                info.cancel();
            }
            if(spellProjectile.getSpell().equals(SpellRegistry.getSpell(new ResourceLocation(SpellbladeNext.MOD_ID, "multislash"))) && spellProjectile.getOwner() instanceof PlayerEntity playerEntity) {
                SpinAttack amethyst = new SpinAttack(spellProjectile.getWorld(),player,player.getX(),player.getY(),player.getZ(), SpellProjectile.Behaviour.FLY,spellProjectile.getSpell(),spellProjectile.getFollowedTarget(),spellProjectile.getImpactContext());
                //amethyst.hero = player;
                //amethyst.setInvisible(true);
                //amethyst.setCustomName(Component.translatable("invisible"));
                entity.getWorld().addFreshEntity(amethyst);

                SpinAttack amethyst2 = new SpinAttack(spellProjectile.getWorld(),player,player.getX(),player.getY(),player.getZ(), SpellProjectile.Behaviour.FLY,spellProjectile.getSpell(),spellProjectile.getFollowedTarget(),spellProjectile.getImpactContext());
                SpinAttack amethyst3 = new SpinAttack(spellProjectile.getWorld(),player,player.getX(),player.getY(),player.getZ(), SpellProjectile.Behaviour.FLY,spellProjectile.getSpell(),spellProjectile.getFollowedTarget(),spellProjectile.getImpactContext());
                amethyst2.setPos(ExampleModFabric.getPlayerPOVHitResultplus(player.getWorld(), player, ClipContext.Fluid.NONE).subtract(0,1,0));
                amethyst2.setPos(amethyst2.position().x,player.getY(),amethyst2.position().z());

                if(amethyst2.position() != player.position() && player.hasLineOfSight(amethyst2))
                entity.getWorld().addFreshEntity(amethyst2);
                amethyst3.setPos(ExampleModFabric.getPlayerPOVHitResultminus(player.getWorld(), player, ClipContext.Fluid.NONE).subtract(0,1,0));
                amethyst3.setPos(amethyst3.position().x,player.getY(),amethyst3.position().z());

                if(amethyst3.position() != player.position() && player.hasLineOfSight(amethyst3)){
                    entity.getWorld().addFreshEntity(amethyst3);
                }
                info.cancel();
            }
            if(spellProjectile.getItem().getItem().equals(Items.BLUE_ICE) && spellProjectile.getOwner() instanceof PlayerEntity playerEntity) {
                ColdAttack amethyst = new ColdAttack(spellProjectile.getWorld(),player,player.getX(),player.getY(),player.getZ(), SpellProjectile.Behaviour.FLY,spellProjectile.getSpell(),spellProjectile.getFollowedTarget(),spellProjectile.getImpactContext());
                amethyst.setPos(player.getEyePosition().add(player.getViewVector(1).normalize()).subtract(0,1,0));


                amethyst.setDeltaMovement(player.getViewVector(1));
                //amethyst.hero = player;
                //amethyst.setInvisible(true);
                //amethyst.setCustomName(Component.translatable("invisible"));
                entity.getWorld().addFreshEntity(amethyst);

                info.cancel();
            }
            if(spellProjectile.getSpell() != null && spellProjectile.getSpell().equals(SpellRegistry.getSpell(new ResourceLocation(SpellbladeNext.MOD_ID, "multicoldfury"))) && spellProjectile.getOwner() instanceof PlayerEntity playerEntity) {
                for(int i = 0; i < 4; i++) {
                    ColdAttack amethyst2 = new ColdAttack(spellProjectile.getWorld(), player, player.getX(), player.getY(), player.getZ(), SpellProjectile.Behaviour.FLY, spellProjectile.getSpell(), spellProjectile.getFollowedTarget(), spellProjectile.getImpactContext());
                    amethyst2.setPos(player.getEyePosition().subtract(0, 1, 0));

                    amethyst2.life = 4;

                    amethyst2.setDeltaMovement(getViewVector(player, 45 + 90*i));
                    //amethyst.hero = player;
                    //amethyst.setInvisible(true);
                    //amethyst.setCustomName(Component.translatable("invisible"));
                    entity.getWorld().addFreshEntity(amethyst2);
                }


                info.cancel();
            }
            if(spellProjectile.getSpell() != null) {
                if (spellProjectile.getSpell().equals(SpellRegistry.getSpell(new ResourceLocation(SpellbladeNext.MOD_ID, "visionofender"))) && spellProjectile.getOwner() instanceof PlayerEntity playerEntity) {
                    Predicate<Entity> selectionPredicate = (target) -> {
                        return (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, player, target)
                                && FriendshipBracelet.PlayerFriendshipPredicate(player, target));
                    };
                    Spell.Release.Target.Area area = new Spell.Release.Target.Area();
                    area.angle_degrees = 360;
                    List<Entity> entities = TargetHelper.targetsFromArea(player, player.getEyePosition(), 4F, area, selectionPredicate);

                    for (Entity entity2 : entities) {
                        for (int i = 1; i < 6; i++) {
                            EndersGazeCirclingSpellProjectile endersGazeCirclingSpellProjectile = new EndersGazeCirclingSpellProjectile(SpellbladeNext.ENDERS_GAZE_ENTITY_TYPE, spellProjectile.getWorld(), player, entity2, i);
                            endersGazeCirclingSpellProjectile.setPos(entity2.getEyePosition());
                            endersGazeCirclingSpellProjectile.power = spellProjectile.getImpactContext().power();
                            endersGazeCirclingSpellProjectile.spell = spellProjectile.getSpell();
                            endersGazeCirclingSpellProjectile.context = spellProjectile.getImpactContext();
                            endersGazeCirclingSpellProjectile.life = 40;
                            player.getWorld().addFreshEntity(endersGazeCirclingSpellProjectile);
                        }

                    }
                    info.cancel();
                }
            }
        }
    }
    public final Vec3 getViewVector(PlayerEntity playerEntity,float f) {
        return this.calculateViewVector(0, player.getViewYRot(1)+f);
    }
    protected final Vec3 calculateViewVector(float f, float g) {
        float h = f * 0.017453292F;
        float i = -g * 0.017453292F;
        float j = Mth.cos(i);
        float k = Mth.sin(i);
        float l = Mth.cos(h);
        float m = Mth.sin(h);
        return new Vec3((double)(k * l), (double)(-m), (double)(j * l));
    }

}