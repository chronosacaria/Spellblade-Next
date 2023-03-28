package net.spellbladenext.client.entities.models;

// Made with Blockbench 4.2.5
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class IcicleEntityModel<T extends Entity & FlyingItemEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(new Identifier("spellbladenext", "dummyfrostmodel"), "main");
    private final ModelPart bone;
    private final ModelPart root;

    public IcicleEntityModel(ModelPart root) {
            super(RenderLayer::getEntitySolid);
        this.root = root;
        this.bone = root.getChild("bone");
    }

    public static TexturedModelData createBodyLayer() {
        ModelData modelData = new ModelData();
        ModelPartData partData = modelData.getRoot();

        ModelPartData bone = partData.addChild("bone", ModelPartBuilder.create().uv(0, 3).cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 7).cuboid(-5.0F, -1.0F, 1.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-6.0F, -1.0F, 5.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-7.0F, -1.0F, 3.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-8.0F, -1.0F, 7.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-9.0F, -1.0F, 8.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-9.0F, -1.0F, 5.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-6.0F, -1.0F, 6.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-8.0F, -1.0F, 4.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-6.0F, -1.0F, 2.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-4.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-2.0F, -1.0F, -2.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-3.0F, -1.0F, -5.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-2.0F, -1.0F, -3.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(2.0F, -1.0F, -1.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(1.0F, -1.0F, -3.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(3.0F, -1.0F, 1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(2.0F, -1.0F, -4.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(5.0F, -1.0F, -7.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(4.0F, -1.0F, -6.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(4.0F, -1.0F, -6.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(3.0F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(2.0F, -1.0F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-3.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(-1.0F, -1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(3.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)),
                ModelTransform.of(0.0F, 0, 0.0F, -1.5708F, 0.0F, 2.3562F));

        return TexturedModelData.of(modelData, 16, 16);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        bone.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}