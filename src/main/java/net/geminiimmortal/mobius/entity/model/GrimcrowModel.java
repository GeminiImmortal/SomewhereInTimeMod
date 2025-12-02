package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.geminiimmortal.mobius.entity.custom.GrimcrowEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GrimcrowModel extends AnimatedGeoModel<GrimcrowEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(GrimcrowEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/grimcrow.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(GrimcrowEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/grimcrow.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GrimcrowEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/grimcrow.png");
    }

}

