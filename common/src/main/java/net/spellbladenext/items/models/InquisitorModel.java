package net.spellbladenext.items.models;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.spell_power.api.MagicSchool;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.items.armoritems.InquisitorSet;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class InquisitorModel extends AnimatedGeoModel<InquisitorSet> {

    @Override
    public Identifier getModelResource(InquisitorSet orb) {
        if(orb.slot == EquipmentSlot.HEAD){
            if(orb.getMagicschool().contains(MagicSchool.ARCANE)){
                if(orb.getMagicschool().contains(MagicSchool.FIRE)){
                    return new Identifier(SpellbladeNext.MOD_ID,"geo/magebane.geo.json");

                }
                if(orb.getMagicschool().contains(MagicSchool.FROST)){
                    return new Identifier(SpellbladeNext.MOD_ID,"geo/mageseeker.geo.json");

                }
            }
            return new Identifier(SpellbladeNext.MOD_ID,"geo/magebreaker.geo.json");

        }
        return new Identifier(SpellbladeNext.MOD_ID,"geo/inquisitor.geo.json");
    }

    @Override
    public Identifier getTextureResource(InquisitorSet orb) {
        if(orb.slot == EquipmentSlot.HEAD){
            if(orb.getMagicschool().contains(MagicSchool.ARCANE)){
                if(orb.getMagicschool().contains(MagicSchool.FIRE)){
                    return new Identifier(SpellbladeNext.MOD_ID,"textures/armor/magebane_crown.png");

                }
                if(orb.getMagicschool().contains(MagicSchool.FROST)){
                    return new Identifier(SpellbladeNext.MOD_ID,"textures/armor/mageseeker_hat.png");

                }
            }
            return new Identifier(SpellbladeNext.MOD_ID,"textures/armor/magebreaker_helmet.png");

        }
        if(orb.getMagicschool().contains(MagicSchool.ARCANE)){
            if(orb.getMagicschool().contains(MagicSchool.FIRE)){
                return new Identifier(SpellbladeNext.MOD_ID,"textures/armor/aetherfire.png");

            }
            if(orb.getMagicschool().contains(MagicSchool.FROST)){
                return new Identifier(SpellbladeNext.MOD_ID,"textures/armor/deathchill.png");

            }
        }
        return new Identifier(SpellbladeNext.MOD_ID,"textures/armor/rimeblaze.png");
    }

    @Override
    public Identifier getAnimationResource(InquisitorSet orb) {
        return null;
    }
}
