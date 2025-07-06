package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.CloneEntity;
import net.geminiimmortal.mobius.entity.model.CloneModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CloneRenderer extends GeoEntityRenderer<CloneEntity> {
    public CloneRenderer(EntityRendererManager renderManager) {
        super(renderManager, new CloneModel());
    }

    @Override
    public ResourceLocation getTextureLocation(CloneEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/governor_new.png");
    }
}

