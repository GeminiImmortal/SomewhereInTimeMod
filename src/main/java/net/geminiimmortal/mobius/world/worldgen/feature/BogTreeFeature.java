package net.geminiimmortal.mobius.world.worldgen.feature;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class BogTreeFeature extends Feature<BaseTreeFeatureConfig> {

    public BogTreeFeature(Codec<BaseTreeFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, BaseTreeFeatureConfig config) {
        if (!world.getBlockState(pos.below()).is(ModBlocks.AURORA_GRASS_BLOCK.get()) && !world.getBlockState(pos.below()).is(ModBlocks.AURORA_DIRT.get())) {
            return false;
        }

        // Generate the trunk
        for (int y = 0; y < 6; y++) {
            world.setBlock(pos.above(y), config.trunkProvider.getState(rand, pos), 3);
        }

        // Generate the canopy
        BlockPos canopyBase = pos.above(6);
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (Math.abs(dx) + Math.abs(dz) < 3) {
                    world.setBlock(canopyBase.offset(dx, 0, dz), config.leavesProvider.getState(rand, pos), 3);
                }
            }
        }

        // Generate roots
        for (int i = 0; i < 3; i++) { // 3 roots
            BlockPos rootStart = pos.offset(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);
            for (int j = 0; j < 3; j++) { // Root length
                BlockPos rootPos = rootStart.below(j);
                world.setBlock(rootPos, config.trunkProvider.getState(rand, pos), 3);
            }
        }

        return true;
    }
}

