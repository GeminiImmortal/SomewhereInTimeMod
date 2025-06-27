package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.entity.custom.FuyukazeEntity;
import net.geminiimmortal.mobius.entity.model.FuyukazeModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class FuyukazeRenderer extends GeoEntityRenderer<FuyukazeEntity> {
    public FuyukazeRenderer(EntityRendererManager renderManager) {
        super(renderManager, new FuyukazeModel());
        this.shadowRadius = 0.5f;
        this.addLayer(new FuyukazeRendererEmissiveLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(FuyukazeEntity entity) {
        return new ResourceLocation("mobius", "textures/entity/fuyukaze.png");
    }
}

