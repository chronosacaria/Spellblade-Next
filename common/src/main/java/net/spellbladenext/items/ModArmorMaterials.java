package net.spellbladenext.items;


import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;
import net.spellbladenext.SpellbladeNext;

import java.util.function.Supplier;

import static net.spellbladenext.SpellbladeNext.WOOL_INGREDIENTS;

public enum ModArmorMaterials implements ArmorMaterial {
    RUNEBLAZING("runeblazing", 37, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0, 0, () -> {
        return Ingredient.ofItems(SpellbladeNext.RUNEBLAZE_PLATING.get());
    }),
    RUNEFROSTED("runefrosted", 37, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0, 0, () -> {
        return Ingredient.ofItems(SpellbladeNext.RUNEFROSTED_INGOT.get());
    }),
    RUNEGLEAMING("runegleaming", 37, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0, 0, () -> {
        return Ingredient.ofItems(SpellbladeNext.RUNEGLINTPLATING.get());
    }),
    AETHERFIRE("aetherfire", 37, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0, 0, () -> {
        return Ingredient.ofItems(SpellbladeNext.RUNEBLAZE_PLATING.get(),SpellbladeNext.RUNEFROSTED_INGOT.get());
    }),
    RIMEBLAZE("rimeblaze", 37, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0, 0, () -> {
        return Ingredient.ofItems(SpellbladeNext.RUNEFROSTED_INGOT.get(),SpellbladeNext.RUNEBLAZE_PLATING.get());
    }),
    DEATHCHILL("deathchill", 37, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0, 0, () -> {
        return Ingredient.ofItems(SpellbladeNext.RUNEGLINTPLATING.get(),SpellbladeNext.RUNEFROSTED_INGOT.get());
    }),
    WOOL("magus", 37, new int[]{2, 4, 6, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0, 0,
            WOOL_INGREDIENTS);







    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final Lazy<Ingredient> repairIngredient;

    private ModArmorMaterials(String name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.slotProtections = slotProtections;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new Lazy<>(repairIngredient);
    }

    @Override
    public int getDurability(EquipmentSlot slot) {
        return HEALTH_PER_SLOT[slot.getEntitySlotId()] * this.durabilityMultiplier;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return this.slotProtections[slot.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

}
