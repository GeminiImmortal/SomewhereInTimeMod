package net.geminiimmortal.mobius.world.worldgen.feature;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

import java.util.Random;

public class FloatingBlockFeature extends Feature<NoFeatureConfig> {
    public FloatingBlockFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        int x = pos.getX() + random.nextInt(16);
        int y = pos.getY() + 5 + random.nextInt(15);
        int z = pos.getZ() + random.nextInt(16);
        BlockPos floatingPos = new BlockPos(x, y, z);

        if (world.isEmptyBlock(floatingPos)) {
            BlockState block = ModBlocks.OBSECFUTORIA.get().defaultBlockState();
            world.setBlock(floatingPos, block, 3);
            return true;
        }
        return false;
    }
}
