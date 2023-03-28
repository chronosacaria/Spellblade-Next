package net.spellbladenext.client.entities.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.entities.ColdAttackEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class ColdAttackEntityModel extends AnimatedGeoModel<ColdAttackEntity> {

    @Override
    public Identifier getModelResource(ColdAttackEntity coldAttackEntity) {
        if(coldAttackEntity.getCustomName() != null && coldAttackEntity.getCustomName().equals(Text.translatable("invisible"))){
            return new Identifier(SpellbladeNext.MOD_ID,"geo/arms.geo.json");
        }
        return new Identifier(SpellbladeNext.MOD_ID,"geo/hexblade.geo.json");
    }
    @Override
    public Identifier getTextureResource(ColdAttackEntity coldAttackEntity) {
        return new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade.png");
    }

    @Override
    public Identifier getAnimationResource(ColdAttackEntity coldAttackEntity) {
        return new Identifier(SpellbladeNext.MOD_ID,"animations/coldfury.animation.json");
    }
}
