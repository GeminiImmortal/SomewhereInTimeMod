package net.geminiimmortal.mobius.world.worldgen.feature;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;

public class ObsidianCappedMountainFeature extends Feature<IFeatureConfig> {

    public ObsidianCappedMountainFeature(Codec<IFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, IFeatureConfig config) {
        int baseRadius = 10 + rand.nextInt(15);  // Random radius for the mountain base
        int height = 40 + rand.nextInt(20);  // Random height for the mountain

        // Generate the stone part of the mountain
        for (int y = 0; y < height; y++) {
            int radius = baseRadius - (y / 2);  // Taper the radius as it goes up
            generateLayer(world, pos.offset(0, y, 0), radius, ModBlocks.HEMATITE.get().defaultBlockState());
        }

        // Add obsidian cap at the peak
        generateLayer(world, pos.offset(0, height, 0), 3, Blocks.OBSIDIAN.defaultBlockState());
        return true;
    }

    // Helper function to generate a circular layer of blocks
    private void generateLayer(ISeedReader world, BlockPos pos, int radius, BlockState block) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz <= radius * radius) {
                    BlockPos targetPos = pos.offset(dx, 0, dz);
                    world.setBlock(targetPos, block, 2);
                }
            }
        }
    }
}

