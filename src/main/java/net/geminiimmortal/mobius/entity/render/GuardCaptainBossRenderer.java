package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.entity.custom.GuardCaptainBossEntity;
import net.geminiimmortal.mobius.entity.model.GuardCaptainBossModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GuardCaptainBossRenderer extends GeoEntityRenderer<GuardCaptainBossEntity> {
    public GuardCaptainBossRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GuardCaptainBossModel());
    }

    @Override
    public ResourceLocation getTextureLocation(GuardCaptainBossEntity entity) {
        return new ResourceLocation("mobius", "textures/entity/royal_guard_captain.png");
    }
}

