package net.spellbladenext.effects;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.spell_power.api.MagicSchool;

public class Overdrive extends StatusEffect {

    private final MagicSchool school;

    public Overdrive(StatusEffectCategory mobEffectCategory, MagicSchool school, int i) {
        super(mobEffectCategory, i);
        this.school = school;
    }

    public MagicSchool getSchool() {
        return school;
    }

    @Override
    public StatusEffect addAttributeModifier(EntityAttribute attribute, String uuid, double amount, EntityAttributeModifier.Operation operation) {
        return super.addAttributeModifier(attribute, uuid, amount, operation);
    }
}
