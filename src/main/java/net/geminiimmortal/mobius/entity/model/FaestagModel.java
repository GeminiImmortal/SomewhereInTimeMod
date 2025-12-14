package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.FaedeerEntity;
import net.geminiimmortal.mobius.entity.custom.FaestagEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FaestagModel extends AnimatedGeoModel<FaestagEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(FaestagEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/faestag.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(FaestagEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/faestag.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FaestagEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/faestag.png");
    }
}

