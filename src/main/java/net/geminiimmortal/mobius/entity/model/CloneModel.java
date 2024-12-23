package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.CloneEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CloneModel extends AnimatedGeoModel<CloneEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(CloneEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/governor.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(CloneEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/governor.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CloneEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/governor.png");
    }

}

