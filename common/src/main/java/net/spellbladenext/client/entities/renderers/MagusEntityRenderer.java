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
import net.spellbladenext.client.entities.models.MagusEntityModel;
import net.spellbladenext.entities.MagusEntity;
import net.spellbladenext.items.spellbladeitems.SpellbladeItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class MagusEntityRenderer extends ExtendedGeoEntityRenderer<MagusEntity> {

    private static final Identifier DEFAULT_LOCATION = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/magus.png");
    private static final Identifier FIRE = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/magus.png");
    private static final Identifier FROST = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/magus_frost.png");
    private static final Identifier ARCANE = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/magus_arcane.png");


    public MagusEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new MagusEntityModel());

        //this.layerRenderers.add((GeoLayerRenderer<magusEntity>) new GeoitemInHand<T,M>((IGeoRenderer<T>) this,context.getItemInHandRenderer()));
    }



    public Identifier getTextureLocation(MagusEntity magusEntity) {
        if(magusEntity.getMainHandStack().getItem() instanceof SpellbladeItem spellbladeItem){
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
    public boolean shouldShowName(MagusEntity magusEntity) {
        return false;
    }

    @Override
    protected void renderLabelIfPresent(MagusEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
    }

    @Override
    protected boolean isArmorBone(GeoBone geoBone) {
        return false;
    }

    @Nullable
    @Override
    protected Identifier getTextureForBone(String boneName, MagusEntity magusEntity) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, MagusEntity magusEntity) {
        if(boneName.equals("rightItem")) {
            return magusEntity.getMainHandStack();
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
    protected BlockState getHeldBlockForBone(String s, MagusEntity magusEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, MagusEntity currentEntity, IBone bone) {
        matrixStack.translate(0,0.1,-0.1);
    }

    @Override
    protected void preRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, MagusEntity currentEntity) {

    }

    @Override
    protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, MagusEntity currentEntity, IBone bone) {

    }

    @Override
    protected void postRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, MagusEntity currentEntity) {

    }
}