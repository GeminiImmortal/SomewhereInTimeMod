package net.geminiimmortal.mobius.world.worldgen.biome;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class LakeGenHelper extends SurfaceBuilder<SurfaceBuilderConfig> {

    public LakeGenHelper(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
    }

    @Override
    public void apply(Random random, IChunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState fluidBlock, int seaLevel, long seed, SurfaceBuilderConfig config) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();


        for (int y = height; y >= 0; --y) {
            blockpos$mutable.set(x & 15, y, z & 15);
            BlockState currentBlock = chunk.getBlockState(blockpos$mutable);

            if (y < seaLevel) {

                if (currentBlock.isAir()) {
                    chunk.setBlockState(blockpos$mutable, Blocks.WATER.defaultBlockState(), false);
                }
            } else if (y == seaLevel) {

                chunk.setBlockState(blockpos$mutable, Blocks.AIR.defaultBlockState(), false);
            } else if (y < seaLevel + 5) {

                chunk.setBlockState(blockpos$mutable, ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState(), false);
            } else {

                chunk.setBlockState(blockpos$mutable, ModBlocks.AURORA_DIRT.get().defaultBlockState(), false);
            }
        }
    }


    public static final SurfaceBuilderConfig LAKE_SURFACE_CONFIG = new SurfaceBuilderConfig(
            Blocks.WATER.defaultBlockState(),
            ModBlocks.AURORA_DIRT.get().defaultBlockState(),
            Blocks.GRAVEL.defaultBlockState()
    );

}

