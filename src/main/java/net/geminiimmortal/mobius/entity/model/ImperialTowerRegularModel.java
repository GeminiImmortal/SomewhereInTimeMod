package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.ImperialRegularEntity;
import net.geminiimmortal.mobius.entity.custom.ImperialTowerRegularEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ImperialTowerRegularModel extends AnimatedGeoModel<ImperialTowerRegularEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(ImperialTowerRegularEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/imperial_regular.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(ImperialTowerRegularEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/imperial_regular.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ImperialTowerRegularEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/imperial_regular.png");
    }

}

