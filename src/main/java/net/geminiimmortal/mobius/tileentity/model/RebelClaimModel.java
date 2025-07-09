package net.geminiimmortal.mobius.tileentity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.tileentity.RebelClaimTileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RebelClaimModel extends AnimatedGeoModel<RebelClaimTileEntity> {


    @Override
    public ResourceLocation getModelLocation(RebelClaimTileEntity tile) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/rebel_claim.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(RebelClaimTileEntity tile) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/block/rebel_claim.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(RebelClaimTileEntity tile) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/capture_point.animation.json");
    }
}

