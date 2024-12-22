package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.entity.custom.FaedeerEntity;
import net.geminiimmortal.mobius.entity.custom.SorcererEntity;
import net.geminiimmortal.mobius.entity.model.FaedeerModel;
import net.geminiimmortal.mobius.entity.model.SorcererModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SorcererRenderer extends GeoEntityRenderer<SorcererEntity> {
    public SorcererRenderer(EntityRendererManager renderManager) {
        super(renderManager, new SorcererModel());
    }

    @Override
    public ResourceLocation getTextureLocation(SorcererEntity entity) {
        return new ResourceLocation("mobius", "textures/entity/sorcerer.png");
    }
}

