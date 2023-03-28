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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.spellbladenext.entities.IceThornSpellProjectile;
import net.spellbladenext.client.entities.models.AmethystEntityModel;

@Environment(EnvType.CLIENT)
public class AmethystEntityRenderer<T extends Entity & FlyingItemEntity> extends EntityRenderer<T> {
    public static final Identifier TEXTURE  = new Identifier("spellbladenext", "textures/entity/sword1.png");
    public static final Identifier TEXTURE2  = new Identifier("spellbladenext", "textures/entity/sword2.png");
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean fullBrightness;
    private final AmethystEntityModel model;
    public AmethystEntityRenderer(EntityRendererFactory.Context context, float scale, boolean fullBrightness) {
        super(context);
        this.model = new AmethystEntityModel<>(context.getPart(AmethystEntityModel.LAYER_LOCATION));
        this.itemRenderer = context.getItemRenderer();
        this.scale = scale;
        this.fullBrightness = fullBrightness;
    }

    @Override
    protected int getBlockLight(T entity, BlockPos blockPos) {
        return 15;
    }
    public AmethystEntityRenderer(EntityRendererFactory.Context context) {
        this(context, 1.0F, false);
    }

    public void render(T entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        if (!entity.isInvisible()) {
            // Is EntityRendererDispatcher#getSquaredDistanceToCamera(entity) the same as EntityRendererDispatcher.camera.getFocusedEntity().squaredDistanceTo(entity)?
            if (entity.age >= 2 || !(this.dispatcher.getSquaredDistanceToCamera(entity) < 12.25D)) {
                matrixStack.push();
                double y = entity.getYaw();
                double x = entity.getPitch();
                if(!(entity instanceof IceThornSpellProjectile)){
                    y = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw());
                    x = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());
                }
                matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((float) (y - 90.0F)));
                matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float) (x - 45.0F)));
                this.itemRenderer.renderItem(entity.getStack(), ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, entity.getId());
                matrixStack.pop();
            }
        }
    }

    @Override
    public Identifier getTexture(T entity) {
        return TEXTURE;
    }
}
