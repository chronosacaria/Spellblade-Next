package net.spellbladenext.items.armoritems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.spell_power.api.MagicSchool;

public class RuneblazingArmor extends RunicArmor  {

    public RuneblazingArmor(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Item.Settings settings, MagicSchool school) {
        super(armorMaterial, equipmentSlot, settings, school);
    }

    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public ItemStack getDefaultStack() {
        return super.getDefaultStack();
    }


}
