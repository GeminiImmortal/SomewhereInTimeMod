package net.geminiimmortal.mobius.world.worldgen.feature;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class TallGrassCarpetFeature extends Feature<NoFeatureConfig> {
    public TallGrassCarpetFeature(Codec<NoFeatureConfig> codec) {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos origin, NoFeatureConfig config) {
        ChunkPos chunkPos = new ChunkPos(origin);

        // Iterate over every (x, z) in this chunk
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int x = chunkPos.getMinBlockX() + dx;
                int z = chunkPos.getMinBlockZ() + dz;

                BlockPos surfacePos = world.getHeightmapPos(Heightmap.Type.WORLD_SURFACE, new BlockPos(x, 0, z)).below();

                if (world.getBlockState(surfacePos).getBlock() == ModBlocks.AURORA_GRASS_BLOCK.get()) {
                    BlockPos above = surfacePos.above();

                    if (world.isEmptyBlock(above) && Blocks.TALL_GRASS.defaultBlockState().canSurvive(world, above)) {
                        world.setBlock(above, Blocks.TALL_GRASS.defaultBlockState(), 2);
                    }
                }
            }
        }

        return true;
    }
}



