package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.FuyukazeEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FuyukazeModel extends AnimatedGeoModel<FuyukazeEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(FuyukazeEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/fuyukaze.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(FuyukazeEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/fuyukaze.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FuyukazeEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/fuyukaze.png");
    }

    public ResourceLocation getEmissiveTextureLocation(FuyukazeEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/fuyukaze_e.png");
    }

}

