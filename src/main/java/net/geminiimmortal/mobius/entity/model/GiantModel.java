package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.GiantEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GiantModel extends AnimatedGeoModel<GiantEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(GiantEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/giant.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(GiantEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/giant.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GiantEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/mountain_giant.png");
    }

}

