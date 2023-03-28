package net.spellbladenext.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.SoundHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;

import java.util.List;
import java.util.function.Predicate;

public class RunicBoon extends StatusEffect {
    public RunicBoon(StatusEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        Predicate<Entity> selectionPredicate = (target2) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, entity, target2));
        List<Entity> targets = entity.getWorld().getOtherEntities(entity, entity.getBoundingBox().expand(6),selectionPredicate);
        if(!entity.getWorld().isClient()) {
            if (entity instanceof PlayerEntity playerEntity ) {
                Spell spell = SpellRegistry.getSpell(new Identifier(SpellbladeNext.MOD_ID, "fireoverdrive"));
                ;
                ParticleHelper.sendBatches(entity, spell.release.particles);
                SoundHelper.playSound(playerEntity.getWorld(), playerEntity, spell.release.sound);

                for(Entity target : targets){
                     SpellHelper.performImpacts(entity.getWorld(), entity, target, spell, new SpellHelper.ImpactContext(1.0F,1.0F,null, SpellPower.getSpellPower(MagicSchool.FIRE,entity), TargetHelper.TargetingMode.AREA));
                }
            }
            if (entity instanceof PlayerEntity playerEntity ) {
                Spell spell = SpellRegistry.getSpell(new Identifier(SpellbladeNext.MOD_ID, "frostoverdrive"));
                ParticleHelper.sendBatches(entity, spell.release.particles);
                SoundHelper.playSound(playerEntity.getWorld(), playerEntity, spell.release.sound);

                for(Entity target : targets){
                    SpellHelper.performImpacts(entity.getWorld(), entity, target, spell, new SpellHelper.ImpactContext(1.0F,1.0F,null, SpellPower.getSpellPower(MagicSchool.FROST,entity), TargetHelper.TargetingMode.AREA));
                }            }
            if (entity instanceof PlayerEntity playerEntity ) {
                Spell spell = SpellRegistry.getSpell(new Identifier(SpellbladeNext.MOD_ID, "arcaneoverdrive"));
                ParticleHelper.sendBatches(entity, spell.release.particles);
                SoundHelper.playSound(playerEntity.getWorld(), playerEntity, spell.release.sound);

                for(Entity target : targets){
                    SpellHelper.performImpacts(entity.getWorld(), entity, target, spell, new SpellHelper.ImpactContext(1.0F,1.0F,null, SpellPower.getSpellPower(MagicSchool.ARCANE,entity), TargetHelper.TargetingMode.AREA));
                }
            }
        }

    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % Math.max(8,(23-amplifier*3)) == 0;
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        //entity.setAbsorptionAmount(entity.getAbsorptionAmount() - (float)(1 * (amplifier)));
        if(entity.getAbsorptionAmount() < amplifier*5) {
            entity.setAbsorptionAmount((float) ((amplifier*5)));
        }
        super.onRemoved(entity, attributes, amplifier);
    }

    @Override
    public StatusEffect addAttributeModifier(EntityAttribute attribute, String uuid, double amount, EntityAttributeModifier.Operation operation) {
        return super.addAttributeModifier(attribute, uuid, amount, operation);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attribute, int i) {

        super.onApplied(entity, attribute, i);
    }
}
