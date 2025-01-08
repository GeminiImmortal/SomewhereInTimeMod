package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.entity.custom.BoneWolfEntity;
import net.geminiimmortal.mobius.entity.model.BoneWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BoneWolfRenderer extends GeoEntityRenderer<BoneWolfEntity> {
    public BoneWolfRenderer(EntityRendererManager renderManager) {
        super(renderManager, new BoneWolfModel());
    }

    @Override
    public ResourceLocation getTextureLocation(BoneWolfEntity entity) {
        return new ResourceLocation("mobius", "textures/entity/bone_wolf.png");
    }
}

