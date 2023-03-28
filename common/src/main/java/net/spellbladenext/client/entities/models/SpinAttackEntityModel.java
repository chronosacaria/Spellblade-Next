package net.spellbladenext.client.entities.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.entities.SpinAttackEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class SpinAttackEntityModel extends AnimatedGeoModel<SpinAttackEntity> {

    @Override
    public Identifier getModelResource(SpinAttackEntity spinAttackEntity) {
        if(spinAttackEntity.getCustomName() != null && spinAttackEntity.getCustomName().equals(Text.translatable("invisible"))){
            return new Identifier(SpellbladeNext.MOD_ID,"geo/arms.geo.json");
        }
        return new Identifier(SpellbladeNext.MOD_ID,"geo/arms.geo.json");
    }
    @Override
    public Identifier getTextureResource(SpinAttackEntity spinAttackEntity) {
        return new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade.png");
    }

    @Override
    public Identifier getAnimationResource(SpinAttackEntity spinAttackEntity) {
        return new Identifier(SpellbladeNext.MOD_ID,"animations/shade.animation.json");
    }
}
