package net.geminiimmortal.mobius.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.tileentity.GlowingBlockTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

public class GlowingBlockRenderer extends TileEntityRenderer<GlowingBlockTileEntity> {

    private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/block/standing_gloomcap_cap_additive.png");

    public GlowingBlockRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(GlowingBlockTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        // Push the matrix to preserve transformations
        matrixStack.pushPose();

        // Translate to the block position
        matrixStack.translate(0.5, 0.5, 0.5); // Center at block

        // Access the buffer to render the glowing quad
        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.entityTranslucent(GLOW_TEXTURE));

        // Create a quad (example: top face of the block)
        Matrix4f matrix = matrixStack.last().pose();
        float size = 0.5f; // Adjust for scaling

        // Full brightness for glow
        int brightness = 0xF000F0;

        // Define glow color (RGBA: red, green, blue, alpha)
        float red = 0.7f;   // Set red component (0.0 - 1.0)
        float green = 0.0f; // Set green component (0.0 - 1.0)
        float blue = 0.5f;  // Set blue component (0.0 - 1.0)
        float alpha = 1.0f; // Set alpha (transparency) component

        // Define the quad vertices with color
        vertexBuilder.vertex(matrix, -size, size, -size).color(red, green, blue, alpha).uv(0, 0).overlayCoords(combinedOverlay).uv2(brightness).endVertex();
        vertexBuilder.vertex(matrix, size, size, -size).color(red, green, blue, alpha).uv(1, 0).overlayCoords(combinedOverlay).uv2(brightness).endVertex();
        vertexBuilder.vertex(matrix, size, size, size).color(red, green, blue, alpha).uv(1, 1).overlayCoords(combinedOverlay).uv2(brightness).endVertex();
        vertexBuilder.vertex(matrix, -size, size, size).color(red, green, blue, alpha).uv(0, 1).overlayCoords(combinedOverlay).uv2(brightness).endVertex();

        // Pop the matrix to restore transformations
        matrixStack.popPose();
    }

}

