package net.geminiimmortal.mobius.block.custom;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class AuroraGrassBlock extends SpreadableSnowyDirtBlock implements IGrowable {
    public AuroraGrassBlock(Properties p_i48388_1_) {
        super(p_i48388_1_);
    }

    public boolean isValidBonemealTarget(IBlockReader p_176473_1_, BlockPos p_176473_2_, BlockState p_176473_3_, boolean p_176473_4_) {
        return p_176473_1_.getBlockState(p_176473_2_.above()).isAir();
    }

    public boolean isBonemealSuccess(World p_180670_1_, Random p_180670_2_, BlockPos p_180670_3_, BlockState p_180670_4_) {
        return true;
    }

    public void performBonemeal(ServerWorld p_225535_1_, Random p_225535_2_, BlockPos p_225535_3_, BlockState p_225535_4_) {
        BlockPos lvt_5_1_ = p_225535_3_.above();
        BlockState lvt_6_1_ = Blocks.GRASS.defaultBlockState();

        label48:
        for(int lvt_7_1_ = 0; lvt_7_1_ < 128; ++lvt_7_1_) {
            BlockPos lvt_8_1_ = lvt_5_1_;

            for(int lvt_9_1_ = 0; lvt_9_1_ < lvt_7_1_ / 16; ++lvt_9_1_) {
                lvt_8_1_ = lvt_8_1_.offset(p_225535_2_.nextInt(3) - 1, (p_225535_2_.nextInt(3) - 1) * p_225535_2_.nextInt(3) / 2, p_225535_2_.nextInt(3) - 1);
                if (!p_225535_1_.getBlockState(lvt_8_1_.below()).is(this) || p_225535_1_.getBlockState(lvt_8_1_).isCollisionShapeFullBlock(p_225535_1_, lvt_8_1_)) {
                    continue label48;
                }
            }

            BlockState lvt_9_2_ = p_225535_1_.getBlockState(lvt_8_1_);
            if (lvt_9_2_.is(lvt_6_1_.getBlock()) && p_225535_2_.nextInt(10) == 0) {
                ((IGrowable)lvt_6_1_.getBlock()).performBonemeal(p_225535_1_, p_225535_2_, lvt_8_1_, lvt_9_2_);
            }

            if (lvt_9_2_.isAir()) {
                BlockState lvt_10_2_;
                if (p_225535_2_.nextInt(8) == 0) {
                    List<ConfiguredFeature<?, ?>> lvt_11_1_ = p_225535_1_.getBiome(lvt_8_1_).getGenerationSettings().getFlowerFeatures();
                    if (lvt_11_1_.isEmpty()) {
                        continue;
                    }

                    ConfiguredFeature<?, ?> lvt_12_1_ = (ConfiguredFeature)lvt_11_1_.get(0);
                    FlowersFeature lvt_13_1_ = (FlowersFeature)lvt_12_1_.feature;
                    lvt_10_2_ = lvt_13_1_.getRandomFlower(p_225535_2_, lvt_8_1_, lvt_12_1_.config());
                } else {
                    lvt_10_2_ = lvt_6_1_;
                }

                if (lvt_10_2_.canSurvive(p_225535_1_, lvt_8_1_)) {
                    p_225535_1_.setBlock(lvt_8_1_, lvt_10_2_, 3);
                }
            }
        }

    }

    @Override
    public void randomTick(BlockState p_225542_1_, ServerWorld p_225542_2_, BlockPos p_225542_3_, Random p_225542_4_) {
        if (!canBeGrass(p_225542_1_, p_225542_2_, p_225542_3_)) {
            if (!p_225542_2_.isAreaLoaded(p_225542_3_, 3)) {
                return;
            }

            p_225542_2_.setBlockAndUpdate(p_225542_3_, ModBlocks.AURORA_DIRT.get().defaultBlockState());
        } else if (p_225542_2_.getMaxLocalRawBrightness(p_225542_3_.above()) >= 9) {
            BlockState blockstate = this.defaultBlockState();

            for(int i = 0; i < 4; ++i) {
                BlockPos blockpos = p_225542_3_.offset(p_225542_4_.nextInt(3) - 1, p_225542_4_.nextInt(5) - 3, p_225542_4_.nextInt(3) - 1);
                if (p_225542_2_.getBlockState(blockpos).is(Blocks.DIRT) && canPropagate(blockstate, p_225542_2_, blockpos)) {
                    p_225542_2_.setBlockAndUpdate(blockpos, (BlockState)blockstate.setValue(SNOWY, p_225542_2_.getBlockState(blockpos.above()).is(Blocks.SNOW)));
                }
            }
        }

    }
}
