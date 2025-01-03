package net.geminiimmortal.mobius.world.worldgen.features;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class ModFeatures {
    public static final Feature<NoFeatureConfig> CLUSTERED_STANDING_GLOOMCAP_FEATURE =
            new ClusteredStandingGloomcapMushroomFeature(NoFeatureConfig.CODEC, ModBlocks.STANDING_GLOOMCAP.get().defaultBlockState());
}

