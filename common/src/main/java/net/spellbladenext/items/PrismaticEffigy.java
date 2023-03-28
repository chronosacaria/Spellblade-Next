package net.spellbladenext.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.spellbladenext.entities.MagusEntity;
import net.spellbladenext.interfaces.IPiglinSummon;
import net.spellbladenext.mixin.EntityAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.StreamSupport;

public class PrismaticEffigy extends Item {
    public PrismaticEffigy(Settings properties) {
        super(properties);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world instanceof ServerWorld serverWorld && StreamSupport.stream(serverWorld.iterateEntities().spliterator(),true).filter(entity -> entity instanceof MagusEntity).count() < 1) {
            for (int i = 0; i < 10; i++) {
                BlockPos vec3 = IPiglinSummon.getSafePositionAroundPlayer(world, ((EntityAccessor)user).callGetPosWithYOffset(0.2F), 10);
                if (vec3 != null && !world.isClient()) {
                    MagusEntity magusEntity = new MagusEntity(ExampleModFabric.MAGUS, world);
                    magusEntity.setPos(vec3.getX(), vec3.getY(), vec3.getZ());
                    if (!user.isCreative()) {
                        user.getStackInHand(hand).decrement(1);
                        if (user.getStackInHand(hand).isEmpty()) {
                            user.getInventory().removeOne(user.getStackInHand(hand));
                        }
                        magusEntity.spawnedfromitem = true;
                    }
                    world.spawnEntity(magusEntity);
                    return TypedActionResult.consume(user.getStackInHand(hand));

                }
            }
            user.sendMessage(Text.translatable("Magus has no room at your location"));
        }
        else{
            if(world instanceof ServerWorld) {
                user.sendMessage(Text.translatable("Magus is busy elsewhere"));
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("Use to summon Magus, if available."));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
