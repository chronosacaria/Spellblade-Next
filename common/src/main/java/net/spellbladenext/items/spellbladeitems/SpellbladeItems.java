package net.spellbladenext.items.spellbladeitems;

import com.google.common.collect.ImmutableMultimap;
import dev.architectury.platform.Platform;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.spell_engine.api.item.ConfigurableAttributes;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.weapon.StaffItem;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.spellbladenext.SpellbladeNext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;


public class SpellbladeItems {

    public static final class Entry {
        private final String namespace;
        private final String name;
        private final Material material;
        private final ItemConfig.Weapon defaults;
        private final Item item;
        private @Nullable String requiredMod;


        public Entry(String namespace, String name, Material material, Item item, ItemConfig.Weapon defaults, @Nullable String requiredMod) {
            this.namespace = namespace;
            this.name = name;
            this.material = material;
            this.defaults = defaults;
            this.requiredMod = requiredMod;
            this.item = item;
        }
        public Item item(){
            return item;
        }
        public Identifier id() {
            return new Identifier(SpellbladeNext.MOD_ID, name);
        }

        public Entry attribute(ItemConfig.SpellAttribute attribute) {
            defaults.add(attribute);
            return this;
        }

        public Entry requires(String modName) {
            this.requiredMod = modName;
            return this;
        }

        public boolean isRequiredModInstalled() {
            if (requiredMod == null || requiredMod.isEmpty()) {
                return true;
            }
            return Platform.isModLoaded(requiredMod);
        }

        public String name() {
            return name;
        }

        public Material material() {
            return material;
        }


        public ItemConfig.Weapon defaults() {
            return defaults;
        }

        public @Nullable String requiredMod() {
            return requiredMod;
        }
    }

    public static final ArrayList<Entry> entries = new ArrayList<>();
    private static Entry entry(String requiredMod, String name, Material material, ItemConfig.Weapon defaults) {

        Item spellblade = new SpellbladeItem(material,attributesFrom(defaults),new Item.Settings().group(SpellbladeNext.EXAMPLE_TAB).maxDamage(650),
                defaults.spell_attributes);
        var entry = new Entry(SpellbladeNext.MOD_ID, name, material, spellblade, defaults, null);
        if (entry.isRequiredModInstalled()) {
            entries.add(entry);
        }
        return entry;
    }
    public static final ArrayList<Entry> runedaggers = new ArrayList<>();

    private static Entry runedaggers(String requiredMod, String name, Material material, ItemConfig.Weapon defaults) {

        Item spellblade = new RuneDaggerItem(material, new Item.Settings().group(SpellbladeNext.EXAMPLE_TAB).maxDamage(650),
                defaults.spell_attributes);
        var entry = new Entry(SpellbladeNext.MOD_ID, name, material, spellblade, defaults, null);
        if (entry.isRequiredModInstalled()) {
            runedaggers.add(entry);
        }
        return entry;
    }
    public static final ArrayList<Entry> orbs = new ArrayList<>();

    private static Entry orbs(String requiredMod, String name, Material material, ItemConfig.Weapon defaults) {
        Item orb = new StaffItem(material,new Item.Settings().group(SpellbladeNext.EXAMPLE_TAB).maxDamage(650));
        var entry = new Entry(SpellbladeNext.MOD_ID, name, material,orb, defaults, null);
        if (entry.isRequiredModInstalled()) {
            orbs.add(entry);
        }
        return entry;
    }
    public static final ArrayList<Entry> claymores = new ArrayList<>();

    private static Entry claymores(String requiredMod, String name, Material material, ItemConfig.Weapon defaults) {
        Item claymore = new ClaymoreItems(material, (int) defaults.attack_damage, defaults.attack_speed, new Item.Settings().group(SpellbladeNext.EXAMPLE_TAB).maxDamage(800),
                defaults.spell_attributes);
        var entry = new Entry(SpellbladeNext.MOD_ID, name, material, claymore, defaults, null);
        if (entry.isRequiredModInstalled()) {
            claymores.add(entry);
        }
        return entry;
    }

    // MARK: Material

    public static class Material implements ToolMaterial {
        public static Material matching(ToolMaterial vanillaMaterial, Item repairIngredient) {
            var material = new Material();
            material.miningLevel = vanillaMaterial.getMiningLevel();
            material.durability = vanillaMaterial.getDurability();
            material.miningSpeed = vanillaMaterial.getMiningSpeedMultiplier();
            material.enchantability = vanillaMaterial.getEnchantability();
            material.ingredient = repairIngredient;
            return material;
        }

        private int miningLevel = 0;
        private int durability = 0;
        private float miningSpeed = 0;
        private int enchantability = 0;
        private Item ingredient = null;

        @Override
        public int getDurability() {
            return durability;
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return miningSpeed;
        }

        @Override
        public float getAttackDamage() {
            return 0;
        }

        @Override
        public int getMiningLevel() {
            return miningLevel;
        }

        @Override
        public int getEnchantability() {
            return enchantability;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(ingredient);
        }
    }

    // MARK: Wands

    private static final float wandAttackDamage = 2;
    private static final float wandAttackSpeed = -2.4F;


