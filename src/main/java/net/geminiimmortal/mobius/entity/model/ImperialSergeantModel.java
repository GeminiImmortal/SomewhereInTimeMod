package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.ImperialSergeantEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ImperialSergeantModel extends AnimatedGeoModel<ImperialSergeantEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(ImperialSergeantEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/imperial_sergeant.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(ImperialSergeantEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/imperial_sergeant.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ImperialSergeantEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/imperial_sergeant.png");
    }

}

