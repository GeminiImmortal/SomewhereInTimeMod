package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GovernorModel extends AnimatedGeoModel<GovernorEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(GovernorEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/governor.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(GovernorEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/governor.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GovernorEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/governor.png");
    }

}

