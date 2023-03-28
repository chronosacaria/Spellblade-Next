package net.spellbladenext.items.models;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.items.armoritems.Robes;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RobeItemModel extends AnimatedGeoModel<Robes> {


    @Override
    public Identifier getModelResource(Robes orb) {
        if(orb.slot == EquipmentSlot.CHEST){
            return new Identifier(SpellbladeNext.MOD_ID,"geo/robeitem.geo.json");

        }
        if(orb.slot == EquipmentSlot.FEET){
            return new Identifier(SpellbladeNext.MOD_ID,"geo/bootsitem.geo.json");

        }
        if(orb.slot == EquipmentSlot.LEGS){
            return new Identifier(SpellbladeNext.MOD_ID,"geo/pantsitem.geo.json");

        }
        return new Identifier(SpellbladeNext.MOD_ID,"geo/hooditem.json");
    }

    @Override
    public Identifier getTextureResource(Robes orb) {

        return new Identifier(SpellbladeNext.MOD_ID, "textures/item/robe.png");
    }

    @Override
    public Identifier getAnimationResource(Robes orb) {
        return null;
    }
}
