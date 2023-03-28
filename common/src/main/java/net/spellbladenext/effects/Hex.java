package net.spellbladenext.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.spellbladenext.entities.ReaverEntity;
import net.spellbladenext.events.AttackEvent;
import net.spellbladenext.interfaces.IPiglinSummon;

import java.util.Optional;
import java.util.stream.StreamSupport;

public class Hex extends StatusEffect {
    protected Hex(StatusEffectCategory statusEffectCategory, int i) {
        super(statusEffectCategory, i);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity instanceof PlayerEntity playerEntity && playerEntity.getWorld() instanceof ServerWorld serverWorld){
            ReaverEntity reaverEntity1 = playerEntity.getWorld().getClosestEntity(ReaverEntity.class, TargetPredicate.createNonAttackable(), playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), playerEntity.getBoundingBox().expand(50,50,50));
            ReaverEntity reaverEntity = new ReaverEntity(ExampleModFabric.REAVER, playerEntity.getWorld());
            reaverEntity.isScout = true;
            reaverEntity.nemesis = playerEntity;
            BlockPos pos = IPiglinSummon.getSafePositionAroundPlayer(playerEntity.getWorld(), playerEntity.getLandingPos(), 50);

            if (pos != null) {
                boolean bool = StreamSupport.stream(serverWorld.iterateEntities().spliterator(),true).toList().stream().noneMatch(asdf -> asdf instanceof ReaverEntity reaver2 && reaver2.isScout() && reaver2.nemesis == playerEntity);
                if(bool) {
                    reaverEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
                    reaverEntity.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(playerEntity.getPos(), 2, 2));
                    playerEntity.getWorld().spawnEntity(reaverEntity);
                }

            }
            if(reaverEntity1 != null && reaverEntity1.isScout() &&  reaverEntity.nemesis == playerEntity && !reaverEntity1.returningHome && !playerEntity.hasStatusEffect(StatusEffects.INVISIBILITY)){
                reaverEntity1.getBrain().remember(MemoryModuleType.WALK_TARGET,new WalkTarget(playerEntity, 1.4F, 1));
            }

        }
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int i, int j) {
        return true;
    }


    @Override
    public void onRemoved(LivingEntity livingEntity, AttributeContainer attributeContainer, int i) {
        super.onRemoved(livingEntity, attributeContainer, i);

        if(livingEntity instanceof PlayerEntity playerEntity && !playerEntity.getWorld().isClient()){
            Optional<BlockPos> pos = BlockPos.findClosest(playerEntity.getBlockPos(),64,128,
                    asdf -> playerEntity.getWorld().getBlockState(asdf).getBlock().equals(ExampleModFabric.HEXBLADE));
            if(pos.isPresent()){
                playerEntity.sendMessage(Text.translatable("Your triumph is respected."));
                return;
            }
            if(!playerEntity.getInventory().containsAny(itemStack -> itemStack.isOf(ExampleModFabric.OFFERING.get()))){
                ExampleModFabric.attackeventArrayList.add(new AttackEvent(playerEntity.getWorld(), playerEntity));
            } else{
                playerEntity.sendMessage(Text.translatable("Your patronage has saved you. For now."));

                if(playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                    serverPlayerEntity.getStatHandler().setStat(serverPlayerEntity, Stats.CUSTOM.getOrCreateStat(ExampleModFabric.SINCELASTHEX), 0);
                }
                if(playerEntity.getStackInHand(Hand.MAIN_HAND).isOf(ExampleModFabric.OFFERING.get())) {
                    ItemStack stack = playerEntity.getStackInHand(Hand.MAIN_HAND);
                    stack.decrement(1);
                    if (stack.isEmpty()) {
                        playerEntity.getInventory().removeOne(stack);
                    }
                } else if(playerEntity.getStackInHand(Hand.OFF_HAND).isOf(ExampleModFabric.OFFERING.get())) {
                    ItemStack stack = playerEntity.getStackInHand(Hand.OFF_HAND);
                    stack.decrement(1);
                } else {
                    for (int ii = 0; ii < playerEntity.getInventory().size(); ++ii) {
                        ItemStack stack = playerEntity.getInventory().getStack(ii);
                        if (stack.isOf(ExampleModFabric.OFFERING.get())) {
                            stack.decrement(1);
                            if (stack.isEmpty()) {
                                playerEntity.getInventory().removeOne(stack);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}
