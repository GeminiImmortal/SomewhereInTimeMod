package net.geminiimmortal.mobius.world.worldgen.feature;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.FaestagEntity;
import net.geminiimmortal.mobius.entity.goals.util.TeleportUtil;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class MysticTearFeature extends Feature<NoFeatureConfig> {

    private final BlockState blockToPlace;

    public MysticTearFeature(Codec<NoFeatureConfig> codec, BlockState blockToPlace) {
        super(codec);
        this.blockToPlace = blockToPlace;
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        while (pos.getY() > 3 && world.isEmptyBlock(pos)) {
            pos = pos.below();
        }

        BlockState ground = world.getBlockState(pos);
        if (ground.getBlock() != ModBlocks.AURORA_GRASS_BLOCK.get() && ground.getBlock() != ModBlocks.AURORA_DIRT.get()) {
            return false;
        }
        BlockPos spawnPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());

        world.setBlock(spawnPos, ModBlocks.MYSTIC_TEAR.get().defaultBlockState(), 3);


        return true;
    }
}

