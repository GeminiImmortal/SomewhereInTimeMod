package net.geminiimmortal.mobius.world.worldgen.biome;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.WorldGenRegion;

import java.util.Random;

public class LakeGenHelper extends SurfaceBuilder<SurfaceBuilderConfig> {

    public LakeGenHelper(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
    }

    @Override
    public void apply(Random random, IChunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState fluidBlock, int seaLevel, long seed, SurfaceBuilderConfig config) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        // Set dirt and grass for land area above sea level
        for (int y = height; y >= 0; --y) {
            blockpos$mutable.set(x & 15, y, z & 15);
            BlockState currentBlock = chunk.getBlockState(blockpos$mutable);

            if (y < seaLevel) {
                // Place water only below sea level
                if (currentBlock.isAir()) {
                    chunk.setBlockState(blockpos$mutable, Blocks.WATER.defaultBlockState(), false);
                }
            } else if (y == seaLevel) {
                // Place sand or grass at sea level, not water
                chunk.setBlockState(blockpos$mutable, Blocks.AIR.defaultBlockState(), false); // You can also use sand or another block
            } else if (y < seaLevel + 5) {
                // Grass or sand just above sea level
                chunk.setBlockState(blockpos$mutable, Blocks.GRASS_BLOCK.defaultBlockState(), false);
            } else {
                // Dirt below the grass layer
                chunk.setBlockState(blockpos$mutable, Blocks.DIRT.defaultBlockState(), false);
            }
        }
    }


    public static final SurfaceBuilderConfig LAKE_SURFACE_CONFIG = new SurfaceBuilderConfig(
            Blocks.WATER.defaultBlockState(),  // Surface block (Water)
            Blocks.DIRT.defaultBlockState(),   // Subsurface block (Dirt)
            Blocks.GRAVEL.defaultBlockState()  // Lower layer block (Gravel or Stone)
    );

}

