package net.geminiimmortal.mobius.block.custom.flora;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.world.worldgen.feature.ModConfiguredFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class Gloomcap extends MushroomBlock {
    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);

    public Gloomcap(Properties p_i48440_1_) {
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
    public boolean canSurvive(BlockState blockState, IWorldReader reader, BlockPos pos){
        return (reader.getBlockState(pos.below()).equals(ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState())) || (reader.getBlockState(pos.below()).equals(ModBlocks.SOUL_PODZOL.get().defaultBlockState()));
    }

    @Override
    public boolean growMushroom(ServerWorld p_226940_1_, BlockPos p_226940_2_, BlockState p_226940_3_, Random p_226940_4_) {
        p_226940_1_.removeBlock(p_226940_2_, false);
        ConfiguredFeature configuredfeature;


        configuredfeature = ModConfiguredFeatures.CONFIGURED_GIANT_GLOOMCAP_FEATURE;


        if (configuredfeature.place(p_226940_1_, p_226940_1_.getChunkSource().getGenerator(), p_226940_4_, p_226940_2_)) {
            return true;
        } else {
            p_226940_1_.setBlock(p_226940_2_, p_226940_3_, 3);
            return false;
        }
    }

    @Override
    public boolean isValidBonemealTarget(IBlockReader p_176473_1_, BlockPos p_176473_2_, BlockState p_176473_3_, boolean p_176473_4_) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(World p_180670_1_, Random p_180670_2_, BlockPos p_180670_3_, BlockState p_180670_4_) {
        return (double)p_180670_2_.nextFloat() < 0.4;
    }

    @Override
    public void performBonemeal(ServerWorld p_225535_1_, Random p_225535_2_, BlockPos p_225535_3_, BlockState p_225535_4_) {
        this.growMushroom(p_225535_1_, p_225535_3_, p_225535_4_, p_225535_2_);
    }
}
