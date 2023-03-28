package net.spellbladenext;

import net.minecraft.util.Identifier;
import net.spell_engine.api.render.CustomModels;

import java.util.List;
public class ClientMod {
    public static void initialize() {

        CustomModels.registerModelIds(List.of(
                new Identifier(SpellbladeNext.MOD_ID, "cleansingflame")
        ));

    }
}