package net.spellbladenext.items.spellbladeitems;

import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.spell_engine.api.item.ConfigurableAttributes;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellContainerHelper;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.spell_power.api.attributes.EntityAttributes_SpellPower;

import java.util.ArrayList;
import java.util.List;

import static net.spell_power.api.SpellPower.getCriticalMultiplier;
import static net.spellbladenext.SpellbladeNext.MOD_ID;
import static net.spellbladenext.fabric.ExampleModFabric.DIREHEX;

public class RuneDaggerItem extends SwordItem implements ConfigurableAttributes {
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;

    public RuneDaggerItem(ToolMaterial toolMaterial, Settings settings, ArrayList<ItemConfig.SpellAttribute> school) {
        super(toolMaterial,1, toolMaterial.getAttackDamage(),  settings);

        this.setAttributes(attributes);
    }
    public List<MagicSchool> getMagicSchools(ItemStack stack){

        List<MagicSchool> list = new ArrayList<>();
        if(SpellContainerHelper.containerFromItemStack(stack) != null && SpellContainerHelper.containerFromItemStack(stack).spell_ids.contains("spellbladenext:gleamingblade")){
            list.add(MagicSchool.ARCANE);
        }
        if(SpellContainerHelper.containerFromItemStack(stack) != null && SpellContainerHelper.containerFromItemStack(stack).spell_ids.contains("spellbladenext:frozenblade")){
            list.add(MagicSchool.FROST);
        }
        if(SpellContainerHelper.containerFromItemStack(stack) != null && SpellContainerHelper.containerFromItemStack(stack).spell_ids.contains("spellbladenext:searingblade")){
            list.add(MagicSchool.FIRE);
        }
        if(SpellContainerHelper.containerFromItemStack(stack) != null && SpellContainerHelper.containerFromItemStack(stack).spell_ids.contains("spellbladenext:steelblade")){
            list.add(MagicSchool.PHYSICAL_MELEE);
        }
        return list;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        double angle = target.getEyePos().subtract(attacker.getEyePos()).dotProduct(target.getRotationVector());
        boolean sneak = angle > 0 || attacker.hasStatusEffect(ExampleModFabric.DIREHEX.get());


        for(MagicSchool school : this.getMagicSchools(stack)){
            SpellPower.Result result = SpellPower.getSpellPower(school,attacker);
            if(sneak){
                EntityAttribute entityAttribute = (EntityAttribute)EntityAttributes_SpellPower.POWER.get(school);
                if (school.isExternalAttribute()) {
                    entityAttribute = (EntityAttribute) Registry.ATTRIBUTE.get(school.attributeId());
                }
                result = new SpellPower.Result(school, attacker.getAttributeValue(entityAttribute), 1, getCriticalMultiplier(attacker, stack));
            }
            if(school == MagicSchool.ARCANE){

                Spell spell = SpellRegistry.getSpell(new Identifier(MOD_ID,"gleamingblade"));
                SpellHelper.performImpacts(attacker.getWorld(),attacker,target, spell,new SpellHelper.ImpactContext(1.0F,1.0F,target.position(), result,SpellHelper.impactTargetingMode(spell)));
            }
            if(school == MagicSchool.FIRE){
                Spell spell = SpellRegistry.getSpell(new Identifier(MOD_ID,"searingblade"));
                SpellHelper.performImpacts(attacker.getWorld(),attacker,target, spell,new SpellHelper.ImpactContext(1.0F,1.0F,target.position(), result,SpellHelper.impactTargetingMode(spell)));
            }
            if(school == MagicSchool.FROST){
                Spell spell = SpellRegistry.getSpell(new Identifier(MOD_ID,"frozenblade"));
                SpellHelper.performImpacts(attacker.getWorld(),attacker,target, spell,new SpellHelper.ImpactContext(1.0F,1.0F,target.position(),result ,SpellHelper.impactTargetingMode(spell)));
            }
            if(school == MagicSchool.PHYSICAL_MELEE){
                Spell spell = SpellRegistry.getSpell(new Identifier(MOD_ID,"steelblade"));
                SpellHelper.performImpacts(attacker.getWorld(),attacker,target, spell,new SpellHelper.ImpactContext(1.0F,1.0F,target.position(),result ,SpellHelper.impactTargetingMode(spell)));

            }
        }
        if(attacker.hasStatusEffect(ExampleModFabric.DIREHEX.get())){
            attacker.removeStatusEffect(ExampleModFabric.DIREHEX.get());
        }
        stack.damage(1, attacker, (e) -> {
            e.sendToolBreakStatus(Hand.MAIN_HAND);
        });
        return true;
    }
    
    public static void lookAt(LivingEntity looker, Entity entity, float f, float g) {
        double d = entity.getX() - looker.getX();
        double e = entity.getZ() - looker.getZ();
        double h;
        if (entity instanceof LivingEntity livingEntity) {
            h = livingEntity.getEyeY() - looker.getEyeY();
        } else {
            h = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0D - looker.getEyeY();
        }

        double i = Math.sqrt(d * d + e * e);
        float j = (float)(MathHelper.atan2(e, d) * 57.2957763671875D) - 90.0F;
        float k = (float)(-(MathHelper.atan2(h, i) * 57.2957763671875D));
        looker.setPitch(rotlerp(looker.getPitch(), k, g));
        looker.setYaw(rotlerp(looker.getYaw(), j, f));
        looker.setBodyYaw(rotlerp(looker.getPitch(), k, g));
        looker.setHeadYaw(rotlerp(looker.getPitch(), k, g));

    }
    private static float rotlerp(float f, float g, float h) {
        float i = MathHelper.wrapDegrees(g - f);
        if (i > h) {
            i = h;
        }

        if (i < -h) {
            i = -h;
        }

        return f + i;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(!user.getItemCooldownManager().isCoolingDown(this)) {
            if(entity instanceof MobEntity mob){
                mob.setTarget(null);
            }
            user.addStatusEffect(new StatusEffectInstance(ExampleModFabric.DIREHEX.get(),20,0,false,false));
            Vec3d vec3d = entity.getPos()
                    .add(user.getRotationVec(1F)
                            .subtract(
                                    0,
                                    user.getRotationVec(1F).y,
                                    0)
                            .normalize()
                            .multiply(entity.getBoundingBox().getXLength() / 2)
                    )
                    .add(user.getRotationVec(1F)
                            .subtract(0, user.getRotationVec(1F).y, 0)
                            .normalize()
                            .multiply(1.5)
                    );

            float f = user.getPitch();
            float f1 = user.getYaw() + 180;
            float f2 = user.prevPitch;
            float f3 = user.prevYaw + 180;
            user.setPitch(f);
            user.setYaw(f1);
            user.prevPitch = f2;
            user.prevYaw = f3;
            user.velocityModified = true;

            user.requestTeleport(vec3d.x, vec3d.y, vec3d.z);


            if (user.getWorld().isClient) {

                KeyBinding.onKeyPressed(MinecraftClient.getInstance().options.attackKey.getDefaultKey());
            }
            user.getItemCooldownManager().getCooldownProgress(this, 160);
            return ActionResult.PASS;
        }
        else{
            return ActionResult.FAIL;
        }
    }

    @Override
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
}
