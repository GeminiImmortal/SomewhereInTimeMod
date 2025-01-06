package net.geminiimmortal.mobius.world.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.LakesFeature;

public class GooLagoonFeature extends LakesFeature {
    public GooLagoonFeature(Codec<BlockStateFeatureConfig> config) {
        super(config);
    }
}
