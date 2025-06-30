package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.BountyHunterEntity;
import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BountyHunterModel extends AnimatedGeoModel<BountyHunterEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(BountyHunterEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/bounty_hunter.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(BountyHunterEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/bounty_hunter.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BountyHunterEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/bounty_hunter.png");
    }

}

