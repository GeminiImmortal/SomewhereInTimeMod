package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.RebelInstigatorEntity;
import net.geminiimmortal.mobius.entity.custom.RebelQuartermasterEntity;
import net.geminiimmortal.mobius.entity.model.RebelInstigatorModel;
import net.geminiimmortal.mobius.entity.model.RebelQuartermasterModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RebelQuartermasterRenderer extends GeoEntityRenderer<RebelQuartermasterEntity> {
    public RebelQuartermasterRenderer(EntityRendererManager renderManager) {
        super(renderManager, new RebelQuartermasterModel());
    }

    @Override
    public ResourceLocation getTextureLocation(RebelQuartermasterEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/rebel_quartermaster.png");
    }
}

