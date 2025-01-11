package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.FaedeerEntity;
import net.minecraft.util.ResourceLocation;

import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FaedeerModel extends AnimatedGeoModel<FaedeerEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(FaedeerEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/faedeer.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(FaedeerEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/faedeer.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FaedeerEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/faedeer_buck.png");
    }

    public ResourceLocation getEmissiveTextureLocation(FaedeerEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/faedeer_buck_emissive.png");
    }

}

