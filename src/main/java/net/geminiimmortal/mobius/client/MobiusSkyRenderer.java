package net.geminiimmortal.mobius.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.ISkyRenderHandler;
import org.lwjgl.opengl.GL11;

public class MobiusSkyRenderer implements ISkyRenderHandler {
    private static final ResourceLocation MOON_TEXTURE = new ResourceLocation("minecraft", "textures/environment/sun.png");
    private static final ResourceLocation SUN_TEXTURE = new ResourceLocation("minecraft", "textures/environment/sun.png");

    @Override
    public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc) {
        RenderSystem.disableTexture();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1F, 1F, 1F, 1F);

        matrixStack.pushPose();

        // Optional: render stars based on brightness
        float starBrightness = world.getStarBrightness(partialTicks);
        if (starBrightness > 0.0F) {
            RenderSystem.color4f(starBrightness, starBrightness, starBrightness, starBrightness);
            // You can use vanilla star rendering here if needed
        }

        float time = world.getTimeOfDay(partialTicks); // 0.0 at sunrise, 0.5 at sunset
        float sunPitch = -time * 360.0F;

        float midnightSunPitch = -time * 360F - 180F;

        // Render first sun (yellow)
        renderSun(matrixStack, 30F, sunPitch + 20, 0.2F, 0.4F, 0.9F);

        // Render second sun (orange)
        renderSun(matrixStack, 0F, sunPitch, 0.92F, 0.6F, 0.3F);

        // Render the moon (stub)
        renderSun(matrixStack, 0F, midnightSunPitch, 1.0f, 0.0f, 0.2f); // You can implement this later

        matrixStack.popPose();

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
    }

    private void renderSun(MatrixStack matrixStack, float yaw, float pitch, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();

        matrixStack.pushPose();
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(pitch));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(yaw));

        Matrix4f matrix = matrixStack.last().pose();

        RenderSystem.enableBlend();
        RenderSystem.enableBlend();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

        RenderSystem.enableTexture();
        RenderSystem.color4f(r, g, b, 1.0F);

        mc.getTextureManager().bind(SUN_TEXTURE);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        float size = 30.0F;

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.vertex(matrix, -size, 100.0F, -size).uv(0F, 0F).endVertex();
        buffer.vertex(matrix, size, 100.0F, -size).uv(1F, 0F).endVertex();
        buffer.vertex(matrix, size, 100.0F, size).uv(1F, 1F).endVertex();
        buffer.vertex(matrix, -size, 100.0F, size).uv(0F, 1F).endVertex();
        tessellator.end();

        matrixStack.popPose();
    }

    private void renderMoon(MatrixStack matrixStack, float size, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();

        matrixStack.pushPose();

        Matrix4f matrix = matrixStack.last().pose();

        RenderSystem.enableBlend();
        RenderSystem.enableBlend();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

        RenderSystem.enableTexture();
        RenderSystem.color4f(r, g, b, 1.0F);

        mc.getTextureManager().bind(MOON_TEXTURE);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.vertex(matrix, -size, 100.0F, -size).uv(0F, 0F).endVertex();
        buffer.vertex(matrix, size, 100.0F, -size).uv(1F, 0F).endVertex();
        buffer.vertex(matrix, size, 100.0F, size).uv(1F, 1F).endVertex();
        buffer.vertex(matrix, -size, 100.0F, size).uv(0F, 1F).endVertex();
        tessellator.end();

        matrixStack.popPose();
    }
}
