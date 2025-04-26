package net.geminiimmortal.mobius.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.BarrierEntity;
import net.geminiimmortal.mobius.entity.model.BarrierModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class BarrierEntityRenderer extends EntityRenderer<BarrierEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/barrier.png");
    private static final ResourceLocation EMISSIVE_TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/barrier.png");

    public BarrierEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);

    }

    @Override
    public ResourceLocation getTextureLocation(BarrierEntity entity) {
        return TEXTURE;
    }

    private class EmissiveLayer extends LayerRenderer<BarrierEntity, BarrierModel<BarrierEntity>>  {

        public EmissiveLayer(IEntityRenderer<BarrierEntity, BarrierModel<BarrierEntity>> entityRenderer) {
            super(entityRenderer);
        }


        @Override
        public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, BarrierEntity entity,
                           float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                           float netHeadYaw, float headPitch) {

            IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.eyes(EMISSIVE_TEXTURE));
            this.getParentModel().renderToBuffer(matrixStack, vertexBuilder, 0xF000F0, OverlayTexture.NO_OVERLAY,
                    1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}


