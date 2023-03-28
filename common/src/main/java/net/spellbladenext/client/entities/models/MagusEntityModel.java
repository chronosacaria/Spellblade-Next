package net.spellbladenext.client.entities.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.entities.MagusEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class MagusEntityModel extends AnimatedGeoModel<MagusEntity> implements ModelWithArms {

    @Override
    public Identifier getModelResource(MagusEntity magusEntity) {
        return new Identifier(SpellbladeNext.MOD_ID,"geo/magus.geo.json");
    }
    @Override
    public Identifier getTextureResource(MagusEntity magusEntity) {
        return new Identifier(SpellbladeNext.MOD_ID,"textures/mob/magus.png");
    }

    @Override
    public Identifier getAnimationResource(MagusEntity magusEntity) {
        return new Identifier(SpellbladeNext.MOD_ID,"animations/magus.animation.json");
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        this.rotate(matrices);
    }

    public void rotate(MatrixStack arg) {
        arg.translate(1, 0 / 16.0F, 0 / 16.0F);
        arg.scale(2, 2, 2);
    }
}
