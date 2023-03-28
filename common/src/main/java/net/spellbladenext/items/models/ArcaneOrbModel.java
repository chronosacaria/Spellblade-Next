package net.spellbladenext.items.models;

import net.minecraft.util.Identifier;
import net.spell_power.api.MagicSchool;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.items.Orb;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ArcaneOrbModel extends AnimatedGeoModel<Orb>{

    @Override
    public Identifier getModelResource(Orb orb) {
        return new Identifier(SpellbladeNext.MOD_ID,"geo/orb.geo.json");
    }

    @Override
    public Identifier getTextureResource(Orb orb) {
        if(orb.getMagicSchool() == MagicSchool.FIRE) {
            return new Identifier(SpellbladeNext.MOD_ID, "textures/item/orb_fire.png");
        }
        if(orb.getMagicSchool() == MagicSchool.FROST) {
            return new Identifier(SpellbladeNext.MOD_ID, "textures/item/orb_frost.png");
        }

            return new Identifier(SpellbladeNext.MOD_ID, "textures/item/orb_arcane.png");
    }

    @Override
    public Identifier getAnimationResource(Orb orb) {
        return new Identifier(SpellbladeNext.MOD_ID,"animations/orb.animations.json");
    }
}
