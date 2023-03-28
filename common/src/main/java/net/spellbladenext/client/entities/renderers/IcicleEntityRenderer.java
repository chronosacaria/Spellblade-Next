package net.spellbladenext.client.entities.renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.client.entities.models.AmethystEntityModel;
import net.spellbladenext.client.entities.models.IcicleEntityModel;

@Environment(EnvType.CLIENT)
public class IcicleEntityRenderer<T extends Entity & FlyingItemEntity> extends EntityRenderer<T> {
    public static final Identifier TEXTURE  = new Identifier("spellbladenext", "textures/entity/sword1.png");
    public static final Identifier TEXTURE2  = new Identifier("spellbladenext", "textures/entity/sword2.png");
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean fullBrightness;
    private final IcicleEntityModel model;
    public IcicleEntityRenderer(EntityRendererFactory.Context context, float scale, boolean fullBrightness) {
        super(context);
        this.model = new IcicleEntityModel<>(context.getPart(AmethystEntityModel.LAYER_LOCATION));
        this.itemRenderer = context.getItemRenderer();
        this.scale = scale;
        this.fullBrightness = fullBrightness;
    }
    public IcicleEntityRenderer(EntityRendererFactory.Context context) {
        this(context, 1.0F, false);
    }

    public void render(T entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        if (!entity.isInvisible()) {
            if (entity.age >= 2) {
                matrixStack.push();
                matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion( entity.getYaw() - 90.0F));
                matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion( entity.getPitch() - 45.0F));
                this.itemRenderer.renderItem(new ItemStack(SpellbladeNext.ICICLE_2.get()), ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, entity.getId());
                matrixStack.pop();
            }
        }
    }

    @Override
    public Identifier getTexture(T entity) {
        return TEXTURE;
    }
}
