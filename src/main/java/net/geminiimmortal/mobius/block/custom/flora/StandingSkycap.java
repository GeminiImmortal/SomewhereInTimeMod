package net.geminiimmortal.mobius.block.custom.flora;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class StandingSkycap extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 14.0, 30.0, 14.0);

    public StandingSkycap(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public void randomTick(BlockState p_225542_1_, ServerWorld p_225542_2_, BlockPos p_225542_3_, Random p_225542_4_) {

    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        float chance = 0.09f;
        if (chance < rand.nextFloat()) {
            if (worldIn.isClientSide()) {
                worldIn.addParticle(ModParticles.FAEDEER_PARTICLE.get(), pos.getX() + rand.nextDouble(),
                        pos.getY() + 1.25D + rand.nextDouble(), pos.getZ() + rand.nextDouble(),
                        0.0d, 0.0d, 0.0d);
            }
        }
        super.animateTick(stateIn, worldIn, pos, rand);
    }


    @Override
    public boolean canSurvive(BlockState blockState, IWorldReader reader, BlockPos pos){
        return reader.getBlockState(pos.below()).equals(ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState());
    }
}
