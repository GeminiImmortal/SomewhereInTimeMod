package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.ImperialRegularEntity;
import net.geminiimmortal.mobius.entity.custom.ImperialTowerRegularEntity;
import net.geminiimmortal.mobius.entity.model.ImperialRegularModel;
import net.geminiimmortal.mobius.entity.model.ImperialTowerRegularModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ImperialTowerRegularRenderer extends GeoEntityRenderer<ImperialTowerRegularEntity> {
    public ImperialTowerRegularRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ImperialTowerRegularModel());
    }

    @Override
    public ResourceLocation getTextureLocation(ImperialTowerRegularEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/imperial_regular.png");
    }
}

