package net.geminiimmortal.mobius.render.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.GrimcrowCaptainBossEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, value = Dist.CLIENT)
public class GrimcrowCaptainBossBarRenderer {
    private static final ResourceLocation CAPTAIN_BOSSBAR_TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/gui/bossbars/captain.png");
    private static final ResourceLocation CAPTAIN_HEALTHBAR = new ResourceLocation(MobiusMod.MOD_ID, "textures/gui/bossbars/captain_health_bar.png");
    private static final ResourceLocation CAPTAIN_PROGRESS_OVERLAY = new ResourceLocation(MobiusMod.MOD_ID, "textures/gui/bossbars/captain_health_bar_overlay.png");
    private static final ResourceLocation CAPTAIN_BREAKBAR_TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/gui/bossbars/captain_break_bar.png");
    private static final ResourceLocation CAPTAIN_BREAKBAR_OUTLINE_TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/gui/bossbars/captain_break_bar_outline.png");
    private static final ResourceLocation PARTICLE_TEXTURE =
            new ResourceLocation(MobiusMod.MOD_ID, "textures/particle/tesla_spark.png");

    private static final List<GovernorBossBarParticleRenderer> PARTICLES = new ArrayList<>();


    @SubscribeEvent
    public static void onBossBarRender(RenderGameOverlayEvent.BossInfo event) {
        if (event.getBossInfo().getName().getContents().equals("Grimcrow Captain Davy Van Diesel")) {
            event.setCanceled(true);

            Minecraft mc = Minecraft.getInstance();
            MatrixStack matrixStack = event.getMatrixStack();
            BossInfo bossInfo = event.getBossInfo();


            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int x = (screenWidth / 2) - 91;
            int y = 10;

            int texWidth = 192;
            int texHeight = 30;

            mc.getTextureManager().bind(CAPTAIN_BOSSBAR_TEXTURE);

            AbstractGui.blit(matrixStack, x, y, 0, 0, texWidth, texHeight, texWidth, texHeight);

            Entity viewEntity = Minecraft.getInstance().getCameraEntity();
            if (viewEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) viewEntity;
                World world = player.level;
                GrimcrowCaptainBossEntity boss = world.getEntitiesOfClass(GrimcrowCaptainBossEntity.class,
                                player.getBoundingBox().inflate(100)).stream()
                        .filter(e -> e.isAlive())
                        .findFirst()
                        .orElse(null);

                if (boss != null) {
                    float breakbarProgress = boss.getBreakbarProgress();
                    if (breakbarProgress > 0.0F) {
                        mc.getTextureManager().bind(CAPTAIN_BREAKBAR_TEXTURE);
                        int breakbarFill = (int) (breakbarProgress * texWidth);
                        int breakbarY = y;

                        AbstractGui.blit(matrixStack, x, breakbarY, 0, 0, breakbarFill, texHeight, texWidth, texHeight);

                        mc.getTextureManager().bind(CAPTAIN_BREAKBAR_OUTLINE_TEXTURE);
                        AbstractGui.blit(matrixStack, x, breakbarY, 0, 0, texWidth, texHeight, texWidth, texHeight);
                    }
                }
            }

            int filled = (int) (bossInfo.getPercent() * texWidth);
            if (filled > 0) {
                mc.getTextureManager().bind(CAPTAIN_HEALTHBAR);
                AbstractGui.blit(matrixStack, x, y, 0, 0, filled, texHeight, texWidth, texHeight);
            }

            mc.getTextureManager().bind(CAPTAIN_PROGRESS_OVERLAY);

            AbstractGui.blit(matrixStack, x, y, 0, 0, texWidth, texHeight, texWidth, texHeight);

            if (Minecraft.getInstance().level != null) {
                Random rand = Minecraft.getInstance().level.random;

                if (rand.nextFloat() < 0.08f) {
                    float px = x + rand.nextInt(texWidth);
                    float py = y + rand.nextInt(texHeight);
                    float vx = (rand.nextFloat() - 0.5f) * 0.3f;
                    float vy = -0.05f;
                    int lifetime = 20 + rand.nextInt(20);
                    PARTICLES.add(new GovernorBossBarParticleRenderer(px, py, vx, vy, lifetime));
                }

                mc.getTextureManager().bind(PARTICLE_TEXTURE);
                int spriteSize = 8;

                Iterator<GovernorBossBarParticleRenderer> iter = PARTICLES.iterator();
                while (iter.hasNext()) {
                    GovernorBossBarParticleRenderer p = iter.next();
                    if (!p.tick()) {
                        iter.remove();
                        continue;
                    }

                    AbstractGui.blit(matrixStack,
                            (int) p.x, (int) p.y,
                            0, 0,
                            spriteSize, spriteSize,
                            spriteSize, spriteSize);
                }
            }

            String name = bossInfo.getName().getString();
            int textX = (screenWidth / 2) - (mc.font.width(name) / 2);
            mc.font.draw(matrixStack, name, textX + 1, y - 2, 0xaa0000);

        }
    }
}
