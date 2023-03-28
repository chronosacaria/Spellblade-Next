package net.spellbladenext.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.MathHelper;

public class DireHex extends StatusEffect {
    protected DireHex(StatusEffectCategory statusEffectCategory, int i) {
        super(statusEffectCategory, i);
    }

    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        super.applyUpdateEffect(livingEntity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
    public void lookAtEntity(LivingEntity living, Entity entity, float f, float g) {
        double d = entity.getX() - living.getX();
        double e = entity.getZ() - living.getZ();
        double h;
        if (entity instanceof LivingEntity livingEntity) {
            h = livingEntity.getEyeY() - living.getEyeY();
        } else {
            h = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0D - living.getEyeY();
        }

        double i = Math.sqrt(d * d + e * e);
        float j = (float)(MathHelper.atan2(e, d) * 57.2957763671875D) - 90.0F;
        float k = (float)(-(MathHelper.atan2(h, i) * 57.2957763671875D));
        living.setYaw(j+180);
        living.prevYaw =j+180;
        living.bodyYaw =j+180;
        living.headYaw =j+180;


    }
}
