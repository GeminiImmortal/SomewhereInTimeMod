package net.geminiimmortal.mobius.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.entity.custom.SpellEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class SpellRenderer extends EntityRenderer<SpellEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("mobius", "textures/entity/spell.png");

    public SpellRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(SpellEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        // Push matrix to handle custom transformations
        matrixStack.pushPose();

        // Apply transformations (position, rotation, scale)
        matrixStack.translate(0.0, 0.025, 0.0);
        matrixStack.scale(8.0f, 1.0f, 8.0f);

        // Rotate the spell to face upwards (X-axis rotation)
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-180.0f));

        float rotation = entity.getRotationAngle() + (partialTicks * 1.0f); // Smooth interpolation for rotation
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotation)); // Rotate around the Y-axis

        // Render the spell (e.g., using a custom texture or a particle-like effect)
        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
        Matrix4f matrix4f = matrixStack.last().pose();

        // Adjust normal vector to (0, 0, -1) since the plane is rotated
        vertexBuilder.vertex(matrix4f, -0.5f, 0.0f, -0.5f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0.0f, 0.0f, -1.0f).endVertex();
        vertexBuilder.vertex(matrix4f, 0.5f, 0.0f, -0.5f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0.0f, 0.0f, -1.0f).endVertex();
        vertexBuilder.vertex(matrix4f, 0.5f, 0.0f, 0.5f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0.0f, 0.0f, -1.0f).endVertex();
        vertexBuilder.vertex(matrix4f, -0.5f, 0.0f, 0.5f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0.0f, 0.0f, -1.0f).endVertex();

        // Pop matrix
        matrixStack.popPose();

        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }


    @Override
    public ResourceLocation getTextureLocation(SpellEntity entity) {
        return TEXTURE;
    }
}

