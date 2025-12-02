package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.GrimcrowCaptainBossEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GrimcrowCaptainModel extends AnimatedGeoModel<GrimcrowCaptainBossEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(GrimcrowCaptainBossEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/grimcrow_captain.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(GrimcrowCaptainBossEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/grimcrow_captain.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GrimcrowCaptainBossEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/grimcrow_captain_boss.png");
    }
}

