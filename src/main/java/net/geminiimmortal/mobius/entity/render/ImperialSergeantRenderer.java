package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.ImperialSergeantEntity;
import net.geminiimmortal.mobius.entity.model.ImperialSergeantModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ImperialSergeantRenderer extends GeoEntityRenderer<ImperialSergeantEntity> {
    public ImperialSergeantRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ImperialSergeantModel());
    }

    @Override
    public ResourceLocation getTextureLocation(ImperialSergeantEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/imperial_sergeant.png");
    }
}

