package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.SorcererEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SorcererModel extends AnimatedGeoModel<SorcererEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(SorcererEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/sorcerer.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(SorcererEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/sorcerer.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SorcererEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/sorcerer.png");
    }

}

