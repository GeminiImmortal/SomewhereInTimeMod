package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.geminiimmortal.mobius.entity.custom.ImperialCommanderEntity;
import net.geminiimmortal.mobius.entity.model.FootmanModel;
import net.geminiimmortal.mobius.entity.model.ImperialCommanderModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ImperialCommanderRenderer extends GeoEntityRenderer<ImperialCommanderEntity> {
    public ImperialCommanderRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ImperialCommanderModel());
    }

    @Override
    public ResourceLocation getTextureLocation(ImperialCommanderEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/imperial_commander.png");
    }
}

