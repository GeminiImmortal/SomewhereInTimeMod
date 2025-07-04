package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.geminiimmortal.mobius.entity.custom.ImperialCommanderEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ImperialCommanderModel extends AnimatedGeoModel<ImperialCommanderEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(ImperialCommanderEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/imperial_commander.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(ImperialCommanderEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/imperial_commander.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ImperialCommanderEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/imperial_commander.png");
    }

}

