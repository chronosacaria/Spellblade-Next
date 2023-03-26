package net.spellbladenext.fabric.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.spellbladenext.SpellbladeNext;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PantsModel extends AnimatedGeoModel<Robes> {

    @Override
    public ResourceLocation getModelResource(Robes orb) {

        return new ResourceLocation(SpellbladeNext.MOD_ID,"geo/pantsitem.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Robes orb) {
        return new ResourceLocation(SpellbladeNext.MOD_ID, "textures/armor/robestexture_default.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Robes orb) {
        return null;
    }
}