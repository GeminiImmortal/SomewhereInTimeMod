package net.geminiimmortal.mobius.world.worldgen.feature;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class ClusteredWildManaWartFeature extends Feature<NoFeatureConfig> {

    private final BlockState mushroomBlock;

    public ClusteredWildManaWartFeature(Codec<NoFeatureConfig> configCodec, BlockState mushroomBlock) {
        super(configCodec);
        this.mushroomBlock = mushroomBlock;
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        // Check if the ground is suitable for mushrooms
        if (world.getBlockState(pos.below()).isAir()) {
            return false;
        }

        int clusterSize = MathHelper.nextInt(random, 4, 8); // Cluster size (4 to 8 mushrooms)
        boolean placedAny = false;

        for (int i = 0; i < clusterSize; i++) {
            // Random offset within a 3x3 area
            int offsetX = random.nextInt(7) - 3;
            int offsetZ = random.nextInt(7) - 3;
            BlockPos mushroomPos = pos.offset(offsetX, 0, offsetZ);



            // Check if the position is valid
            if (isValidPosition(world, mushroomPos)) {
                world.setBlock(mushroomPos, mushroomBlock, 3); // Place the mushroom
                placedAny = true;

            }
        }

        return placedAny;
    }

    private boolean isValidPosition(ISeedReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).is(ModBlocks.AURORA_GRASS_BLOCK.get());
    }
}

