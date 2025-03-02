package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.AnglerfishEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AnglerfishModel extends AnimatedGeoModel<AnglerfishEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(AnglerfishEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/anglerfish.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(AnglerfishEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/anglerfish_two.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(AnglerfishEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/anglerfish.png");
    }

    public ResourceLocation getEmissiveTextureLocation(AnglerfishEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/anglerfish_emissive.png");
    }
}

