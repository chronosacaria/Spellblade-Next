package net.spellbladenext.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.World;
import net.spellbladenext.fabric.ExampleModFabric;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Offering extends Item{
    public Offering(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World level, Entity entity, int i, boolean bl) {
        if(entity instanceof PlayerEntity playerEntity){
            if(player.hasEffect(ExampleModFabric.HEX.get())){
                player.removeEffect(ExampleModFabric.HEX.get());
            }
        }
        super.inventoryTick(itemStack, level, entity, i, bl);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(World level, PlayerEntity playerEntity, InteractionHand interactionHand) {
        if(player.hasEffect(ExampleModFabric.HEX.get())){
            player.removeEffect(ExampleModFabric.HEX.get());
            return InteractionResultHolder.consume(player.getItemInHand(interactionHand));

        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("Use or keep in inventory to ward away Hex."));

        super.appendHoverText(itemStack, level, list, tooltipFlag);
    }
}
