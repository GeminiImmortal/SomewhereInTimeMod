package net.geminiimmortal.mobius.tileentity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.tileentity.CapturePointTileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CapturePointModel extends AnimatedGeoModel<CapturePointTileEntity> {


    @Override
    public ResourceLocation getModelLocation(CapturePointTileEntity tile) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/capture_point.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CapturePointTileEntity tile) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/block/capture_point.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CapturePointTileEntity tile) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/capture_point.animation.json");
    }
}

