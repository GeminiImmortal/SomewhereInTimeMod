package net.geminiimmortal.mobius.block.custom.flora;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class CandyCane extends Block {

    protected static final VoxelShape COLLISION_SHAPE = Block.box(6.5, 0.0, 6.5, 9.5, 16.0, 9.5);

    public CandyCane(Properties properties) {
        super(Properties.of(Material.PLANT)
                .strength(1.0F)
                .sound(SoundType.BAMBOO)
                .noOcclusion()
                .randomTicks()
                .instabreak());
    }
        @Override
        public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
            if (!world.isAreaLoaded(pos, 1)) return;

            BlockPos above = pos.above();
            if (world.isEmptyBlock(above)) {
                int height = 0;
                BlockPos current = pos;

                // Count the height of the plant
                while (world.getBlockState(current.below()).getBlock() instanceof CandyCane) {
                    height++;
                    current = current.below();
                }

                if (height < 12 && random.nextInt(3) == 0) { // Limit height to 12 blocks
                    world.setBlockAndUpdate(above, this.defaultBlockState());
                }
            }
        }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        BlockPos below = pos.below();
        Block blockBelow = world.getBlockState(below).getBlock();

        return blockBelow == ModBlocks.AURORA_GRASS_BLOCK.get() || blockBelow == ModBlocks.AURORA_DIRT.get() || blockBelow == this;
    }

    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    public boolean isPathfindable(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
        return false;
    }

    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        Vector3d vector3d = p_220071_1_.getOffset(p_220071_2_, p_220071_3_);
        return COLLISION_SHAPE.move(vector3d.x, vector3d.y, vector3d.z);
    }
}