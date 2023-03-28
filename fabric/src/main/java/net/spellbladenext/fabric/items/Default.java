package net.spellbladenext.fabric.items;

import net.minecraft.resources.ResourceLocation;
import net.spellbladenext.config.ItemConfig;
import net.spellbladenext.config.LootConfig;
import net.spellbladenext.items.Orbs;
import net.spellbladenext.items.spellbladeitems.SpellbladeItems;

import java.util.List;

import static net.spellbladenext.SpellbladeNext.MOD_ID;

public class Default {
    public final static ItemConfig itemConfig;
    public final static LootConfig lootConfig;

    static {
        itemConfig = new ItemConfig();
        for (var weapon: SpellbladeItems.entries) {
            itemConfig.weapons.put(weapon.name(), weapon.defaults());
        }
        for (var weapon: SpellbladeItems.orbs) {
            itemConfig.weapons.put(weapon.name(), weapon.defaults());
        }
        for (var weapon: SpellbladeItems.claymores) {
            itemConfig.weapons.put(weapon.name(), weapon.defaults());
        }
        for (var weapon: SpellbladeItems.runedaggers) {
            itemConfig.weapons.put(weapon.name(), weapon.defaults());
        }


        lootConfig = new LootConfig();
        lootConfig.item_groups.put("spellblades", new LootConfig.ItemGroup(List.of(
                SpellbladeItems.fireBlade.id().toString(),
                SpellbladeItems.frostBlade.id().toString(),
                SpellbladeItems.arcaneBlade.id().toString()),
                1
        ).chance(0.2F));
        lootConfig.item_groups.put("foci", new LootConfig.ItemGroup(List.of(
                Orbs.arcaneOrb.id().toString(),
                Orbs.fireOrb.id().toString(),
                Orbs.frostOrb.id().toString()),
                1
        ).chance(0.2F));
        lootConfig.item_groups.put("claymores", new LootConfig.ItemGroup(List.of(
                SpellbladeItems.arcaneClaymore.id().toString(),
                SpellbladeItems.fireClaymore.id().toString(),
                SpellbladeItems.frostClaymore.id().toString()),
                1
        ).chance(0.2F));
        lootConfig.item_groups.put("runedaggers", new LootConfig.ItemGroup(List.of(
                SpellbladeItems.runedagger.id().toString()),
                1
        ).chance(0.1F));
        lootConfig.item_groups.put("runic_armor", new LootConfig.ItemGroup(List.of(
                new ResourceLocation(MOD_ID, "runeblazing_head").toString(),
                new ResourceLocation(MOD_ID, "runeblazing_chest").toString(),
                new ResourceLocation(MOD_ID, "runeblazing_legs").toString(),
                new ResourceLocation(MOD_ID, "runeblazing_feet").toString(),
                 new ResourceLocation(MOD_ID, "runefrosted_head").toString(),
                new ResourceLocation(MOD_ID, "runefrosted_chest").toString(),
                new ResourceLocation(MOD_ID, "runefrosted_legs").toString(),
                new ResourceLocation(MOD_ID, "runefrosted_feet").toString(),
                new ResourceLocation(MOD_ID, "runegleaming_head").toString(),
                new ResourceLocation(MOD_ID, "runegleaming_chest").toString(),
                new ResourceLocation(MOD_ID, "runegleaming_legs").toString(),
                new ResourceLocation(MOD_ID, "runegleaming_feet").toString()),
                1
        ).chance(0.4F));

        List.of("minecraft:chests/abandoned_mineshaft",
                        "minecraft:chests/igloo_chest",
                        "minecraft:chests/ruined_portal",
                        "minecraft:chests/shipwreck_supply",
                        "minecraft:chests/jungle_temple")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of("spellblades")));

        List.of("minecraft:chests/desert_pyramid",
                        "minecraft:chests/bastion_bridge",
                        "minecraft:chests/jungle_temple",
                        "minecraft:chests/pillager_outpost",
                        "minecraft:chests/simple_dungeon",
                        "minecraft:chests/stronghold_crossing")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of("spellblades","claymores","foci","runic_armor")));

        List.of("minecraft:chests/bastion_other",
                        "minecraft:chests/nether_bridge",
                        "minecraft:chests/underwater_ruin_small")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of("spellblades")));

        List.of("minecraft:chests/shipwreck_treasure")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of("runic_armor")));

        List.of("minecraft:chests/bastion_treasure",
                        "minecraft:chests/stronghold_library",
                        "minecraft:chests/underwater_ruin_big",
                        "minecraft:chests/woodland_mansion")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of("spellblades","claymores","foci", "runic_armor","runedaggers")));

        List.of("minecraft:chests/end_city_treasure",
                        "minecraft:chests/ancient_city",
                        "minecraft:chests/stronghold_library")
                .forEach(id -> lootConfig.loot_tables.put(id, List.of("spellblades","claymores","foci", "runic_armor","runedaggers")));

    }
}