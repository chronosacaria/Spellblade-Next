package net.spellbladenext.items.armoritems;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.armor.Armor;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.items.ModArmorMaterials;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static net.spellbladenext.SpellbladeNext.*;

public class Armors {
    private static final Supplier<Ingredient> WOOL_INGREDIENTS = () -> Ingredient.ofItems(
            Items.WHITE_WOOL,
            Items.ORANGE_WOOL,
            Items.MAGENTA_WOOL,
            Items.LIGHT_BLUE_WOOL,
            Items.YELLOW_WOOL,
            Items.LIME_WOOL,
            Items.PINK_WOOL,
            Items.GRAY_WOOL,
            Items.LIGHT_GRAY_WOOL,
            Items.CYAN_WOOL,
            Items.PURPLE_WOOL,
            Items.BLUE_WOOL,
            Items.BROWN_WOOL,
            Items.GREEN_WOOL,
            Items.RED_WOOL,
            Items.BLACK_WOOL);

    public static final ArrayList<Armor.Entry> ENTRIES = new ArrayList<>();
    private static Armor.Entry create(Armor.CustomMaterial material, ItemConfig.ArmorSet defaults) {
        return new Armor.Entry(material, null, defaults);
    }



    private static final float SPECIALIZED_ROBE_SPELL_POWER = 0.25F;
    private static final float SPECIALIZED_ROBE_CRIT_DAMAGE = 0.1F;
    private static final float SPECIALIZED_ROBE_CRIT_CHANCE = 0.02F;
    private static final float SPECIALIZED_ROBE_HASTE = 0.03F;

