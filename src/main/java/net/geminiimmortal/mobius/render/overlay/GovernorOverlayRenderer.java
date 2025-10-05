package net.geminiimmortal.mobius.render.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MobiusMod.MOD_ID)
public class GovernorOverlayRenderer {
    private static final ResourceLocation GOVERNOR_TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/overlay/governor_curse.png");
    private static GovernorEntity currentGovernor;
    private static long displayEndTime = 0;
    private static final int FADE_DURATION = 20;

    public static void trigger(GovernorEntity governor, int durationTicks) {
        currentGovernor = governor;
        displayEndTime = System.currentTimeMillis() + durationTicks * 50L;
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (currentGovernor == null) return;

        long timeLeft = displayEndTime - System.currentTimeMillis();
        if (timeLeft <= 0) {
            currentGovernor = null;
            return;
        }

        float alpha = 1.0f;
        if (timeLeft < FADE_DURATION * 50L) {
            alpha = timeLeft / (FADE_DURATION * 50f);
        }

        Minecraft mc = Minecraft.getInstance();
        TextureManager textureManager = mc.getTextureManager();
        textureManager.bind(GOVERNOR_TEXTURE);
        RenderHelper.turnBackOn();

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        MatrixStack stack = event.getMatrixStack();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1f, 1f, 1f, alpha);

        AbstractGui.blit(stack, (width - 256) / 2, (height - 256) / 2, 0, 0, 256, 256, 256, 256);

        RenderSystem.disableBlend();
        RenderSystem.color4f(1f, 1f, 1f, 1f);
    }
}

