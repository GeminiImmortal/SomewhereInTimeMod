package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.BountyHunterEntity;
import net.geminiimmortal.mobius.entity.custom.RebelInstigatorEntity;
import net.geminiimmortal.mobius.entity.model.BountyHunterModel;
import net.geminiimmortal.mobius.entity.model.RebelInstigatorModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RebelInstigatorRenderer extends GeoEntityRenderer<RebelInstigatorEntity> {
    public RebelInstigatorRenderer(EntityRendererManager renderManager) {
        super(renderManager, new RebelInstigatorModel());
    }

    @Override
    public ResourceLocation getTextureLocation(RebelInstigatorEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/rebel_instigator.png");
    }
}

