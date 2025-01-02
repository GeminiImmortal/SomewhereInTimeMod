package net.geminiimmortal.mobius.world.worldgen.features;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class GloamthornTreeFeature extends Feature<NoFeatureConfig> {
    public GloamthornTreeFeature(Codec<NoFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        int height = 6 + rand.nextInt(2);
        BlockState log = ModBlocks.GLOAMTHORN_LOG.get().defaultBlockState();
        BlockState leaves = ModBlocks.GLOAMTHORN_BRAMBLE.get().defaultBlockState();

        for (int y = 0; y < height; y++) {
            world.setBlock(pos.offset(0, y, 0), log, 3);
        }

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

