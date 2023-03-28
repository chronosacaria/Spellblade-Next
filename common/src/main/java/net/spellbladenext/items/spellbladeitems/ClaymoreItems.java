package net.spellbladenext.items.spellbladeitems;

import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.ConfigurableAttributes;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.internals.SpellCast;
import net.spell_engine.internals.SpellContainerHelper;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.items.FriendshipBracelet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ClaymoreItems extends SwordItem implements ConfigurableAttributes {
    private final ArrayList<ItemConfig.SpellAttribute> school;
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;

    public ClaymoreItems(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings, ArrayList<ItemConfig.SpellAttribute> school) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.school = school;
        this.setAttributes(attributes);
    }

    public ArrayList<ItemConfig.SpellAttribute> getMagicSchools(){
        return this.school;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        for(ItemConfig.SpellAttribute school: this.getMagicSchools().stream().toList()) {
            if(attacker instanceof PlayerEntity player) {
                //System.out.println(school.name);
                MagicSchool actualSchool = MagicSchool.fromAttributeId(new Identifier(SpellPowerMod.ID, school.name));

                SpellPower.Result power = SpellPower.getSpellPower(actualSchool, attacker);
                SpellPower.Vulnerability vulnerability;

                vulnerability = SpellPower.getVulnerability(target, actualSchool);

                //SpellPower.Result power = SpellPower.getSpellPower(MagicSchool.ARCANE, (LivingEntity) this.getOwner());
                double amount = 3 * power.randomValue(vulnerability) / 3;


                //particleMultiplier = power.criticalDamage() + (double)vulnerability.criticalDamageBonus();
                target.hurtTime = 0;

                target.damage(SpellDamageSource.create(actualSchool, attacker), (float) amount);
                switch (actualSchool) {
                    case FIRE -> {
                        if(SpellContainerHelper.containerFromItemStack(stack).spell_ids.contains("spellbladenext:fireoverdrive")){
                            Predicate<Entity> selectionPredicate = (target2) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, player, target)
                                    && FriendshipBracelet.PlayerFriendshipPredicate(player,target));
                            List<Entity> targets = player.getWorld().getOtherEntities(player,player.getBoundingBox().expand(6),selectionPredicate);

                            if(SpellHelper.ammoForSpell(player, SpellRegistry.getSpell(new Identifier(SpellbladeNext.MOD_ID,"fireoverdrive")),stack).satisfied()) {
                                SpellHelper.performSpell(player.world,player, new Identifier(SpellbladeNext.MOD_ID,"fireoverdrive"), targets,stack, SpellCast.Action.RELEASE, Hand.MAIN_HAND, 0);
                            }
                        }
                        if (attacker.hasStatusEffect(SpellbladeNext.FIRE_INFUSION.get())){
                            attacker.addStatusEffect(new StatusEffectInstance(SpellbladeNext.FIRE_INFUSION.get(), 100, Math.min(attacker.getStatusEffect(SpellbladeNext.FIRE_INFUSION.get()).getAmplifier()+1,2)));
                        }
                    }
                    case FROST -> {
                        if(SpellContainerHelper.containerFromItemStack(stack).spell_ids.contains("spellbladenext:frostoverdrive")){
                            Predicate<Entity> selectionPredicate = (target2) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, player, target)
                                    && FriendshipBracelet.PlayerFriendshipPredicate(player,target));
                            List<Entity> targets = player.getWorld().getOtherEntities(player,player.getBoundingBox().expand(6),selectionPredicate);
                            if(SpellHelper.ammoForSpell(player, SpellRegistry.getSpell(new Identifier(SpellbladeNext.MOD_ID,"frostoverdrive")),stack).satisfied()) {

                                SpellHelper.performSpell(player.world,player, new Identifier(SpellbladeNext.MOD_ID,"frostoverdrive"), targets,stack, SpellCast.Action.RELEASE, Hand.MAIN_HAND, 0);
                            }
                        }
                        if (attacker.hasStatusEffect(SpellbladeNext.FROST_INFUSION.get())) {
                            attacker.addStatusEffect(new StatusEffectInstance(SpellbladeNext.FROST_INFUSION.get(), 100, Math.min(attacker.getStatusEffect(SpellbladeNext.FROST_INFUSION.get()).getAmplifier()+1,2)));
                        }
                    }
                    case ARCANE -> {

                        if(SpellContainerHelper.containerFromItemStack(stack).spell_ids.contains("spellbladenext:arcaneoverdrive")){
                            Predicate<Entity> selectionPredicate = (target2) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, player, target)
                                    && FriendshipBracelet.PlayerFriendshipPredicate(player,target));
                            List<Entity> targets = player.getWorld().getOtherEntities(player,player.getBoundingBox().expand(6),selectionPredicate);

                            if(SpellHelper.ammoForSpell(player, SpellRegistry.getSpell(new Identifier(SpellbladeNext.MOD_ID,"arcaneoverdrive")),stack).satisfied()) {

                                SpellHelper.performSpell(player.world,player, new Identifier(SpellbladeNext.MOD_ID,"arcaneoverdrive"), targets,stack, SpellCast.Action.RELEASE, Hand.MAIN_HAND, 0);
                            }
                        }
                        if (attacker.hasStatusEffect(SpellbladeNext.ARCANE_INFUSION.get())){
                            attacker.addStatusEffect(new StatusEffectInstance(SpellbladeNext.ARCANE_INFUSION.get(), 100, Math.min(attacker.getStatusEffect(SpellbladeNext.ARCANE_INFUSION.get()).getAmplifier()+1,2)));
                        }
                    }
                }
            }

        }
        stack.damage(1, attacker, (e) -> {
            e.sendToolBreakStatus(Hand.MAIN_HAND);
        });
        return true;
    }

    @Override
    public void setAttributes(Multimap<EntityAttribute, EntityAttributeModifier> attributes) {
        this.attributes = attributes;

    }
}
