package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.FaedeerEntity;
import net.geminiimmortal.mobius.entity.custom.FaestagEntity;
import net.geminiimmortal.mobius.entity.model.FaedeerModel;
import net.geminiimmortal.mobius.entity.model.FaestagModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class FaestagRenderer extends GeoEntityRenderer<FaestagEntity> {
    public FaestagRenderer(EntityRendererManager renderManager) {
        super(renderManager, new FaestagModel());
        this.shadowRadius = 0.5f;
        this.addLayer(new FaestagRendererEmissiveLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(FaestagEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/faestag.png");
    }
}

