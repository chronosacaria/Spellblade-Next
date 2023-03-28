package net.spellbladenext.items.armoritems;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.spell_engine.api.item.ConfigurableAttributes;
import net.spell_power.api.MagicSchool;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InquisitorSet extends ArmorItem implements IAnimatable, ConfigurableAttributes {
    public final EquipmentSlot slot;
    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{
            UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
    };

    private final int defense;
    private final float toughness;
    protected final float knockbackResistance;
    protected final ArmorMaterial material;
    private Multimap<EntityAttribute, EntityAttributeModifier> defaultModifiers;
    private final List<MagicSchool> magicschool = new ArrayList<>();
    private final EquipmentSlot equipmentslot;
    public InquisitorSet(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Settings settings, List<MagicSchool> magicSchool) {
        super(armorMaterial, equipmentSlot, settings);
        //super(settings.defaultDurability(armorMaterial.getDurabilityForSlot(equipmentSlot)));
        this.material = armorMaterial;
        this.slot = equipmentSlot;
        this.defense = armorMaterial.getDurability(equipmentSlot);
        this.toughness = armorMaterial.getToughness();
        this.knockbackResistance = armorMaterial.getKnockbackResistance();
        this.magicschool.addAll(magicSchool);
        this.equipmentslot = equipmentSlot;
        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        UUID uUID = ARMOR_MODIFIER_UUID_PER_SLOT[equipmentSlot.getEntitySlotId()];
        this.defaultModifiers = builder.build();
    }

    public List<MagicSchool> getMagicschool() {
        return magicschool;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == this.slot ? this.defaultModifiers : super.getAttributeModifiers(equipmentSlot);
    }

    public void setAttributes(Multimap<EntityAttribute, EntityAttributeModifier> attributes) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        // builder.putAll(super.getAttributeModifiers(this.slot));
        System.out.println(attributes);
        builder.putAll(attributes);
        this.defaultModifiers = builder.build();
    }
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        return slot == this.slot ? this.defaultModifiers : super.getAttributeModifiers(slot);
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
