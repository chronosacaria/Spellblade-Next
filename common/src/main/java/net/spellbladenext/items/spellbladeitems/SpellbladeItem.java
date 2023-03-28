package net.spellbladenext.items.spellbladeitems;

import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

public class SpellbladeItem extends SwordItem implements ConfigurableAttributes {
    private final ArrayList<ItemConfig.SpellAttribute> school;
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;

    public SpellbladeItem(ToolMaterial material, Multimap<EntityAttribute, EntityAttributeModifier> attributes, Settings settings, ArrayList<ItemConfig.SpellAttribute> school) {
        super(material,1, material.getAttackDamage(),  settings);
        this.school = school;


        this.setAttributes(attributes);
    }
    public ArrayList<ItemConfig.SpellAttribute> getMagicSchools(){
        return this.school;
    }



    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        for(ItemConfig.SpellAttribute school: this.getMagicSchools().stream().toList()) {
            if(attacker instanceof PlayerEntity playerEntity) {
                //System.out.println(school.name);
                MagicSchool actualSchool = MagicSchool.fromAttributeId(new Identifier(SpellPowerMod.ID, school.name));

                SpellPower.Result power = SpellPower.getSpellPower(actualSchool, (LivingEntity) attacker);
                SpellPower.Vulnerability vulnerability = SpellPower.Vulnerability.none;

                vulnerability = SpellPower.getVulnerability(target, actualSchool);

                //SpellPower.Result power = SpellPower.getSpellPower(MagicSchool.ARCANE, (LivingEntity) this.getOwner());
                double amount = 2 * power.randomValue(vulnerability) / 3;


                //particleMultiplier = power.criticalDamage() + (double)vulnerability.criticalDamageBonus();
                target.hurtTime = 0;

                target.damage(SpellDamageSource.create(actualSchool, attacker), (float) amount);
                switch (actualSchool) {
                    case FIRE -> {
                        if(SpellContainerHelper.containerFromItemStack(stack).spell_ids.contains("spellbladenext:fireoverdrive")){
                            Predicate<Entity> selectionPredicate = (target2) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, playerEntity, target)
                                    && FriendshipBracelet.PlayerFriendshipPredicate(playerEntity,target));
                            List<Entity> targets = playerEntity.getWorld().getOtherEntities(playerEntity,playerEntity.getBoundingBox().expand(6),selectionPredicate);

                            if(SpellHelper.ammoForSpell(playerEntity, SpellRegistry.getSpell(new Identifier(SpellbladeNext.MOD_ID,"fireoverdrive")),stack).satisfied()) {
                                SpellHelper.performSpell(playerEntity.world,playerEntity, new Identifier(SpellbladeNext.MOD_ID,"fireoverdrive"), targets,stack, SpellCast.Action.RELEASE, Hand.MAIN_HAND, 0);
                            }
                        }
                        if (attacker.hasStatusEffect(SpellbladeNext.FIRE_INFUSION.get())){
                            attacker.addStatusEffect(new StatusEffectInstance(SpellbladeNext.FIRE_INFUSION.get(), 100, Math.min(attacker.getStatusEffect(SpellbladeNext.FIRE_INFUSION.get()).getAmplifier()+1,2)));
                        }
                    }
                    case FROST -> {
                        if(SpellContainerHelper.containerFromItemStack(stack).spell_ids.contains("spellbladenext:frostoverdrive")){
                            Predicate<Entity> selectionPredicate = (target2) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, playerEntity, target)
                                    && FriendshipBracelet.PlayerFriendshipPredicate(playerEntity,target));
                            List<Entity> targets = playerEntity.getWorld().getOtherEntities(playerEntity,playerEntity.getBoundingBox().expand(6),selectionPredicate);
                            if(SpellHelper.ammoForSpell(playerEntity, SpellRegistry.getSpell(new Identifier(SpellbladeNext.MOD_ID,"frostoverdrive")),stack).satisfied()) {

                                SpellHelper.performSpell(playerEntity.world,playerEntity, new Identifier(SpellbladeNext.MOD_ID,"frostoverdrive"), targets,stack, SpellCast.Action.RELEASE, Hand.MAIN_HAND, 0);
                            }
                        }
                        if (attacker.hasStatusEffect(SpellbladeNext.FROST_INFUSION.get())) {
                            attacker.addStatusEffect(new StatusEffectInstance(SpellbladeNext.FROST_INFUSION.get(), 100, Math.min(attacker.getStatusEffect(SpellbladeNext.FROST_INFUSION.get()).getAmplifier()+1,2)));
                        }
                    }
                    case ARCANE -> {

                        if(SpellContainerHelper.containerFromItemStack(stack).spell_ids.contains("spellbladenext:arcaneoverdrive")){
                            Predicate<Entity> selectionPredicate = (target2) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, playerEntity, target)
                                    && FriendshipBracelet.PlayerFriendshipPredicate(playerEntity,target));
                            List<Entity> targets = playerEntity.getWorld().getOtherEntities(playerEntity,playerEntity.getBoundingBox().expand(6),selectionPredicate);

                            if(SpellHelper.ammoForSpell(playerEntity, SpellRegistry.getSpell(new Identifier(SpellbladeNext.MOD_ID,"arcaneoverdrive")),stack).satisfied()) {

                                SpellHelper.performSpell(playerEntity.world,playerEntity, new Identifier(SpellbladeNext.MOD_ID,"arcaneoverdrive"), targets,stack, SpellCast.Action.RELEASE, Hand.MAIN_HAND, 0);
                            }
                        }
                        if (attacker.hasStatusEffect(SpellbladeNext.ARCANE_INFUSION.get())){
                            attacker.addStatusEffect(new StatusEffectInstance(SpellbladeNext.ARCANE_INFUSION.get(), 100, Math.min(attacker.getStatusEffect(SpellbladeNext.ARCANE_INFUSION.get()).getAmplifier()+1,2)));
                        }
                    }
                }
            }

        }
        stack.damage(1, attacker, (e) -> e.sendToolBreakStatus(Hand.MAIN_HAND));
        return true;

    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        for(ItemConfig.SpellAttribute school: this.getMagicSchools().stream().toList()) {
            MagicSchool actualSchool = MagicSchool.fromAttributeId(new Identifier(SpellPowerMod.ID, school.name));

            if (entity instanceof LivingEntity living) {
                if (living.hasStatusEffect(SpellbladeNext.FIRE_INFUSION.get()) && actualSchool == MagicSchool.FIRE){
                    stack.getOrCreateNbt().putInt("CustomModelData", (1));

                }
                else if (living.hasStatusEffect(SpellbladeNext.ARCANE_INFUSION.get()) && actualSchool == MagicSchool.ARCANE){
                    stack.getOrCreateNbt().putInt("CustomModelData", (1));

                }
                else if (living.hasStatusEffect(SpellbladeNext.FROST_INFUSION.get()) && actualSchool == MagicSchool.FROST){
                    stack.getOrCreateNbt().putInt("CustomModelData", (1));

                }
                else{
                    stack.getOrCreateNbt().putInt("CustomModelData", (0));

                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }


    public void setAttributes(Multimap<EntityAttribute, EntityAttributeModifier> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getHardness(world, pos) != 0.0F) {
            stack.damage(2, miner, e -> e.sendToolBreakStatus(Hand.MAIN_HAND));
        }
        return true;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (this.attributes == null) {
            return super.getAttributeModifiers(slot);
        } else {
            return slot == EquipmentSlot.MAINHAND ? this.attributes : super.getAttributeModifiers(slot);
        }
    }

    /*
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(MinecraftClient.getInstance().player != null && (MinecraftClient.getInstance().player.getMainHandStack().getItem() == this || MinecraftClient.getInstance().player.getOffHandStack().getItem() == this)) {
            for (ItemConfig.SpellAttribute school : this.getMagicSchools().stream().toList()) {
                MagicSchool actualSchool = MagicSchool.fromAttributeId(new Identifier(SpellPowerMod.ID, school.name));

                SpellPower.Result power = SpellPower.getSpellPower(actualSchool, MinecraftClient.getInstance().player);
                SpellPower.Vulnerability vulnerability = SpellPower.Vulnerability.none;

                //SpellPower.Result power = SpellPower.getSpellPower(MagicSchool.ARCANE, (LivingEntity) this.getOwner());
                double amount = 2 * power.nonCriticalValue() / 3;
                double amount2 = 2 * power.forcedCriticalValue() / 3;

                Formatting chatFormatting = Formatting.GRAY;
                switch (actualSchool) {
                    case FIRE -> chatFormatting = Formatting.RED;
                    case FROST -> chatFormatting = Formatting.AQUA;
                    case ARCANE -> chatFormatting = Formatting.DARK_PURPLE;
                }
                MutableText component = Text.translatable("Adds " + amount + " to " + amount2 + " damage to attacks with this weapon.").formatted(chatFormatting);
                tooltip.add(component);
            }
        }
        else{
            tooltip.add(Text.translatable("Does additional damage on hit when equipped.").formatted(Formatting.GRAY));
        }

        super.appendTooltip(stack, world, tooltip, context);
    }
    */

}