 /*   public static final Entry noviceWand = wand("wand_novice",
            Material.matching(Tiers.WOOD, () -> Ingredient.of(Items.STICK)))
            .add(ItemConfig.SpellAttribute.bonus(Attributes.POWER.get(MagicSchool.FIRE), 1));
    public static final Entry arcaneWand = wand("wand_arcane",
            Material.matching(Tiers.IRON, () -> Ingredient.of(Items.GOLD_INGOT)))
            .add(ItemConfig.SpellAttribute.bonus(Attributes.POWER.get(MagicSchool.ARCANE), 2));
    public static final Entry fireWand = wand("wand_fire",
            Material.matching(Tiers.IRON, () -> Ingredient.of(Items.GOLD_INGOT)))
            .add(ItemConfig.SpellAttribute.bonus(Attributes.POWER.get(MagicSchool.FIRE), 2));
    public static final Entry frostWand = wand("wand_frost",
            Material.matching(Tiers.IRON, () -> Ingredient.of(Items.IRON_INGOT)))
            .add(ItemConfig.SpellAttribute.bonus(Attributes.POWER.get(MagicSchool.FROST), 2));
*/
    // MARK: Staves

    private static final float staffAttackDamage = 2;
    private static final float staffAttackSpeed = -2.4F;
    private static Entry entry(String name, Material material, ItemConfig.Weapon defaults) {
        return entry(null, name, material, defaults);
    }
    private static Entry orb(String name, Material material, ItemConfig.Weapon defaults) {
        return orbs(null, name, material, defaults);
    }
    private static Entry claymore(String name, Material material, ItemConfig.Weapon defaults) {
        return claymores(null, name, material, defaults);
    }
    private static Entry sword(String name, Material material) {
        var settings = new Item.Settings().group(SpellbladeNext.EXAMPLE_TAB);
        //var item = new StaffItem(material, settings);
        return entry(name, material, new ItemConfig.Weapon(wandAttackDamage, wandAttackSpeed));
    }
    private static Entry runedagger(String name, Material material) {
        var settings = new Item.Settings().group(SpellbladeNext.EXAMPLE_TAB);
        //var item = new StaffItem(material, settings);
        return runedaggers(null, name,material, new ItemConfig.Weapon(3, -2));
    }
    private static Entry orb(String name, Material material) {
        var settings = new Item.Settings().group(SpellbladeNext.EXAMPLE_TAB);
        //var item = new StaffItem(material, settings);
        return orb(name, material, new ItemConfig.Weapon(wandAttackDamage, wandAttackSpeed));
    }
    private static Entry claymore(String name, Material material) {
        var settings = new Item.Settings().group(SpellbladeNext.EXAMPLE_TAB);
        //var item = new StaffItem(material, settings);
        return claymore(name, material, new ItemConfig.Weapon(3, -3F));
    }

