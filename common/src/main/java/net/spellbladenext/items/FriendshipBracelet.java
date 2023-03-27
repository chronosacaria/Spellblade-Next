package net.spellbladenext.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FriendshipBracelet extends Item {
    public FriendshipBracelet(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        if (stack.hasNbt()) {
            if (stack.getOrCreateNbt().getInt("Friendship") == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT) {
            stack.getOrCreateNbt();
            NbtCompound nbtCompound;
            if (stack.hasNbt() && stack.getNbt() != null) {
                if (stack.getNbt().get("Friendship") != null) {
                    nbtCompound = stack.getNbt();
                    nbtCompound.remove("Friendship");
                    return true;
                } else {
                    nbtCompound = stack.getOrCreateNbt();
                    nbtCompound.putInt("Friendship", 1);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        NbtCompound nbtCompound;
        if (itemStack.hasNbt() && itemStack.getNbt() != null) {
            if (itemStack.getNbt().get("Friendship") != null) {
                nbtCompound = itemStack.getNbt();
                nbtCompound.remove("Friendship");
            } else {
                nbtCompound = itemStack.getOrCreateNbt();
                nbtCompound.putInt("Friendship", 1);
            }
        } else {
            nbtCompound = itemStack.getOrCreateNbt();
            nbtCompound.putInt("Friendship", 1);
        }
        return TypedActionResult.success(itemStack);
    }

    public static boolean PlayerFriendshipPredicate(PlayerEntity playerEntity, Entity livingEntity) {
        boolean flag1 = false;
        boolean flag2 = false;
        for (int i = 0; i <= playerEntity.getInventory().size(); i++) {
            if (playerEntity.getInventory().getStack(i).getItem() instanceof FriendshipBracelet) {
                if (playerEntity.getInventory().getStack(i).getNbt() != null) {
                    if (playerEntity.getInventory().getStack(i).getOrCreateNbt().get("Friendship") != null) {
                        flag1 = true;
                    }
                }
            }
        }
        if (livingEntity instanceof Angerable angerableMob) {
            if (angerableMob.shouldAngerAt(playerEntity)) {
                flag2 = true;
            }
        }
        return !(flag1 && (livingEntity.getType().getSpawnGroup().isPeaceful() || livingEntity instanceof PlayerEntity || (livingEntity instanceof Angerable && !flag2)));

    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Text text = Text.translatable("tooltip.spellbladenext.enabled").formatted(Formatting.GRAY);
        Text text2 = Text.translatable("tooltip.spellbladenext.activation_prompt").formatted(Formatting.GRAY);

        if (stack.hasNbt()) {
            if (stack.getOrCreateNbt().get("Friendship") != null) {
                tooltip.add(text);
            }
            else{
                tooltip.add(text2);
            }
        }
        else{
            tooltip.add(text2);

        }
        
        super.appendTooltip(stack, world, tooltip, context);
    }
}
