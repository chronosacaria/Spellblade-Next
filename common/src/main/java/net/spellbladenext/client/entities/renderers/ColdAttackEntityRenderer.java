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
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.client.entities.models.ColdAttackEntityModel;
import net.spellbladenext.entities.ColdAttackEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class ColdAttackEntityRenderer extends ExtendedGeoEntityRenderer<ColdAttackEntity> {

    private static final Identifier DEFAULT_LOCATION = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/phantasm.png");
    private static final Identifier FIRE = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade_fire.png");
    private static final Identifier FROST = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade_cold.png");
    private static final Identifier ARCANE = new Identifier(SpellbladeNext.MOD_ID,"textures/mob/hexblade_arcane.png");


    public ColdAttackEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ColdAttackEntityModel());
    }

    public Identifier getTexture(ColdAttackEntity coldAttackEntity) {
        return DEFAULT_LOCATION;
    }

    @Override
    public boolean shouldShowName(ColdAttackEntity animatable) {
        return false;
    }

    @Override
    protected void renderLabelIfPresent(ColdAttackEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
    }

    @Override
    protected boolean isArmorBone(GeoBone geoBone) {
        return false;
    }

    @Nullable
    @Override
    protected Identifier getTextureForBone(String boneName, ColdAttackEntity coldAttackEntity) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, ColdAttackEntity coldAttackEntity) {
        if(boneName.equals("rightItem") ) {
            return new ItemStack(SpellbladeNext.FROSTBLADE.get());
        }
        else{
            return null;
        }
    }

    @Override
    protected ModelTransformation.Mode getCameraTransformForItemAtBone(ItemStack itemStack, String boneName) {
            return ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND;


    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String s, ColdAttackEntity coldAttackEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, ColdAttackEntity currentEntity, IBone bone) {
        matrixStack.translate(0,0.1,-0.1);
    }

    @Override
    protected void preRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, ColdAttackEntity currentEntity) {

    }

    @Override
    protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, ColdAttackEntity currentEntity, IBone bone) {

    }

    @Override
    protected void postRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, ColdAttackEntity currentEntity) {

    }
}