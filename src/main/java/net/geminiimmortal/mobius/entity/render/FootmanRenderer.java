package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.geminiimmortal.mobius.entity.custom.GiantEntity;
import net.geminiimmortal.mobius.entity.model.FootmanModel;
import net.geminiimmortal.mobius.entity.model.GiantModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class FootmanRenderer extends GeoEntityRenderer<FootmanEntity> {
    public FootmanRenderer(EntityRendererManager renderManager) {
        super(renderManager, new FootmanModel());
    }

    @Override
    public ResourceLocation getTextureLocation(FootmanEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/footman.png");
    }
}

