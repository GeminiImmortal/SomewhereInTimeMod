package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.FuyukazeEntity;
import net.geminiimmortal.mobius.entity.custom.GiantEntity;
import net.geminiimmortal.mobius.entity.custom.JackalopeEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class JackalopeModel extends AnimatedGeoModel<JackalopeEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(JackalopeEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/jackalope.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(JackalopeEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/jackalope.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(JackalopeEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/jackalope.png");
    }

    public ResourceLocation getEmissiveTextureLocation(JackalopeEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/jackalope_e.png");
    }

}

