package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.GiantEntity;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.geminiimmortal.mobius.entity.model.GiantModel;
import net.geminiimmortal.mobius.entity.model.GovernorModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GiantRenderer extends GeoEntityRenderer<GiantEntity> {
    public GiantRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GiantModel());
    }

    @Override
    public ResourceLocation getTextureLocation(GiantEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/mountain_giant.png");
    }
}

