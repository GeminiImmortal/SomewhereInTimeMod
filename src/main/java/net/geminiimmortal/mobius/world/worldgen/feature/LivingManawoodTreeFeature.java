package net.geminiimmortal.mobius.world.worldgen.feature;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class LivingManawoodTreeFeature extends Feature<NoFeatureConfig> {
    public LivingManawoodTreeFeature(Codec<NoFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        int height = 8 + rand.nextInt(4); // Define your custom tree height
        BlockState log = ModBlocks.LIVING_MANAWOOD_LOG.get().defaultBlockState();
        BlockState leaves = ModBlocks.LIVING_MANAWOOD_LEAVES.get().defaultBlockState();

        // Create the 2x2 trunk
        for (int y = 0; y < height; y++) {
            world.setBlock(pos.offset(0, y, 0), log, 3);
            world.setBlock(pos.offset(1, y, 0), log, 3);
            world.setBlock(pos.offset(0, y, 1), log, 3);
            world.setBlock(pos.offset(1, y, 1), log, 3);
        }

        // Create the leaf canopy
        for (int dx = -2; dx <= 3; dx++) {
            for (int dz = -2; dz <= 3; dz++) {
                for (int dy = 0; dy < 3; dy++) {
                    if (rand.nextFloat() < 0.7) {
                        world.setBlock(pos.offset(dx, height + dy, dz), leaves, 3);
                    }
                }
            }
        }

        return true;
    }
}