    public static final Entry arcaneBlade = sword("blade_arcane",
            Material.matching(ToolMaterials.GOLD, Items.GOLD_INGOT))
            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.ARCANE), 2));
    public static final Entry fireBlade = sword("blade_fire",
            Material.matching(ToolMaterials.GOLD, Items.COPPER_INGOT))
            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.FIRE), 2));
    public static final Entry frostBlade = sword("blade_frost",
            Material.matching(ToolMaterials.IRON, Items.IRON_INGOT))
            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.FROST), 2));
    public static final Entry runedagger = runedagger("rune_dagger",
            Material.matching(ToolMaterials.IRON, Items.IRON_INGOT)).attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.ARCANE), 4)).attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.FROST), 4)).attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.FIRE), 4));
    public static final Entry arcaneClaymore = claymore("claymore_arcane",
            Material.matching(ToolMaterials.GOLD, Items.GOLD_INGOT))
            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.ARCANE), 3));
    public static final Entry fireClaymore = claymore("claymore_fire",
            Material.matching(ToolMaterials.GOLD, Items.COPPER_INGOT))
            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.FIRE), 3));
    public static final Entry frostClaymore = claymore("claymore_frost",
            Material.matching(ToolMaterials.IRON, Items.IRON_INGOT))
            .attribute(ItemConfig.SpellAttribute.bonus(SpellAttributes.POWER.get(MagicSchool.FROST), 3));/*
    public static final StaffItem spellblade = new StaffItem(Tiers.DIAMOND, new Item.Settings());
    public static final StaffItem dummyfrost = new StaffItem(Tiers.DIAMOND, new Item.Settings());
    public static final StaffItem dummyfrost2 = new StaffItem(Tiers.DIAMOND, new Item.Settings());
    public static final StaffItem dummyfrost3 = new StaffItem(Tiers.DIAMOND, new Item.Settings());*/

    // MARK: Register

    public static void register(Map<String, ItemConfig.Weapon> configs) {
        for(var entry: entries) {
            var config = configs.get(entry.name);
            if (config == null) {
                config = entry.defaults;
                configs.put(entry.name(), config);
            };

            //entry.defaults().spell_attributes.removeIf(asdf -> Arrays.stream(MagicSchool.values()).noneMatch(asdf2 -> asdf2.toString().toLowerCase().equals(asdf.name)));
            var settings = new Item.Settings().group(SpellbladeNext.EXAMPLE_TAB);
            var item = entry.item;

            ((ConfigurableAttributes)item).setAttributes(attributesFrom(config));
            Registry.register(Registry.ITEM, entry.id(), item);
        }
        for(var entry: claymores) {
            var config = configs.get(entry.name);
            if (config == null) {
                config = entry.defaults;
                configs.put(entry.name(), config);
            };

            //entry.defaults().spell_attributes.removeIf(asdf -> Arrays.stream(MagicSchool.values()).noneMatch(asdf2 -> asdf2.toString().toLowerCase().equals(asdf.name)));
            var settings = new Item.Settings().group(SpellbladeNext.EXAMPLE_TAB);
            var item = entry.item();
            ((ConfigurableAttributes)item).setAttributes(attributesFrom(config));
            Registry.register(Registry.ITEM, entry.id(), item);
        }
        for(var entry: runedaggers) {
            var config = configs.get(entry.name);
            if (config == null) {
                config = entry.defaults;
                configs.put(entry.name(), config);
            };

            //entry.defaults().spell_attributes.removeIf(asdf -> Arrays.stream(MagicSchool.values()).noneMatch(asdf2 -> asdf2.toString().toLowerCase().equals(asdf.name)));
            var settings = new Item.Settings().group(SpellbladeNext.EXAMPLE_TAB);
            var item = entry.item;

            ((ConfigurableAttributes)item).setAttributes(attributesFrom(config));
            Registry.register(Registry.ITEM, entry.id(), item);
        }
    }

    private static ImmutableMultimap<EntityAttribute, EntityAttributeModifier> attributesFrom(ItemConfig.Weapon config) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                new EntityAttributeModifier(
                        ItemAccessor.ATTACK_DAMAGE_MODIFIER_ID(),
                        "Weapon modifier",
                        config.attack_damage,
                        EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED,
                new EntityAttributeModifier(
                        ItemAccessor.ATTACK_SPEED_MODIFIER_ID(),
                        "Weapon modifier",
                        config.attack_speed,
                        EntityAttributeModifier.Operation.ADDITION));
        for(var attribute: config.spell_attributes) {
            try {
                var entityAttribute = SpellAttributes.all.get(attribute.name).attribute;
                builder.put(entityAttribute,
                        new EntityAttributeModifier(
                                entityAttribute.weaponUUID,
                                "Weapon modifier",
                                attribute.value,
                                attribute.operation));
            } catch (Exception e) {
                System.err.println("Failed to add item attribute modifier: " + e.getMessage());
            }
        }
        return builder.build();
    }
    private static ImmutableMultimap<EntityAttribute, EntityAttributeModifier> attributesFromDagger(ItemConfig.Weapon config) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                new EntityAttributeModifier(
                        ItemAccessor.ATTACK_DAMAGE_MODIFIER_ID(),
                        "Weapon modifier",
                        3,
                        EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED,
                new EntityAttributeModifier(
                        ItemAccessor.ATTACK_SPEED_MODIFIER_ID(),
                        "Weapon modifier",
                        -2,
                        EntityAttributeModifier.Operation.ADDITION));
        for(var attribute: config.spell_attributes) {
            try {
                var entityAttribute = SpellAttributes.all.get(attribute.name).attribute;
                builder.put(entityAttribute,
                        new EntityAttributeModifier(
                                entityAttribute.weaponUUID,
                                "Weapon modifier",
                                attribute.value,
                                attribute.operation));
            } catch (Exception e) {
                System.err.println("Failed to add item attribute modifier: " + e.getMessage());
            }
        }
        return builder.build();
    }
    private static ImmutableMultimap<EntityAttribute, EntityAttributeModifier> claymoreAttributes(ItemConfig.Weapon config) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                new EntityAttributeModifier(
                        ItemAccessor.ATTACK_DAMAGE_MODIFIER_ID(),
                        "Weapon modifier",
                        3,
                        EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED,
                new EntityAttributeModifier(
                        ItemAccessor.ATTACK_SPEED_MODIFIER_ID(),
                        "Weapon modifier",
                        -3,
                        EntityAttributeModifier.Operation.ADDITION));
        for(var attribute: config.spell_attributes) {
            try {
                var entityAttribute = SpellAttributes.all.get(attribute.name).attribute;
                builder.put(entityAttribute,
                        new EntityAttributeModifier(
                                entityAttribute.weaponUUID,
                                "Weapon modifier",
                                attribute.value,
                                attribute.operation));
            } catch (Exception e) {
                System.err.println("Failed to add item attribute modifier: " + e.getMessage());
            }
        }
        return builder.build();
    }
    static abstract class ItemAccessor extends Item {
        public ItemAccessor(Item.Settings settings) { super(settings); }
        public static UUID ATTACK_DAMAGE_MODIFIER_ID() { return ATTACK_DAMAGE_MODIFIER_ID; }
        public static UUID ATTACK_SPEED_MODIFIER_ID() { return ATTACK_SPEED_MODIFIER_ID; }
    }
}
