package net.spellbladenext.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.spellbladenext.SpellbladeNext;

import java.util.Random;

public class SoulFire extends StatusEffect {
    public SoulFire(StatusEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(!(entity.isFireImmune() || entity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE) || entity.blockedByShield(DamageSource.ON_FIRE))) {
            entity.setFireTicks(1);
            entity.hurtTime = 0;
        }
        else{
            entity.removeStatusEffect(SpellbladeNext.SOULFLAME.get());
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        Random random = new Random();
        return amplifier  > 4 && random.nextFloat() < 0+(amplifier-4)*0.05;
    }
}
