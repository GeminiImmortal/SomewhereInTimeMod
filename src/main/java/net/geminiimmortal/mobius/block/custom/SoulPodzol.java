package net.geminiimmortal.mobius.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class SoulPodzol extends GrassBlock implements IGrowable {
    public SoulPodzol(Properties p_i48388_1_) {
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
}

