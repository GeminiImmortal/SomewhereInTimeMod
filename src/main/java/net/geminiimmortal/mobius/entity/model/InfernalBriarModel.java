package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.InfernalBriarEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class InfernalBriarModel extends AnimatedGeoModel<InfernalBriarEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(InfernalBriarEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/infernal_briar.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(InfernalBriarEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/infernal_briar.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(InfernalBriarEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/infernal_briar.png");
    }

}

