package net.geminiimmortal.mobius.tileentity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.custom.WardingObeliskBlock;
import net.geminiimmortal.mobius.tileentity.WardingObeliskTileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

public class WardingObeliskModel extends AnimatedGeoModel<WardingObeliskTileEntity> {


    @Override
    public ResourceLocation getModelLocation(WardingObeliskTileEntity tile) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/warding_obelisk.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(WardingObeliskTileEntity tile) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/block/warding_obelisk.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(WardingObeliskTileEntity tile) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/warding_obelisk.animation.json");
    }

    public ResourceLocation getEmissiveTextureLocation(WardingObeliskTileEntity tile) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/block/warding_obelisk_emissive.png");
    }
}

