package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
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
        if (entity.getGrinning()) {
            return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/governor_new_grin.png");
        }

        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/governor_new.png");
    }
}

