package net.geminiimmortal.mobius.world.worldgen.feature;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class BoulderFeature extends Feature<NoFeatureConfig> {

    private final BlockState blockToPlace;

    public BoulderFeature(Codec<NoFeatureConfig> codec, BlockState blockToPlace) {
        super(codec);
        this.blockToPlace = blockToPlace;
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        // Find the ground surface (skip air blocks)
        while (pos.getY() > 3 && world.isEmptyBlock(pos)) {
            pos = pos.below();
        }

        // Only place on grass or dirt-like blocks
        BlockState ground = world.getBlockState(pos);
        if (ground.getBlock() != ModBlocks.AURORA_GRASS_BLOCK.get() && ground.getBlock() != ModBlocks.AURORA_DIRT.get() && ground.getBlock() != Blocks.TALL_GRASS) {
            return false;
        }

        // Random radius (1 to 3 blocks)
        int radius = 1 + random.nextInt(2); // [1, 2]
        int height = 1 + random.nextInt(2); // [1, 2]

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = 0; dy <= height; dy++) {
                    double distance = MathHelper.sqrt(dx * dx + dz * dz + dy * dy * 0.5);
                    if (distance <= radius + 0.5) {
                        BlockPos placePos = pos.offset(dx, dy, dz);
                        if (world.isEmptyBlock(placePos) || world.getBlockState(placePos).getMaterial().isReplaceable()) {
                            world.setBlock(placePos, blockToPlace, 2);
                        }
                    }
                }
            }
        }

        return true;
    }
}

