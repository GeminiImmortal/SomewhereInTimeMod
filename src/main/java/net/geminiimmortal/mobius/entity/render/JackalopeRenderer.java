package net.geminiimmortal.mobius.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.JackalopeEntity;
import net.geminiimmortal.mobius.entity.model.JackalopeModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class JackalopeRenderer extends GeoEntityRenderer<JackalopeEntity> {
    public JackalopeRenderer(EntityRendererManager renderManager) {
        super(renderManager, new JackalopeModel());
        this.addLayer(new JackalopeRendererEmissiveLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(JackalopeEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/jackalope.png");
    }

    @Override
    public void renderEarly(JackalopeEntity entity, MatrixStack matrixStack, float ticks, IRenderTypeBuffer renderTypeBuffer,
                            IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue,
                            float partialTicks) {
        if (entity.isBaby()) {
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            this.shadowRadius = 0.5f;
        }

        super.renderEarly(entity, matrixStack, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }
}

