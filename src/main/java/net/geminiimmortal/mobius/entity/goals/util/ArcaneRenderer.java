package net.geminiimmortal.mobius.entity.goals.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import javafx.scene.Camera;
import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ArcaneRenderer {
    private static BlockPos circlePos = null;
    private static boolean beamActive = false;
    private static int age = 0;

    private static final ResourceLocation CIRCLE_TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/effect/arcane_circle.png");
    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/effect/arcane_beam.png");

    public static void startCircle(BlockPos pos) {
        circlePos = pos;
        age = 0;
        beamActive = false;
    }

    public static void fireBeam(BlockPos pos) {
        beamActive = true;
    }

    public static void stop() {
        circlePos = null;
        beamActive = false;
    }

    @SubscribeEvent
    public static void onRenderWorld(RenderWorldLastEvent event) {
        if (circlePos == null) return;

        Minecraft mc = Minecraft.getInstance();
        ActiveRenderInfo camera = mc.gameRenderer.getMainCamera();
        Vector3d camPos = camera.getPosition();

        MatrixStack matrixStack = event.getMatrixStack();
        matrixStack.pushPose();

        // Translate to beam center
        double dx = circlePos.getX() + 0.5 - camPos.x;
        double dy = circlePos.getY() + 0.01 - camPos.y; // just above ground
        double dz = circlePos.getZ() + 0.5 - camPos.z;
        matrixStack.translate(dx, dy, dz);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        Minecraft.getInstance().getTextureManager().bind(CIRCLE_TEXTURE);

        drawCircle(matrixStack, age / 40.0f); // Charge percent (0.0–1.0)

        if (beamActive) {
            Minecraft.getInstance().getTextureManager().bind(BEAM_TEXTURE);
            drawBeam(matrixStack);
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        matrixStack.popPose();

        age++;
    }

    private static void drawCircle(MatrixStack matrixStack, float charge) {
        Matrix4f matrix = matrixStack.last().pose();
        float size = 6f * charge;
        float alpha = 0.6f + 0.4f * charge;
        float u1 = 0f, v1 = 0f, u2 = 1f, v2 = 1f;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        buffer.vertex(matrix, -size, 0, -size).uv(u1, v1).color(1f, 1f, 1f, alpha).endVertex();
        buffer.vertex(matrix, -size, 0, size).uv(u1, v2).color(1f, 1f, 1f, alpha).endVertex();
        buffer.vertex(matrix, size, 0, size).uv(u2, v2).color(1f, 1f, 1f, alpha).endVertex();
        buffer.vertex(matrix, size, 0, -size).uv(u2, v1).color(1f, 1f, 1f, alpha).endVertex();

        tessellator.end();
    }

    private static void drawBeam(MatrixStack matrixStack) {
        Matrix4f matrix = matrixStack.last().pose();
        float width = 1.5f;
        float height = 100f;
        float alpha = 0.8f;
        float u1 = 0f, v1 = 0f, u2 = 1f, v2 = 1f;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        // You can render four sides for a full beam or two sides for a flat one
        for (int i = 0; i < 4; i++) {
            float angle = (float) (Math.PI / 2 * i);
            float x1 = width * (float) Math.cos(angle);
            float z1 = width * (float) Math.sin(angle);
            float x2 = width * (float) Math.cos(angle + Math.PI / 2);
            float z2 = width * (float) Math.sin(angle + Math.PI / 2);

            buffer.vertex(matrix, x1, 0, z1).uv(u1, v1).color(1f, 1f, 1f, alpha).endVertex();
            buffer.vertex(matrix, x1, height, z1).uv(u1, v2).color(1f, 1f, 1f, alpha).endVertex();
            buffer.vertex(matrix, x2, height, z2).uv(u2, v2).color(1f, 1f, 1f, alpha).endVertex();
            buffer.vertex(matrix, x2, 0, z2).uv(u2, v1).color(1f, 1f, 1f, alpha).endVertex();
        }

        tessellator.end();
    }
}


