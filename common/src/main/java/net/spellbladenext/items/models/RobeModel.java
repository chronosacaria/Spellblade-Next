package net.spellbladenext.items.models;

import net.minecraft.util.Identifier;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.items.armoritems.Robes;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RobeModel extends AnimatedGeoModel<Robes> {

    @Override
    public Identifier getModelResource(Robes orb) {

        return new Identifier(SpellbladeNext.MOD_ID,"geo/robes.geo.json");
    }

    @Override
    public Identifier getTextureResource(Robes orb) {
        return new Identifier(SpellbladeNext.MOD_ID, "textures/armor/robestexture_default.png");
    }

    @Override
    public Identifier getAnimationResource(Robes orb) {
        return null;
    }
}
