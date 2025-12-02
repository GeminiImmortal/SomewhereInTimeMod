package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.geminiimmortal.mobius.entity.custom.GrimcrowEntity;
import net.geminiimmortal.mobius.entity.model.FootmanModel;
import net.geminiimmortal.mobius.entity.model.GrimcrowModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GrimcrowRenderer extends GeoEntityRenderer<GrimcrowEntity> {
    public GrimcrowRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GrimcrowModel());
    }

    @Override
    public ResourceLocation getTextureLocation(GrimcrowEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/grimcrow.png");
    }
}

