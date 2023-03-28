package net.spellbladenext.fabric.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.spellbladenext.fabric.ExampleModFabric;

import static net.spellbladenext.fabric.ExampleModFabric.SINCELASTHEX;

public class DebugNetherPortal extends Item {
    public DebugNetherPortal(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(World p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if(p_41433_.isShiftKeyDown()){
            p_41433_.awardStat(SINCELASTHEX,1);
            System.out.println(p_41433_.getWorld().dimension().equals(World.OVERWORLD) && p_41433_.getWorld().canSeeSky(p_41433_.getOnPos().above().above()));
            if(p_41433_ instanceof ServerPlayer) {
                System.out.println(0.01 * Math.pow((1.02930223664), ((ServerPlayer) p_41433_).getStats().getValue(Stats.CUSTOM.get(SINCELASTHEX))));
            }

        }
        else {
            if(!p_41432_.isClientSide()) {
                p_41433_.addEffect(new MobEffectInstance(ExampleModFabric.HEX.get(),20*60*3,0));
            }
        }

        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack itemStack, ItemStack itemStack2, Slot slot, ClickAction clickAction, PlayerEntity playerEntity, SlotAccess slotAccess) {
        return super.overrideOtherStackedOnMe(itemStack, itemStack2, slot, clickAction, player, slotAccess);
    }
}
