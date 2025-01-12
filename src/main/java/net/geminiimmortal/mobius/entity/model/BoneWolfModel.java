package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.BoneWolfEntity;
import net.geminiimmortal.mobius.entity.custom.FaedeerEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BoneWolfModel extends AnimatedGeoModel<BoneWolfEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(BoneWolfEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/bone_wolf.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(BoneWolfEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/bone_wolf.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BoneWolfEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/bone_wolf.png");
    }

    public ResourceLocation getEmissiveTextureLocation(FaedeerEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/bone_wolf_emissive.png");
    }
}

