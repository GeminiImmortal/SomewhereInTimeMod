package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.geminiimmortal.mobius.entity.custom.ImperialRegularEntity;
import net.geminiimmortal.mobius.entity.model.FootmanModel;
import net.geminiimmortal.mobius.entity.model.ImperialRegularModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ImperialRegularRenderer extends GeoEntityRenderer<ImperialRegularEntity> {
    public ImperialRegularRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ImperialRegularModel());
    }

    @Override
    public ResourceLocation getTextureLocation(ImperialRegularEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/imperial_regular.png");
    }
}

