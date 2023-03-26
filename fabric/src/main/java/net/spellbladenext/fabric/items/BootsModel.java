package net.spellbladenext.fabric.items;

import net.minecraft.resources.ResourceLocation;
import net.spellbladenext.SpellbladeNext;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BootsModel extends AnimatedGeoModel<Robes> {


    @Override
    public ResourceLocation getModelResource(Robes orb) {
        return new ResourceLocation(SpellbladeNext.MOD_ID,"geo/robes.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Robes orb) {

        return new ResourceLocation(SpellbladeNext.MOD_ID, "textures/item/robes.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Robes orb) {
        return null;
    }
}
