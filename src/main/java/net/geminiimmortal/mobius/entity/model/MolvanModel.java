package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.MolvanEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MolvanModel extends AnimatedGeoModel<MolvanEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(MolvanEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/molvan.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(MolvanEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/molvan.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(MolvanEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/molvan.png");
    }

}

