package net.geminiimmortal.mobius.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.FuyukazeEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class FuyukazeRendererEmissiveLayer extends GeoLayerRenderer<FuyukazeEntity> {
    public FuyukazeRendererEmissiveLayer(IGeoRenderer<FuyukazeEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffer, int packedLight, FuyukazeEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ResourceLocation emissiveTexture = new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/fuyukaze.png");

        IVertexBuilder vertexConsumer = buffer.getBuffer(RenderType.eyes(emissiveTexture));

        this.getRenderer().render(
                this.getEntityModel().getModel(this.getEntityModel().getModelLocation(entity)),
                entity,
                partialTicks,
                RenderType.eyes(emissiveTexture),
                stack,
                buffer,
                vertexConsumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 0.1F
        );
    }
}

