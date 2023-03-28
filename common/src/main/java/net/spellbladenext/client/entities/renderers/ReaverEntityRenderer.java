package net.spellbladenext.client.entities.renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.MagicSchool;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.client.entities.models.ReaverEntityModel;
import net.spellbladenext.entities.ReaverEntity;
import net.spellbladenext.items.spellbladeitems.SpellbladeItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class ReaverEntityRenderer extends ExtendedGeoEntityRenderer<ReaverEntity> {

    private static final Identifier DEFAULT_LOCATION = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade_none.png");
    private static final Identifier FIRE = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade_fire.png");
    private static final Identifier FROST = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade_cold.png");
    private static final Identifier ARCANE = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade_arcane.png");


    public ReaverEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ReaverEntityModel());
    }



    @Override
    public Identifier getTexture(ReaverEntity reaverEntity) {
        if(reaverEntity.getMainHandStack().getItem() instanceof SpellbladeItem spellbladeItem){
            if(spellbladeItem.getMagicSchools().stream().anyMatch(asdf -> MagicSchool.fromAttributeId(new Identifier(SpellPowerMod.ID,asdf.name)).equals(MagicSchool.FIRE))){
                return FIRE;
            }
            if(spellbladeItem.getMagicSchools().stream().anyMatch(asdf -> MagicSchool.fromAttributeId(new Identifier(SpellPowerMod.ID,asdf.name)).equals(MagicSchool.FROST))){
                return FROST;
            }
            if(spellbladeItem.getMagicSchools().stream().anyMatch(asdf -> MagicSchool.fromAttributeId(new Identifier(SpellPowerMod.ID,asdf.name)).equals(MagicSchool.ARCANE))){
                return ARCANE;
            }
        }
        return DEFAULT_LOCATION;
    }

    @Override
    public boolean shouldShowName(ReaverEntity reaverEntity) {
        return false;
    }

    @Override
    protected void renderLabelIfPresent(ReaverEntity reaverEntity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
    }

    @Override
    protected boolean isArmorBone(GeoBone geoBone) {
        return false;
    }

    @Nullable
    @Override
    protected Identifier getTextureForBone(String boneName, ReaverEntity reaverEntity) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, ReaverEntity reaverEntity) {
        if(boneName.equals("rightItem")) {
            return reaverEntity.getMainHandStack();
        }
        else{
            return null;
        }
    }

    @Override
    protected ModelTransformation.Mode getCameraTransformForItemAtBone(ItemStack itemStack, String s) {
        return ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String s, ReaverEntity reaverEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, ReaverEntity currentEntity, IBone bone) {

    }

    @Override
    protected void preRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, ReaverEntity currentEntity) {

    }

    @Override
    protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, ReaverEntity currentEntity, IBone bone) {

    }

    @Override
    protected void postRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, ReaverEntity currentEntity) {

    }

}