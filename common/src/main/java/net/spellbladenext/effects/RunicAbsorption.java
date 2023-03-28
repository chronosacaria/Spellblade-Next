package net.spellbladenext.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class RunicAbsorption extends StatusEffect {

    public RunicAbsorption(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        //entity.setAbsorptionAmount(entity.getAbsorptionAmount() - (float)(1 * (amplifier)));
        if(entity.getAbsorptionAmount() < amplifier*4) {
            entity.setAbsorptionAmount((float) ((amplifier*4)));
        }
        super.onRemoved(entity, attributes, amplifier);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attribute, int i) {
        super.onApplied(entity, attribute, i);
    }
}
