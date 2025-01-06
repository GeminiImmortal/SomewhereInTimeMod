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

public class StandingGloomcap extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 14.0, 30.0, 14.0);

    public StandingGloomcap(Properties p_i48440_1_) {
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
                worldIn.addParticle(ModParticles.SPORE_PARTICLE.get(), pos.getX() + rand.nextDouble(),
                        pos.getY() + 1.25D, pos.getZ() + rand.nextDouble(),
                        0.04d, 0.02d, 0.04d);
            }
        }
        super.animateTick(stateIn, worldIn, pos, rand);
    }


    @Override
    public boolean canSurvive(BlockState blockState, IWorldReader reader, BlockPos pos){
        return reader.getBlockState(pos.below()).equals(ModBlocks.SOUL_PODZOL.get().defaultBlockState());
    }

   /* @Override
    public boolean growMushroom(ServerWorld p_226940_1_, BlockPos p_226940_2_, BlockState p_226940_3_, Random p_226940_4_) {
        return false;
    }

    @Override
    public boolean isValidBonemealTarget(IBlockReader p_176473_1_, BlockPos p_176473_2_, BlockState p_176473_3_, boolean p_176473_4_) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(World p_180670_1_, Random p_180670_2_, BlockPos p_180670_3_, BlockState p_180670_4_) {
        return false;
    }

    @Override
    public void performBonemeal(ServerWorld p_225535_1_, Random p_225535_2_, BlockPos p_225535_3_, BlockState p_225535_4_) {

    }*/
}
