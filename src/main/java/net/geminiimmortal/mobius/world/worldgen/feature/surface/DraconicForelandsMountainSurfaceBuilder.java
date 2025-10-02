package net.geminiimmortal.mobius.world.worldgen.feature.surface;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;

import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.SimplexNoiseGenerator;

public class DraconicForelandsMountainSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {

    private final PerlinNoiseGenerator baseNoise;
    private final PerlinNoiseGenerator detailNoise;
    private final SimplexNoiseGenerator domainWarpNoise;

    public DraconicForelandsMountainSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
        SharedSeedRandom sharedSeedRandom = new SharedSeedRandom(123456789L);
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

        BlockState top = config.getTopMaterial().getBlockState();
        BlockState filler = config.getUnderMaterial();
        BlockState base = config.getUnderwaterMaterial();

        double height = calculateHillHeight(worldX, worldZ);
        double slope = calculateSlope(worldX, worldZ);
        int topY = (int) height;

        for (int y = topY; y >= 0; y--) {
            pos.set(worldX, y, worldZ);

            if (y <= 5) {
                chunk.setBlockState(pos, Blocks.BEDROCK.defaultBlockState(), false);
                continue;
            }

            int layerFromTop = topY - y;
            BlockState block;

            // Top layer with random variation
            if (layerFromTop == 0) {
                if (y > 200) {
                    block = Blocks.SNOW_BLOCK.defaultBlockState();
                } else {
                    int roll = rand.nextInt(10);
                    if (roll < 7) block = top;
                    else if (roll < 9) block = ModBlocks.BLOODSTONE.get().defaultBlockState();
                    else block = filler;
                }
            }
            // Dirt/filler layer
            else if (layerFromTop <= 2) {
                block = filler;
            }
            // Upper slopes
            else if (layerFromTop <= 15) {
                block = (slope > 3.5) ? base : ModBlocks.BLOODSTONE.get().defaultBlockState();
            }
            // Deep base
            else {
                block = base;
            }

            chunk.setBlockState(pos, block, false);
        }
    }

    private double calculateHillHeight(int x, int z) {
        double warpX = domainWarpNoise.getValue(x * 0.001, z * 0.001) * 10;
        double warpZ = domainWarpNoise.getValue(z * 0.001, x * 0.001) * 10;

        double b1 = baseNoise.getValue((x + warpX - 2) * 0.002, (z + warpZ) * 0.002, true);
        double b2 = baseNoise.getValue((x + warpX + 2) * 0.002, (z + warpZ) * 0.002, true);
        double b3 = baseNoise.getValue((x + warpX) * 0.002, (z + warpZ - 2) * 0.002, true);
        double b4 = baseNoise.getValue((x + warpX) * 0.002, (z + warpZ + 2) * 0.002, true);
        double base = (b1 + b2 + b3 + b4) / 4.0;

        double detail = detailNoise.getValue((x + warpX) * 0.01, (z + warpZ) * 0.01, true) * 2;

        double shaped = Math.pow(base, 2.1);
        shaped = shaped * (base < 0 ? -1 : 1);

        return MathHelper.clamp(63 + shaped + detail, 63, 150);
    }

    private double calculateSlope(int x, int z) {
        double dx = MathHelper.clamp(calculateHillHeight(x + 1, z) - calculateHillHeight(x - 1, z), -3, 3) * 0.5;
        double dz = MathHelper.clamp(calculateHillHeight(x, z + 1) - calculateHillHeight(x, z - 1), -3, 3) * 0.5;
        return Math.sqrt(dx * dx + dz * dz);
    }
}