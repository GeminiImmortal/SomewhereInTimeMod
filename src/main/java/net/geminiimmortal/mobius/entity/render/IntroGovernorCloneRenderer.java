package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.entity.custom.IntroGovernorCloneEntity;
import net.geminiimmortal.mobius.entity.model.IntroGovernorCloneModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class IntroGovernorCloneRenderer extends GeoEntityRenderer<IntroGovernorCloneEntity> {
    public IntroGovernorCloneRenderer(EntityRendererManager renderManager) {
        super(renderManager, new IntroGovernorCloneModel());
    }

    @Override
    public ResourceLocation getTextureLocation(IntroGovernorCloneEntity entity) {
        return new ResourceLocation("mobius", "textures/entity/governor_new.png");
    }
}

