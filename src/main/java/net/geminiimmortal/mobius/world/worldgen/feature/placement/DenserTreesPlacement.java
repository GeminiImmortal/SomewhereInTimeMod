package net.geminiimmortal.mobius.world.worldgen.feature.placement;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.placement.SimplePlacement;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DenserTreesPlacement extends SimplePlacement<FeatureSpreadConfig> {
    public DenserTreesPlacement(Codec<FeatureSpreadConfig> p_i232095_1_) {
        super(p_i232095_1_);
    }

    @Override
    protected Stream<BlockPos> place(Random random, FeatureSpreadConfig featureSpreadConfig, BlockPos blockPos) {
        return IntStream.range(0, featureSpreadConfig.count().sample(random)).mapToObj((p_242878_1_) -> {
            return blockPos;
        });
    }
}
