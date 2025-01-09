package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.entity.custom.InfernalBriarEntity;
import net.geminiimmortal.mobius.entity.model.InfernalBriarModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class InfernalBriarRenderer extends GeoEntityRenderer<InfernalBriarEntity> {
    public InfernalBriarRenderer(EntityRendererManager renderManager) {
        super(renderManager, new InfernalBriarModel());
    }

    @Override
    public ResourceLocation getTextureLocation(InfernalBriarEntity entity) {
        return new ResourceLocation("mobius", "textures/entity/infernal_briar.png");
    }
}

