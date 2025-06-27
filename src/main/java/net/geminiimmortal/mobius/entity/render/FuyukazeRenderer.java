package net.geminiimmortal.mobius.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.entity.custom.FuyukazeEntity;
import net.geminiimmortal.mobius.entity.model.FuyukazeModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class FuyukazeRenderer extends GeoEntityRenderer<FuyukazeEntity> {
    public FuyukazeRenderer(EntityRendererManager renderManager) {
        super(renderManager, new FuyukazeModel());
        this.shadowRadius = 1f;
        this.addLayer(new FuyukazeRendererEmissiveLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(FuyukazeEntity entity) {
        return new ResourceLocation("mobius", "textures/entity/fuyukaze.png");
    }

    @Override
    public void renderEarly(FuyukazeEntity entity, MatrixStack matrixStack, float ticks, IRenderTypeBuffer renderTypeBuffer,
                            IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue,
                            float partialTicks) {
        if (entity.isBaby()) {
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            this.shadowRadius = 0.5f;
        }

        super.renderEarly(entity, matrixStack, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }
}

