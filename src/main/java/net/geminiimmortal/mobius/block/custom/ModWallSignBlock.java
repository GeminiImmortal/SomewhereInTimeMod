package net.geminiimmortal.mobius.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.geminiimmortal.mobius.tileentity.ModSignsTileEntity;

import javax.annotation.Nullable;

public class ModWallSignBlock extends WallSignBlock {
    public ModWallSignBlock(Properties properties, WoodType type) {
        super(properties, type);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ModSignsTileEntity();
    }

}
