package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.ImperialRegularEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ImperialRegularModel extends AnimatedGeoModel<ImperialRegularEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(ImperialRegularEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/imperial_regular.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(ImperialRegularEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/imperial_regular.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ImperialRegularEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/imperial_regular.png");
    }

}

