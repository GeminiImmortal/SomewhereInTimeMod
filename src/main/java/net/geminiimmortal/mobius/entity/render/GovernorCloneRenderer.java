package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.entity.custom.GovernorCloneEntity;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.geminiimmortal.mobius.entity.model.GovernorCloneModel;
import net.geminiimmortal.mobius.entity.model.GovernorModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GovernorCloneRenderer extends GeoEntityRenderer<GovernorCloneEntity> {
    public GovernorCloneRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GovernorCloneModel());
    }

    @Override
    public ResourceLocation getTextureLocation(GovernorCloneEntity entity) {
        return new ResourceLocation("mobius", "textures/entity/governor_new.png");
    }
}

