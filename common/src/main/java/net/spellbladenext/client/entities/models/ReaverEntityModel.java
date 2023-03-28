package net.spellbladenext.client.entities.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.entities.ReaverEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class ReaverEntityModel extends AnimatedGeoModel<ReaverEntity> {

    @Override
    public Identifier getModelResource(ReaverEntity reaverEntity) {
        return new Identifier(SpellbladeNext.MOD_ID,"geo/hexblade.geo.json");
    }
    @Override
    public Identifier getTextureResource(ReaverEntity reaverEntity) {
        return new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade.png");
    }

    @Override
    public Identifier getAnimationResource(ReaverEntity reaverEntity) {
        return new Identifier(SpellbladeNext.MOD_ID,"animations/hexblade.animation.json");
    }
}
