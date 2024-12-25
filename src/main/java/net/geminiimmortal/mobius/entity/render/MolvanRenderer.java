package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.entity.custom.CloneEntity;
import net.geminiimmortal.mobius.entity.custom.MolvanEntity;
import net.geminiimmortal.mobius.entity.model.CloneModel;
import net.geminiimmortal.mobius.entity.model.MolvanModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MolvanRenderer extends GeoEntityRenderer<MolvanEntity> {
    public MolvanRenderer(EntityRendererManager renderManager) {
        super(renderManager, new MolvanModel());
    }

    @Override
    public ResourceLocation getTextureLocation(MolvanEntity entity) {
        return new ResourceLocation("mobius", "textures/entity/molvan.png");
    }
}

