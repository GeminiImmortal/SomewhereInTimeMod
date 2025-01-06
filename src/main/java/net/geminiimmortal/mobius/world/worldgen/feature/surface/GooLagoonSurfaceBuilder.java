package net.geminiimmortal.mobius.world.worldgen.feature.surface;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.fluid.ModFluids;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;

public class GooLagoonSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {

    public GooLagoonSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
    }

    @Override
    public void apply(Random random, IChunk iChunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState fluidBlock, int seaLevel, long seed, SurfaceBuilderConfig config) {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        int chunkX = x & 15;
        int chunkZ = z & 15;

        // Replace the existing fluid in this biome
        for (int y = seaLevel - 20; y <= seaLevel + 5; y++) {
            mutablePos.set(chunkX, y, chunkZ);
            BlockState currentState = iChunk.getBlockState(mutablePos);

            // Check if the current block is the default fluid
            if (currentState.is(Blocks.WATER.getBlock())) {
                // Replace with your custom fluid
                iChunk.setBlockState(mutablePos, ModFluids.ECTOPLASM_BLOCK.get().defaultBlockState(), false);
            }
        }

        // Build the surface layers (topMaterial and underwaterMaterial)
        for (int y = height; y >= 0; y--) {
            mutablePos.set(chunkX, y, chunkZ);
            BlockState currentState = iChunk.getBlockState(mutablePos);

            if (currentState.is(ModBlocks.HEMATITE.get().getBlock())) {
                if (y >= seaLevel) {
                    iChunk.setBlockState(mutablePos, config.getTopMaterial(), false);
                } else if (y < seaLevel && y > seaLevel - 5) {
                    iChunk.setBlockState(mutablePos, config.getUnderwaterMaterial(), false);
                }
            }
        }
    }
}