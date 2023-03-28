package net.spellbladenext.items.armoritems;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.spell_engine.api.item.ConfigurableAttributes;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.EntityAttributes_SpellPower;
import net.spellbladenext.SpellbladeNext;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.UUID;

public class Robes extends ArmorItem implements IAnimatable, DyeableItem, ConfigurableAttributes {
    public EquipmentSlot slot;
    EntityAttribute attribute;
    EntityAttribute attribute2;
    EntityAttribute attribute3;
    EntityAttributeModifier modifier;
    EntityAttributeModifier modifier2;
    EntityAttributeModifier modifier3;
    String uuid;
    private Multimap<EntityAttribute, EntityAttributeModifier> defaultModifiers;

    public Robes(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Settings settings) {
        super(armorMaterial, equipmentSlot, settings);
        this.slot = equipmentSlot;
        this.defense = armorMaterial.getDurability(equipmentSlot);
        UUID uUID = ARMOR_MODIFIER_UUID_PER_SLOT[this.slot.getEntitySlotId()];
         attribute = EntityAttributes_SpellPower.POWER.get(MagicSchool.FROST);

         modifier = new EntityAttributeModifier(uUID.toString(), 0.125, EntityAttributeModifier.Operation.MULTIPLY_BASE);
         attribute2 = EntityAttributes_SpellPower.POWER.get(MagicSchool.FIRE);

         modifier2 = new EntityAttributeModifier(uUID.toString(), 0.125, EntityAttributeModifier.Operation.MULTIPLY_BASE);
         attribute3 = EntityAttributes_SpellPower.POWER.get(MagicSchool.ARCANE);

         modifier3 = new EntityAttributeModifier(uUID.toString(), 0.125, EntityAttributeModifier.Operation.MULTIPLY_BASE);
         uuid = ARMOR_MODIFIER_UUID_PER_SLOT[slot.getEntitySlotId()].toString();
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        this.defaultModifiers = builder.build();

    }
    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{
            UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
    };

    public double defense;
    String asdfuuid = "e17f655f-46b5-4885-8be3-5846f55c8fae";

    @Override
    public EquipmentSlot getSlotType() {
        return slot;
    }
    public void setAttributes(Multimap<EntityAttribute, EntityAttributeModifier> attributes) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        // builder.putAll(super.getAttributeModifiers(this.slot));
        builder.putAll(attributes);
        this.defaultModifiers = builder.build();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        boolean broken = false;
        MagicSchool school;

        if(!world.isClient()) {
            if(entity instanceof PlayerEntity playerEntity) {

                int amount = 0;
                if(playerEntity.getInventory().getArmorStack(0).getItem() instanceof Robes){
                    amount++;
                }
                if(playerEntity.getInventory().getArmorStack(1).getItem() instanceof Robes){
                    amount++;
                }
                if(playerEntity.getInventory().getArmorStack(2).getItem() instanceof Robes){
                    amount++;
                }
                if(playerEntity.getInventory().getArmorStack(3).getItem() instanceof Robes){
                    amount++;
                }
                int ii = 0;
                if(playerEntity.hasStatusEffect(SpellbladeNext.RUNIC_BOON.get())){
                    ii = playerEntity.getStatusEffect(SpellbladeNext.RUNIC_BOON.get()).getAmplifier();
                }
                if(amount > 0 && !playerEntity.hasStatusEffect(SpellbladeNext.RUNIC_BOON.get()) && playerEntity.getAbsorptionAmount() <= 0)
                    playerEntity.addStatusEffect(new StatusEffectInstance(SpellbladeNext.RUNIC_BOON.get(),20*5, amount, false, false));

            }
        }
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == this.slot ? this.defaultModifiers : super.getAttributeModifiers(equipmentSlot);
    }

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
