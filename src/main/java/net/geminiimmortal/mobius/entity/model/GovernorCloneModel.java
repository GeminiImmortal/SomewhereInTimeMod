package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.GovernorCloneEntity;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GovernorCloneModel extends AnimatedGeoModel<GovernorCloneEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(GovernorCloneEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/governor_new.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(GovernorCloneEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/governor_new.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GovernorCloneEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/governor_new.png");
    }

}

