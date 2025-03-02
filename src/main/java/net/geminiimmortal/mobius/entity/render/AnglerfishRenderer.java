package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.entity.custom.AnglerfishEntity;
import net.geminiimmortal.mobius.entity.model.AnglerfishModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class AnglerfishRenderer extends GeoEntityRenderer<AnglerfishEntity> {
    public AnglerfishRenderer(EntityRendererManager renderManager) {
        super(renderManager, new AnglerfishModel());
        this.shadowRadius = 0.5f;
        this.addLayer(new AnglerfishRendererEmissiveLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(AnglerfishEntity entity) {
        return new ResourceLocation("mobius", "textures/entity/anglerfish.png");
    }
}

