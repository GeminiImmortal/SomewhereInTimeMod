package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FootmanModel extends AnimatedGeoModel<FootmanEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(FootmanEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/footman.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(FootmanEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/footman.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FootmanEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/footman.png");
    }

}

