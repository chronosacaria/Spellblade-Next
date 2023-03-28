package net.spellbladenext.client.entities.renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.entities.SpinAttackEntity;
import net.spellbladenext.client.entities.models.SpinAttackEntityModel;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class SpinAttackEntityRenderer extends ExtendedGeoEntityRenderer<SpinAttackEntity> {

    private static final Identifier DEFAULT_LOCATION = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade_none.png");
    private static final Identifier FIRE = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade_fire.png");
    private static final Identifier FROST = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade_cold.png");
    private static final Identifier ARCANE = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade_arcane.png");


    public SpinAttackEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SpinAttackEntityModel());

        //this.layerRenderers.add((GeoLayerRenderer<Reaver>) new GeoitemInHand<T,M>((IGeoRenderer<T>) this,context.getItemInHandRenderer()));
    }

    @Override
    public Identifier getTexture(SpinAttackEntity spinAttackEntity) {
        return DEFAULT_LOCATION;
    }

    @Override
    protected boolean isArmorBone(GeoBone geoBone) {
        return false;
    }

    @Nullable
    @Override
    protected Identifier getTextureForBone(String boneName, SpinAttackEntity spinAttackEntity) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, SpinAttackEntity spinAttackEntity) {
        if(boneName.equals("rightItem") || boneName.equals("leftItem")) {
            return new ItemStack(SpellbladeNext.SPELLBLADE_DUMMY.get());
        }
        else{
            return null;
        }
    }

    @Override
    protected ModelTransformation.Mode getCameraTransformForItemAtBone(ItemStack itemStack, String boneName) {
        if(boneName.equals("rightItem")) {
            return ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND;
        } else if(boneName.equals("leftItem")) {
            return ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND;
        } else {
            return ModelTransformation.Mode.FIXED;
        }
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String s, SpinAttackEntity spinAttackEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, SpinAttackEntity currentEntity, IBone bone) {
        matrixStack.translate(0,0.1,-0.1);
    }

    @Override
    protected void preRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, SpinAttackEntity currentEntity) {

    }

    @Override
    protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, SpinAttackEntity currentEntity, IBone bone) {

    }

    @Override
    protected void postRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, SpinAttackEntity currentEntity) {

    }
}