package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.GuardCaptainBossEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GuardCaptainBossModel extends AnimatedGeoModel<GuardCaptainBossEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(GuardCaptainBossEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/royal_guard_captain.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(GuardCaptainBossEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/royal_guard_captain.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GuardCaptainBossEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/royal_guard_captain.png");
    }

}

