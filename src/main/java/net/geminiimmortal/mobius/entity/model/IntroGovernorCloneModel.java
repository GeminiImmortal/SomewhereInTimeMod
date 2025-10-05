package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.IntroGovernorCloneEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class IntroGovernorCloneModel extends AnimatedGeoModel<IntroGovernorCloneEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(IntroGovernorCloneEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/governor_new.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(IntroGovernorCloneEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/governor_new.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(IntroGovernorCloneEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/governor_new.png");
    }

}

