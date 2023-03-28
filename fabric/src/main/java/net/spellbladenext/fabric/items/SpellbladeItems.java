package net.spellbladenext.fabric.items;

import net.minecraft.world.item.Item;
import net.spellbladenext.items.Orbs;
import net.spellbladenext.items.armoritems.Armors;
import net.spellbladenext.items.armoritems.RunicArmor;

import java.util.HashMap;

import static net.spellbladenext.SpellbladeNext.MOD_ID;

public class SpellbladeItems {
    public static final HashMap<String, Item> entries;
    static {
        entries = new HashMap<>();
        for (var weaponEntry : net.spellbladenext.items.spellbladeitems.SpellbladeItems.entries) {
            entries.put(weaponEntry.id().toString(), weaponEntry.item());
        }
        for (var weaponEntry : Orbs.orbs) {
            entries.put(weaponEntry.id().toString(), weaponEntry.item());
        }
        for (var weaponEntry : net.spellbladenext.items.spellbladeitems.SpellbladeItems.claymores) {
            entries.put(weaponEntry.id().toString(), weaponEntry.item());
        }
        for (var weaponEntry : net.spellbladenext.items.spellbladeitems.SpellbladeItems.runedaggers) {
            entries.put(weaponEntry.id().toString(), weaponEntry.item());
        }
        for (var entry : Armors.ENTRIES) {
            if (entry.armorSet().head instanceof RunicArmor) {
                entries.put(MOD_ID+":"+entry.armorSet().head.toString(),
                        entry.armorSet().head);
                entries.put(MOD_ID+":"+entry.armorSet().chest.toString(),
                        entry.armorSet().chest);
                entries.put(MOD_ID+":"+entry.armorSet().legs.toString(),
                        entry.armorSet().legs);
                entries.put(MOD_ID+":"+entry.armorSet().feet.toString(),
                        entry.armorSet().feet);
            }

        }
        System.out.println("asdf " + entries);
    }
}
