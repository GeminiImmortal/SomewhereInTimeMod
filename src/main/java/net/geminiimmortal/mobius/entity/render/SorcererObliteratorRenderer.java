package net.geminiimmortal.mobius.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.entity.custom.spell.ObliteratorEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class SorcererObliteratorRenderer extends EntityRenderer<ObliteratorEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("mobius", "textures/entity/knives_out.png");

    public SorcererObliteratorRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(ObliteratorEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        matrixStack.pushPose();

        float baseYOffset = 6.0f; // Starting Y offset
        float spacing = 5.0f;     // Vertical spacing between each circle
        float pulseSpeed = 0.1f;  // Speed of pulsing effect

        // Draw 3 circles vertically stacked
        for (int i = 0; i < 3; i++) {
            matrixStack.pushPose();

            // Position each circle at increasing height
            matrixStack.translate(0.0, baseYOffset + (i * spacing), 0.0);
            matrixStack.scale(8.0f, 8.0f, 8.0f);

            // Calculate rotation for each circle (alternating directions)
            float rotation = entity.getRotationAngle() + (partialTicks * 1.0f) + (i * 15); // add offset to each
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotation));

            float pulse = MathHelper.sin(entity.tickCount * pulseSpeed + i * 1.0f) * 0.1f + 1.0f; // same pulse

            float shrinkMultiplier = 1.0f;
            if (entity.isBeamFiring()) {
                shrinkMultiplier = (float) Math.pow(Math.max(0.0f, 1.0f - (entity.getShrinkTicks() / 4.0f)), 10);
            }

            float finalScale = pulse * shrinkMultiplier;
            matrixStack.scale(finalScale, finalScale, finalScale);


            // Glow effect by modulating alpha over time
            int alpha = (int)(MathHelper.sin(entity.tickCount * 0.1f + i) * 127 + 128); // Sinusoidal alpha change for glowing effect
            alpha = MathHelper.clamp(alpha, 0, 255);

            // Render the glowing, rotating circle
            IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
            Matrix4f matrix4f = matrixStack.last().pose();

            vertexBuilder.vertex(matrix4f, -0.5f, 0.0f, -0.5f).color(255, 255, 255, alpha).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0.0f, 1.0f, 0.0f).endVertex();
            vertexBuilder.vertex(matrix4f, 0.5f, 0.0f, -0.5f).color(255, 255, 255, alpha).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0.0f, 1.0f, 0.0f).endVertex();
            vertexBuilder.vertex(matrix4f, 0.5f, 0.0f, 0.5f).color(255, 255, 255, alpha).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0.0f, 1.0f, 0.0f).endVertex();
            vertexBuilder.vertex(matrix4f, -0.5f, 0.0f, 0.5f).color(255, 255, 255, alpha).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0.0f, 1.0f, 0.0f).endVertex();

            matrixStack.popPose();
        }

        matrixStack.popPose();

        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }


    @Override
    public ResourceLocation getTextureLocation(ObliteratorEntity entity) {
        return TEXTURE;
    }
}

