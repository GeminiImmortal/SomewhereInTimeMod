package net.geminiimmortal.mobius.world.worldgen.feature.surface;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.world.dimension.SeedBearer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Arrays;
import java.util.Random;

public class DraconicFoothillsSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
    private static final int SMOOTH_RADIUS = 2;
    private final PerlinNoiseGenerator baseNoise;
    private final PerlinNoiseGenerator detailNoise;
    private final SimplexNoiseGenerator warpNoise;

    public DraconicFoothillsSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
        SharedSeedRandom random = new SharedSeedRandom(SeedBearer.getSeed());
        this.baseNoise = new PerlinNoiseGenerator(random, ImmutableList.of(20, 21));
        this.detailNoise = new PerlinNoiseGenerator(random, ImmutableList.of(22, 23));
        this.warpNoise = new SimplexNoiseGenerator(random);
    }

    @Override
    public void apply(Random rand, IChunk chunk, Biome biome, int x, int z, int startHeight,
                      double noiseVal, BlockState defaultBlock, BlockState topBlock,
                      int seaLevel, long seed, SurfaceBuilderConfig config) {

        BlockPos.Mutable pos = new BlockPos.Mutable();
        int worldX = chunk.getPos().getMinBlockX() + (x & 15);
        int worldZ = chunk.getPos().getMinBlockZ() + (z & 15);

        // Get smoothed height from 5x5 area
        double height = getSmoothedHeight(worldX, worldZ);
        double slope = calculateGentleSlope(worldX, worldZ, height);

        // Build terrain from top down
        for (int y = (int)height; y >= 0; y--) {
            pos.set(x, y, z);

            if (y <= 5) {
                chunk.setBlockState(pos, Blocks.BEDROCK.defaultBlockState(), false);
                continue;
            }

            BlockState block = getTransitionalBlock(y, height - y, slope, worldX, worldZ);
            chunk.setBlockState(pos, block, false);
        }
    }

    private double getSmoothedHeight(int x, int z) {
        // Sample 5x5 grid of heights
        double[][] heights = new double[5][5];
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                heights[dx+2][dz+2] = calculateBaseHeight(x + dx, z + dz);
            }
        }

        // Apply weighted smoothing
        double heightSum = 0;
        double weightSum = 0;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                double weight = 1.0 / (1 + dx*dx + dz*dz); // Inverse square weighting
                heightSum += weight * heights[dx+2][dz+2];
                weightSum += weight;
            }
        }
        return heightSum / weightSum;
    }

    private double calculateBaseHeight(int x, int z) {
        // Gentle warping for organic shapes
        double warpX = warpNoise.getValue(x * 0.0008, z * 0.0008) * 25;
        double warpZ = warpNoise.getValue(z * 0.0008, x * 0.0008) * 25;



        // Subtle details
        double detail = detailNoise.getValue(x * 0.02, z * 0.02, true) * 2;

        double base = baseNoise.getValue((x + warpX) * 0.0008, (z + warpZ) * 0.0008, true);
        double steepnessFactor = MathHelper.clamp((base + 0.4) * 1.3, 0.85, 1.25); // amplify slope gradually
        return 60 + Math.pow(Math.abs(base), 1.5) * 70 * steepnessFactor + detail;

    }

    private double calculateGentleSlope(int x, int z, double height) {
        // Check slopes in 4 cardinal directions
        double[] slopes = new double[4];
        slopes[0] = Math.abs(height - calculateBaseHeight(x + 3, z)) / 3;
        slopes[1] = Math.abs(height - calculateBaseHeight(x - 3, z)) / 3;
        slopes[2] = Math.abs(height - calculateBaseHeight(x, z + 3)) / 3;
        slopes[3] = Math.abs(height - calculateBaseHeight(x, z - 3)) / 3;

        // Return average slope (capped at 1.0)
        return MathHelper.clamp(Arrays.stream(slopes).average().orElse(0), 0, 1.0);
    }

    private BlockState getTransitionalBlock(int y, double depth, double slope, int x, int z) {
        double noise = detailNoise.getValue(x * 0.1, z * 0.1, true);

        // Surface layers
        if (depth < 4) {
            if (y > 100 && noise > 0.6) {
                return ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState();
            }
            return ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState();
        }

        // Upper slopes
        if (depth < 15) {
            if (slope > 0.7) {
                return ModBlocks.AURORA_DIRT.get().defaultBlockState();
            }
            return ModBlocks.AURORA_DIRT.get().defaultBlockState();
        }

        // Transition layer
        if (depth < 40) {
            return ModBlocks.AURORA_DIRT.get().defaultBlockState();
        }

        // Base layer
        return ModBlocks.HEMATITE.get().defaultBlockState();
    }
}