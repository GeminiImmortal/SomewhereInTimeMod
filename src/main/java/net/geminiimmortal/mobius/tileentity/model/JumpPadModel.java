package net.geminiimmortal.mobius.tileentity.model;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.tileentity.JumpPadTileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class JumpPadModel extends AnimatedGeoModel<JumpPadTileEntity> {


    @Override
    public ResourceLocation getModelLocation(JumpPadTileEntity tile) {
        return new ResourceLocation(MobiusMod.MOD_ID, "geo/jump_pad.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(JumpPadTileEntity tile) {
        if (tile.isLinked()) {
            if (tile.isActive()) {
                return new ResourceLocation(MobiusMod.MOD_ID, "textures/block/jump_pad/jump_pad_active.png");
            } else {
                return new ResourceLocation(MobiusMod.MOD_ID, "textures/block/jump_pad/jump_pad_locked.png");
            }
        }
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/block/jump_pad/jump_pad_inactive.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(JumpPadTileEntity tile) {
        return new ResourceLocation(MobiusMod.MOD_ID, "animations/jump_pad.animation.json");
    }
}