    public static final Armor.Set RUNEGLEAMING =
            create(
                    new Armor.CustomMaterial(
                            "runegleaming",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(RUNEGLINTPLATING.get())
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), SPECIALIZED_ROBE_SPELL_POWER)
                                    )),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), SPECIALIZED_ROBE_SPELL_POWER)
                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), SPECIALIZED_ROBE_SPELL_POWER)
                                    )),
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), SPECIALIZED_ROBE_SPELL_POWER)
                                    ))
                    ))
                    .armorSet(material -> new Armor.Set(SpellbladeNext.MOD_ID,
                            new RuneblazingArmor(ModArmorMaterials.RUNEGLEAMING, EquipmentSlot.HEAD, new Item.Settings().group(EXAMPLE_TAB), MagicSchool.FIRE),
                            new RuneblazingArmor(ModArmorMaterials.RUNEGLEAMING, EquipmentSlot.CHEST, new Item.Settings().group(EXAMPLE_TAB), MagicSchool.FIRE),
                            new RuneblazingArmor(ModArmorMaterials.RUNEGLEAMING, EquipmentSlot.LEGS, new Item.Settings().group(EXAMPLE_TAB), MagicSchool.FIRE),
                            new RuneblazingArmor(ModArmorMaterials.RUNEGLEAMING, EquipmentSlot.FEET, new Item.Settings().group(EXAMPLE_TAB), MagicSchool.FIRE)
                    ), ENTRIES);

    public static final Armor.Set RUNEBLAZING =
            create(
                    new Armor.CustomMaterial(
                            "runeblazing",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(RUNEBLAZE_PLATING.get())
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), SPECIALIZED_ROBE_SPELL_POWER)
                                    )),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), SPECIALIZED_ROBE_SPELL_POWER)

                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), SPECIALIZED_ROBE_SPELL_POWER)

                                    )),
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), SPECIALIZED_ROBE_SPELL_POWER)

                                    ))
                    ))
                    .armorSet(material -> new Armor.Set(SpellbladeNext.MOD_ID,
                            new RuneblazingArmor(ModArmorMaterials.RUNEBLAZING, EquipmentSlot.HEAD, new Item.Settings().group(EXAMPLE_TAB), MagicSchool.FIRE),
                                new RuneblazingArmor(ModArmorMaterials.RUNEBLAZING, EquipmentSlot.CHEST, new Item.Settings().group(EXAMPLE_TAB), MagicSchool.FIRE),
                                new RuneblazingArmor(ModArmorMaterials.RUNEBLAZING, EquipmentSlot.LEGS, new Item.Settings().group(EXAMPLE_TAB), MagicSchool.FIRE),
                                new RuneblazingArmor(ModArmorMaterials.RUNEBLAZING, EquipmentSlot.FEET, new Item.Settings().group(EXAMPLE_TAB), MagicSchool.FIRE)
                                                ), ENTRIES);

    public static final Armor.Set RUNEFROSTED =
            create(
                    new Armor.CustomMaterial(
                            "runefrosted",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(RUNEFROSTED_INGOT.get())
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), SPECIALIZED_ROBE_SPELL_POWER)

                                    )),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), SPECIALIZED_ROBE_SPELL_POWER)

                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), SPECIALIZED_ROBE_SPELL_POWER)

                                    )),
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), SPECIALIZED_ROBE_SPELL_POWER)

                                    ))
                    ))
                    .armorSet(material -> new Armor.Set(SpellbladeNext.MOD_ID,
                            new RuneblazingArmor(ModArmorMaterials.RUNEFROSTED, EquipmentSlot.HEAD, new Item.Settings().group(EXAMPLE_TAB), MagicSchool.FIRE),
                            new RuneblazingArmor(ModArmorMaterials.RUNEFROSTED, EquipmentSlot.CHEST, new Item.Settings().group(EXAMPLE_TAB), MagicSchool.FIRE),
                            new RuneblazingArmor(ModArmorMaterials.RUNEFROSTED, EquipmentSlot.LEGS, new Item.Settings().group(EXAMPLE_TAB), MagicSchool.FIRE),
                            new RuneblazingArmor(ModArmorMaterials.RUNEFROSTED, EquipmentSlot.FEET, new Item.Settings().group(EXAMPLE_TAB), MagicSchool.FIRE)
                    ), ENTRIES);
    public static final Armor.Set AETHERFIRE =
            create(
                    new Armor.CustomMaterial(
                            "aetherfire",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(RUNEGLINTPLATING.get(), RUNEBLAZE_PLATING.get())
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.CRITICAL_DAMAGE, 0.4F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.HASTE, 0.12F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(6)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), 0.2F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), 0.2F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(4)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), 0.2F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), 0.2F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), 0.2F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), 0.2F)


                                    ))
                    ))
                    .armorSet(material -> new Armor.Set(SpellbladeNext.MOD_ID,
                            new InquisitorSet(ModArmorMaterials.AETHERFIRE, EquipmentSlot.HEAD, new Item.Settings().group(EXAMPLE_TAB), List.of(MagicSchool.FIRE,MagicSchool.ARCANE)),
                            new InquisitorSet(ModArmorMaterials.AETHERFIRE, EquipmentSlot.CHEST, new Item.Settings().group(EXAMPLE_TAB), List.of(MagicSchool.FIRE,MagicSchool.ARCANE)),
                            new InquisitorSet(ModArmorMaterials.AETHERFIRE, EquipmentSlot.LEGS, new Item.Settings().group(EXAMPLE_TAB), List.of(MagicSchool.FIRE,MagicSchool.ARCANE)),
                            new InquisitorSet(ModArmorMaterials.AETHERFIRE, EquipmentSlot.FEET, new Item.Settings().group(EXAMPLE_TAB), List.of(MagicSchool.FIRE,MagicSchool.ARCANE))
                    ), ENTRIES);
    public static final Armor.Set RIMEBLAZE =
            create(
                    new Armor.CustomMaterial(
                            "rimeblaze",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(RUNEFROSTED_INGOT.get(), RUNEBLAZE_PLATING.get())
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.CRITICAL_CHANCE, 0.08F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.HASTE, 0.12F)


                                    )),                            new ItemConfig.ArmorSet.Piece(6)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), 0.2F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), 0.2F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(4)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), 0.2F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), 0.2F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), 0.2F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), 0.2F)


                                    ))
                    ))
                    .armorSet(material -> new Armor.Set(SpellbladeNext.MOD_ID,
                            new InquisitorSet(ModArmorMaterials.RIMEBLAZE, EquipmentSlot.HEAD, new Item.Settings().group(EXAMPLE_TAB), List.of(MagicSchool.FIRE,MagicSchool.FROST)),
                            new InquisitorSet(ModArmorMaterials.RIMEBLAZE, EquipmentSlot.CHEST, new Item.Settings().group(EXAMPLE_TAB), List.of(MagicSchool.FIRE,MagicSchool.FROST)),
                            new InquisitorSet(ModArmorMaterials.RIMEBLAZE, EquipmentSlot.LEGS, new Item.Settings().group(EXAMPLE_TAB), List.of(MagicSchool.FIRE,MagicSchool.FIRE)),
                            new InquisitorSet(ModArmorMaterials.RIMEBLAZE, EquipmentSlot.FEET, new Item.Settings().group(EXAMPLE_TAB), List.of(MagicSchool.FIRE,MagicSchool.FIRE))
                    ), ENTRIES);
    public static final Armor.Set DEATHCHILL =
            create(
                    new Armor.CustomMaterial(
                            "deathchill",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(RUNEGLINTPLATING.get(), RUNEBLAZE_PLATING.get())
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.CRITICAL_CHANCE, 0.08F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.CRITICAL_DAMAGE, 0.4F)

                                    )),
                            new ItemConfig.ArmorSet.Piece(6)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), 0.2F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), 0.2F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(4)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), 0.2F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), 0.2F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), 0.2F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), 0.2F)


                                    ))
                    ))
                    .armorSet(material -> new Armor.Set(SpellbladeNext.MOD_ID,
                            new InquisitorSet(ModArmorMaterials.DEATHCHILL, EquipmentSlot.HEAD, new Item.Settings().group(EXAMPLE_TAB), List.of(MagicSchool.FROST,MagicSchool.ARCANE)),
                            new InquisitorSet(ModArmorMaterials.DEATHCHILL, EquipmentSlot.CHEST, new Item.Settings().group(EXAMPLE_TAB), List.of(MagicSchool.FROST,MagicSchool.ARCANE)),
                            new InquisitorSet(ModArmorMaterials.DEATHCHILL, EquipmentSlot.LEGS, new Item.Settings().group(EXAMPLE_TAB), List.of(MagicSchool.FROST,MagicSchool.ARCANE)),
                            new InquisitorSet(ModArmorMaterials.DEATHCHILL, EquipmentSlot.FEET, new Item.Settings().group(EXAMPLE_TAB), List.of(MagicSchool.FROST,MagicSchool.ARCANE))
                    ), ENTRIES);
    public static final Armor.Set MAGUS =
            create(
                    new Armor.CustomMaterial(
                            "magus",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            WOOL_INGREDIENTS
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), 0.125F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), 0.125F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), 0.125F)
                                    )),
                            new ItemConfig.ArmorSet.Piece(6)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), 0.125F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), 0.125F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), 0.125F)                                 )),
                            new ItemConfig.ArmorSet.Piece(6)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), 0.125F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), 0.125F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), 0.125F)                                )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FROST), 0.125F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.ARCANE), 0.125F),
                                            ItemConfig.SpellAttribute.multiply(SpellAttributes.POWER.get(MagicSchool.FIRE), 0.125F)                               ))
                    ))
                    .armorSet(material -> new Armor.Set(SpellbladeNext.MOD_ID,
                        new Robes(ModArmorMaterials.WOOL,EquipmentSlot.HEAD,new Item.Settings().group(EXAMPLE_TAB)),

                        new Robes(ModArmorMaterials.WOOL,EquipmentSlot.CHEST,new Item.Settings().group(EXAMPLE_TAB)),
                        new Robes(ModArmorMaterials.WOOL,EquipmentSlot.LEGS,new Item.Settings().group(EXAMPLE_TAB)),
                        new Robes(ModArmorMaterials.WOOL,EquipmentSlot.FEET,new Item.Settings().group(EXAMPLE_TAB))
                    ), ENTRIES);


    public static void register(Map<String, ItemConfig.ArmorSet> configs) {
        Armor.register(configs, ENTRIES);
    }
}