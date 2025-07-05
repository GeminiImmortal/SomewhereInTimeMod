package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.geminiimmortal.mobius.entity.model.GovernorModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GovernorRenderer extends GeoEntityRenderer<GovernorEntity> {
    public GovernorRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GovernorModel());
    }

    @Override
    public ResourceLocation getTextureLocation(GovernorEntity entity) {
        return new ResourceLocation("mobius", "textures/entity/governor_new.png");
    }
}

