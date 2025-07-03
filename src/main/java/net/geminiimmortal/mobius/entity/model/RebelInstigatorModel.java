package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.BountyHunterEntity;
import net.geminiimmortal.mobius.entity.custom.RebelInstigatorEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RebelInstigatorModel extends AnimatedGeoModel<RebelInstigatorEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(RebelInstigatorEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/rebel_instigator.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(RebelInstigatorEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/rebel_instigator.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(RebelInstigatorEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/rebel_instigator.png");
    }

}

