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

import java.util.Random;

public class DraconicForelandsMountainSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
    private static final int OCTAVES = 5;
    private final PerlinNoiseGenerator baseNoise;
    private final PerlinNoiseGenerator detailNoise;
    private final SimplexNoiseGenerator domainWarpNoise;

    public DraconicForelandsMountainSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
        SharedSeedRandom sharedSeedRandom = new SharedSeedRandom(SeedBearer.getSeed());
        this.baseNoise = new PerlinNoiseGenerator(sharedSeedRandom, ImmutableList.of(0, 1, 2));
        this.detailNoise = new PerlinNoiseGenerator(sharedSeedRandom, ImmutableList.of(3, 4));
        this.domainWarpNoise = new SimplexNoiseGenerator(sharedSeedRandom);
    }

    @Override
    public void apply(Random rand, IChunk chunk, Biome biome, int x, int z, int startHeight,
                      double noiseVal, BlockState defaultBlock, BlockState topBlock,
                      int seaLevel, long seed, SurfaceBuilderConfig config) {

        BlockPos.Mutable pos = new BlockPos.Mutable();
        int worldX = chunk.getPos().getMinBlockX() + (x & 15);
        int worldZ = chunk.getPos().getMinBlockZ() + (z & 15);

        double height = calculateHillHeight(worldX, worldZ);
        double slope = calculateSlope(worldX, worldZ, height);

        for (int y = (int) height; y >= 0; y--) {
            pos.set(worldX, y, worldZ);

            if (y <= 5) {
                chunk.setBlockState(pos, Blocks.BEDROCK.defaultBlockState(), false);
                continue;
            }

            BlockState block = chooseHillBlock(y, height - y, slope);
            chunk.setBlockState(pos, block, false);
        }
    }

    private double calculateHillHeight(int x, int z) {
        // Domain warping for shape irregularity
        double warpX = domainWarpNoise.getValue(x * 0.001, z * 0.001) * 60;
        double warpZ = domainWarpNoise.getValue(z * 0.001, x * 0.001) * 60;

        // Sample larger area of base noise to reduce harsh cliffs
        double b1 = baseNoise.getValue((x + warpX - 2) * 0.002, (z + warpZ) * 0.002, true);
        double b2 = baseNoise.getValue((x + warpX + 2) * 0.002, (z + warpZ) * 0.002, true);
        double b3 = baseNoise.getValue((x + warpX) * 0.002, (z + warpZ - 2) * 0.002, true);
        double b4 = baseNoise.getValue((x + warpX) * 0.002, (z + warpZ + 2) * 0.002, true);
        double base = (b1 + b2 + b3 + b4) / 4.0;

        // Mid-frequency bumps
        double detail = detailNoise.getValue((x + warpX) * 0.01, (z + warpZ) * 0.01, true) * 5;

        // Preserve dramatic height, but soften edges
        double shaped = Math.pow(base, 2.1);  // Slightly increased power for craggy peaks
        shaped = shaped * (base < 0 ? -1 : 1);  // Restore sign after squaring

        // Height computation
        double height = 70 + shaped * 150 + detail;


        return MathHelper.clamp(height, 70, 200);
    }



    private double calculateSlope(int x, int z, double height) {
        double dx = calculateHillHeight(x + 1, z) - calculateHillHeight(x - 1, z);
        double dz = calculateHillHeight(x, z + 1) - calculateHillHeight(x, z - 1);
        return Math.sqrt(dx * dx + dz * dz) / 2.0;
    }

    private BlockState chooseHillBlock(int y, double depth, double slope) {
        // Top layer
        if (depth < 4) {
            if (y > 200) {
                return Blocks.SNOW_BLOCK.defaultBlockState();
            }
            return ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState();
        }

        // Upper slopes
        if (depth < 15) {
            return ModBlocks.BLOODSTONE.get().defaultBlockState();
        }

        // Steep slopes
        if (slope > 2.5) {
            return ModBlocks.HEMATITE.get().defaultBlockState();
        }

        // Base material
        return ModBlocks.HEMATITE.get().defaultBlockState();
    }
}