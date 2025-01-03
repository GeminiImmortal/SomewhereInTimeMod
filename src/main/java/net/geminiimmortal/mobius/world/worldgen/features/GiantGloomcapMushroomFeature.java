package net.geminiimmortal.mobius.world.worldgen.features;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;


public class GiantGloomcapMushroomFeature extends Feature<NoFeatureConfig> {

    private final Block stemBlock;
    private final Block capBlock;

    public GiantGloomcapMushroomFeature(Codec<NoFeatureConfig> codec, Block stemBlock, Block capBlock) {
        super(codec);
        this.stemBlock = stemBlock;
        this.capBlock = capBlock;
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {

        if(!isValidPosition(world, pos)) return false;

        BlockPos origin = pos;

        int stemHeight = 12 + random.nextInt(15);
        int capRadius = 4 + random.nextInt(6);


        for (int y = 0; y <= stemHeight + capRadius; y++) {
            if (!world.getBlockState(origin.above(y)).canBeReplacedByLogs(world, pos)) {
                return false;
            }
        }


        for (int y = 0; y < stemHeight; y++) {
            setBlock(world, origin.above(y), stemBlock.defaultBlockState());
        }


        for (int x = -capRadius; x <= capRadius; x++) {
            for (int z = -capRadius; z <= capRadius; z++) {
                int distance = x * x + z * z;
                if (distance <= capRadius * capRadius) {
                    setBlock(world, origin.above(stemHeight).offset(x, 0, z), capBlock.defaultBlockState());
                }
            }
        }


        for (int x = -capRadius + 1; x <= capRadius - 1; x++) {
            for (int z = -capRadius + 1; z <= capRadius - 1; z++) {
                int distance = x * x + z * z;
                if (distance <= (capRadius - 1) * (capRadius - 1)) {
                    setBlock(world, origin.above(stemHeight - 1).offset(x, 0, z), capBlock.defaultBlockState());
                }
            }
        }

        return true;
    }

    private boolean isValidPosition(ISeedReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).is(ModBlocks.SOUL_PODZOL.get());
    }
}

