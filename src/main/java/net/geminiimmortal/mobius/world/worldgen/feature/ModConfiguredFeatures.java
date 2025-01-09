package net.geminiimmortal.mobius.world.worldgen.feature;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.fluid.ModFluids;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.DarkOakFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.SpruceFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.GiantTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

public class ModConfiguredFeatures {

    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> CONFIGURED_MARROWOOD_TREE =
            register("marrowood_tree", Feature.TREE.configured(
            new BaseTreeFeatureConfig.Builder(
                    new SimpleBlockStateProvider(ModBlocks.MARROWOOD_LOG.get().defaultBlockState()),
                    new SimpleBlockStateProvider(ModBlocks.BONE_LEAVES.get().defaultBlockState()),
                    new BlobFoliagePlacer(FeatureSpread.of(1, 2), FeatureSpread.of(0, 1), 3),
                    new StraightTrunkPlacer(6, 4, 3),
                    new TwoLayerFeature(1, 0, 1)
            ).ignoreVines().build()));

    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> CONFIGURED_LIVING_MANAWOOD_TREE_FEATURE =
            register("living_manawood_tree", LivingManawoodTreeFeature.TREE.configured(
                    (new BaseTreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(ModBlocks.LIVING_MANAWOOD_LOG.get().defaultBlockState()),
                            new SimpleBlockStateProvider(ModBlocks.LIVING_MANAWOOD_LEAVES.get().defaultBlockState()),
                            new DarkOakFoliagePlacer(FeatureSpread.fixed(0), FeatureSpread.fixed(1)),
                            new FancyTrunkPlacer(10, 3, 5),
                            new TwoLayerFeature(1, 0, 1)))
                            .build()));

    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> CONFIGURED_MANAWOOD_TREE =
            register("manawood_tree", Feature.TREE.configured(
                    (new BaseTreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(ModBlocks.MANAWOOD_LOG.get().defaultBlockState()),
                            new SimpleBlockStateProvider(ModBlocks.MANAWOOD_LEAVES.get().defaultBlockState()),
                            new SpruceFoliagePlacer(FeatureSpread.of(2, 4), FeatureSpread.fixed(1), FeatureSpread.fixed(6)),
                            new GiantTrunkPlacer(12, 3, 5),
                            new TwoLayerFeature(1, 0, 1)))
                            .build()));

    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> CONFIGURED_GLOAMTHORN_TREE =
            register("gloamthorn_tree", GloamthornTreeFeature.TREE.configured(
                    (new BaseTreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(ModBlocks.GLOAMTHORN_LOG.get().defaultBlockState()),
                            new SimpleBlockStateProvider(ModBlocks.GLOAMTHORN_BRAMBLE.get().defaultBlockState()),
                            new DarkOakFoliagePlacer(FeatureSpread.fixed(0), FeatureSpread.fixed(1)),
                            new FancyTrunkPlacer(6, 3, 4),
                            new TwoLayerFeature(1, 2, 1)))
                            .build()));

    public static final ConfiguredFeature<NoFeatureConfig, ?> CONFIGURED_STANDING_GLOOMCAP_FEATURE =
            register("standing_gloomcap",
                    ModFeatures.CLUSTERED_STANDING_GLOOMCAP_FEATURE.get().configured(new NoFeatureConfig()));

    public static final ConfiguredFeature<NoFeatureConfig, ?> CONFIGURED_WILD_MANA_WART_FEATURE =
            register("wild_mana_wart",
                    ModFeatures.CLUSTERED_WILD_MANA_WART_FEATURE.get().configured(new NoFeatureConfig()));

    public static final ConfiguredFeature<NoFeatureConfig, ?> CONFIGURED_GIANT_GLOOMCAP_FEATURE =
            register("giant_gloomcap",
                    ModFeatures.GIANT_GLOOMCAP_MUSHROOM_FEATURE.get().configured(new NoFeatureConfig()));


    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key,
                                                                                 ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, configuredFeature);
    }
}




