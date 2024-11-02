package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.entity.custom.FaedeerEntity;
import net.geminiimmortal.mobius.entity.model.FaedeerModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class FaedeerRenderer extends GeoEntityRenderer<FaedeerEntity> {
    public FaedeerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new FaedeerModel());
    }

    @Override
    public ResourceLocation getTextureLocation(FaedeerEntity entity) {
        return new ResourceLocation("mobius", "textures/entity/faedeer_buck.png");
    }
}

