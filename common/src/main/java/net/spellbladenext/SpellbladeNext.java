package net.spellbladenext;

import com.google.common.base.Suppliers;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.spell_engine.api.render.CustomModels;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.spellbladenext.effects.*;
import net.spellbladenext.entities.*;

import java.util.List;
import java.util.function.Supplier;

public class SpellbladeNext {
    public static final String MOD_ID = "spellbladenext";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
    // Registering a new creative tab
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_KEY);

    public static final RegistrySupplier<Item> SPELLBLADE_DUMMY = ITEMS.register("spellblade", () ->
            new Item(new Item.Settings()));
    public static final ItemGroup EXAMPLE_TAB = CreativeTabRegistry.create(new Identifier(MOD_ID, "example_tab"), () ->
            new ItemStack(SPELLBLADE_DUMMY.get()));

    public static final EntityType<AmethystEntity> AMETHYST;

    public static final EntityType<AmethystEntity2> AMETHYST2;
    public static final Supplier<Ingredient> WOOL_INGREDIENTS = () -> Ingredient.ofItems(
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
    public static final EntityType<CleansingFlameEntity> CLEANSING_FLAME_ENTITY_ENTITY_TYPE;
    public static final EntityType<Eruption> ERUPTION_ENTITY_TYPE;

    public static final EntityType<FlameWindsEntity> FLAME_WINDS_ENTITY_ENTITY_TYPE;
    public static final EntityType<EndersGazeEntity> ENDERS_GAZE_ENTITY_ENTITY_TYPE;

    public static final EntityType<EndersGaze> ENDERS_GAZE_ENTITY_TYPE;

    public static final EntityType<MagmaOrbEntity> MAGMA_ORB_ENTITY_ENTITY_TYPE;
    public static final EntityType<IceThorn> ICE_THORN_ENTITY_TYPE;
    public static final EntityType<ExplosionDummy> EXPLOSION_DUMMY_ENTITY_TYPE;
    public static final EntityType<ExplosionDummy> REAL_EXPLOSION_DUMMY_ENTITY_TYPE;


    public static final EntityType<IcicleBarrierEntity> ICICLE_BARRIER_ENTITY_ENTITY_TYPE;
    public static final DeferredRegister<StatusEffect> MOB_EFFECTS_REGISTRY = DeferredRegister.create(MOD_ID, Registry.MOB_EFFECT_KEY);
    public static final RegistrySupplier<StatusEffect> FIRE_INFUSION = MOB_EFFECTS_REGISTRY.register("fireinfusion", () -> new Infusion(StatusEffectCategory.BENEFICIAL, 0x990000).addAttributeModifier(
            SpellAttributes.POWER.get(MagicSchool.FIRE).attribute, "aef7de5e-2333-401b-8f19-d83258eab800", 2, EntityAttributeModifier.Operation.ADDITION));
    public static final RegistrySupplier<StatusEffect> ARCANE_INFUSION = MOB_EFFECTS_REGISTRY.register("arcaneinfusion", () -> new Infusion(StatusEffectCategory.BENEFICIAL, 0x990000).addAttributeModifier(
            SpellAttributes.POWER.get(MagicSchool.ARCANE).attribute, "ace1b04e-4844-4780-9ff1-32d7f02d4d62", 2, EntityAttributeModifier.Operation.ADDITION));
    public static final RegistrySupplier<StatusEffect> FROST_INFUSION = MOB_EFFECTS_REGISTRY.register("frostinfusion", () -> new Infusion(StatusEffectCategory.BENEFICIAL, 0x990000).addAttributeModifier(
            SpellAttributes.POWER.get(MagicSchool.FROST).attribute, "22b2d3fe-ce03-46b7-9114-6740bd338c7e", 2, EntityAttributeModifier.Operation.ADDITION));
    public static final RegistrySupplier<StatusEffect> FIRE_OVERDRIVE = MOB_EFFECTS_REGISTRY.register("fireoverdrive", () ->  new Overdrive(StatusEffectCategory.BENEFICIAL,MagicSchool.FIRE, 0xFF5A4F).addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED,"363f757c-33be-4254-a647-4d30100a4bfd",0.2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistrySupplier<StatusEffect> ARCANE_OVERDRIVE = MOB_EFFECTS_REGISTRY.register("arcaneoverdrive", () ->  new Overdrive(StatusEffectCategory.BENEFICIAL,MagicSchool.ARCANE, 0x64329F).addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED,"4abf6ee7-591f-4ff5-b36a-babf2ffe23ca",0.2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistrySupplier<StatusEffect> FROST_OVERDRIVE = MOB_EFFECTS_REGISTRY.register("frostoverdrive", () ->
            new Overdrive(StatusEffectCategory.BENEFICIAL,MagicSchool.FROST, 0x60939F).addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED,"8422cbc0-a935-4992-b187-60d69dd02cba",0.2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistrySupplier<StatusEffect> SOULFLAME = MOB_EFFECTS_REGISTRY.register("soulflame", () ->  new SoulFire(StatusEffectCategory.BENEFICIAL, 0xFF5A4F).addAttributeModifier(SpellAttributes.POWER.get(MagicSchool.FIRE).attribute,"6b64d185-2b88-46c9-833e-5d1c33804eec",1, EntityAttributeModifier.Operation.ADDITION));
    public static final RegistrySupplier<StatusEffect> DOUSED = MOB_EFFECTS_REGISTRY.register("doused", () -> new Infusion(StatusEffectCategory.HARMFUL, 0x990000).addAttributeModifier(
            SpellAttributes.POWER.get(MagicSchool.FIRE).attribute, "de7cee4d-954a-44a0-8b85-9b72fa475336", -0.95, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistrySupplier<StatusEffect> INERT = MOB_EFFECTS_REGISTRY.register("inert", () -> new Infusion(StatusEffectCategory.HARMFUL, 0x990000).addAttributeModifier(
            SpellAttributes.POWER.get(MagicSchool.ARCANE).attribute, "7882eb98-c23c-4d4f-a13f-e14993ca12f6", -0.95, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistrySupplier<StatusEffect> MELTED = MOB_EFFECTS_REGISTRY.register("melted", () -> new Infusion(StatusEffectCategory.HARMFUL, 0x990000).addAttributeModifier(
            SpellAttributes.POWER.get(MagicSchool.FROST).attribute, "c4337ce6-57ca-44cf-9eec-a765a179529d", -0.95, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));

    @ExpectPlatform
    public static <T extends Entity> Supplier<EntityType<T>> registerEntityType(
            String name,
            Supplier<EntityType<T>> entityType
    ) {
        throw new AssertionError();
    }
    public static final RegistrySupplier<StatusEffect> RUNIC_ABSORPTION = MOB_EFFECTS_REGISTRY.register("runic_absorption", () -> new RunicAbsorption(StatusEffectCategory.BENEFICIAL, 0x994000));
    public static final RegistrySupplier<StatusEffect> RUNIC_BOON = MOB_EFFECTS_REGISTRY.register("runic_boon", () -> new RunicBoon(StatusEffectCategory.BENEFICIAL, 0x994000));

    public static final RegistrySupplier<Item> RUNEBLAZE_PLATING = ITEMS.register("runeblazing_ingot", () ->
            new Item(new Item.Settings().group(EXAMPLE_TAB)));
    public static final RegistrySupplier<Item> RUNEGLINTPLATING = ITEMS.register("runegleaming_ingot", () ->
            new Item(new Item.Settings().group(EXAMPLE_TAB)));
    public static final RegistrySupplier<Item> RUNEFROSTED_INGOT = ITEMS.register("runefrosted_ingot", () ->
            new Item(new Item.Settings().group(EXAMPLE_TAB)));
    public static final RegistrySupplier<Item> FIRE_TOTEM = ITEMS.register("nullifying_fire_totem", () ->
            new Item(new Item.Settings().group(EXAMPLE_TAB)));
    public static final RegistrySupplier<Item> FROST_TOTEM = ITEMS.register("nullifying_frost_totem", () ->
            new Item(new Item.Settings().group(EXAMPLE_TAB)));
    public static final RegistrySupplier<Item> ARCANE_TOTEM = ITEMS.register("nullifying_arcane_totem", () ->
            new Item(new Item.Settings().group(EXAMPLE_TAB)));

    public static final RegistrySupplier<Item> ICICLE = ITEMS.register("icicle", () ->
            new Item(new Item.Settings()));
    public static final RegistrySupplier<Item> ICICLE_2 = ITEMS.register("icicle2", () ->
            new Item(new Item.Settings()));
    public static final RegistrySupplier<Item> FROSTBLADE = ITEMS.register("frostblade", () ->
            new Item(new Item.Settings()));

    public static final Item EXPLOSION = new Item(new Item.Settings());
    public static final Item REAL_EXPLOSION = new Item(new Item.Settings());

   // public static static FriendshipBracelet FRIENDSHIP_BRACELET = new FriendshipBracelet(new Item.Properties().tab(EXAMPLE_TAB));

    public static final StatusEffect CLEANSING_FLAME = new CleansingFlame(StatusEffectCategory.BENEFICIAL, 0x990000);
    public static final ClampedEntityAttribute HEX = new ClampedEntityAttribute("hex", 0, 0, 9999);

    public static void init() {
        ITEMS.register();
        CustomModels.registerModelIds(List.of(new Identifier(MOD_ID,"projectile/magic_missile")));

        Registry.register(Registry.STATUS_EFFECT,new Identifier(MOD_ID,"cleansing_fire"), CLEANSING_FLAME);
        MOB_EFFECTS_REGISTRY.register();
        //System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());

        Registry.register(Registry.ATTRIBUTE, new Identifier(MOD_ID,"hex"), HEX);

        Registry.register(Registry.ITEM, new Identifier(SpellbladeNext.MOD_ID, "explosion"),
                EXPLOSION);
        Registry.register(Registry.ITEM, new Identifier(SpellbladeNext.MOD_ID, "realexplosion"),
                REAL_EXPLOSION);

    }
}
