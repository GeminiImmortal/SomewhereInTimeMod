package net.geminiimmortal.mobius.entity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.RebelInstigatorEntity;
import net.geminiimmortal.mobius.entity.custom.RebelQuartermasterEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RebelQuartermasterModel extends AnimatedGeoModel<RebelQuartermasterEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(RebelQuartermasterEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/rebel_quartermaster.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(RebelQuartermasterEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/rebel_quartermaster.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(RebelQuartermasterEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/rebel_quartermaster.png");
    }

}

