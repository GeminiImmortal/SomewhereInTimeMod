package net.geminiimmortal.mobius.world.worldgen.feature;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class ModOreGeneration {
    public static void generateOres(final BiomeLoadingEvent event) {


        for (OreType ore : OreType.values()) {
            OreFeatureConfig oreFeatureConfig = new OreFeatureConfig(
                    ModOreFeatureConfig.FillerBlockType.MOBIAN_HEMATITE,
                    ore.getBlock().get().getBlock().defaultBlockState(), ore.getMaxVeinSize());

            // bottomOffset -> minimum height for the ore
            // maximum -> minHeight + maximum = top level (the vertical expansion of the ore, it grows x levels from bottomOffset)
            // topOffset -> subtracted from the maximum to give actual top level
            // ore effectively exists from bottomOffset to (bottomOffset + maximum - topOffset)
            ConfiguredPlacement<TopSolidRangeConfig> configuredPlacement = Placement.RANGE.configured(
                    new TopSolidRangeConfig(ore.getMinHeight(), ore.getMinHeight(), ore.getMaxHeight()));

            ConfiguredFeature<?, ?> oreFeature = registerOreFeature(ore, oreFeatureConfig, configuredPlacement);

            event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, oreFeature);

        }

        for (HematiteIronOreType ore : HematiteIronOreType.values()) {
            OreFeatureConfig oreFeatureConfig = new OreFeatureConfig(
                    ModOreFeatureConfig.FillerBlockType.MOBIAN_HEMATITE,
                    ore.getBlock().get().getBlock().defaultBlockState(), ore.getMaxVeinSize());

            // bottomOffset -> minimum height for the ore
            // maximum -> minHeight + maximum = top level (the vertical expansion of the ore, it grows x levels from bottomOffset)
            // topOffset -> subtracted from the maximum to give actual top level
            // ore effectively exists from bottomOffset to (bottomOffset + maximum - topOffset)
            ConfiguredPlacement<TopSolidRangeConfig> configuredPlacement = Placement.RANGE.configured(
                    new TopSolidRangeConfig(ore.getMinHeight(), ore.getMinHeight(), ore.getMaxHeight()));

            ConfiguredFeature<?, ?> oreFeature = registerIronOreFeature(ore, oreFeatureConfig, configuredPlacement);

            event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, oreFeature);

        }

        for (BloodstoneGemOreType ore : BloodstoneGemOreType.values()) {
            OreFeatureConfig oreFeatureConfig = new OreFeatureConfig(
                    ModBloodstoneGemOreFeatureConfig.FillerBlockType.IS_BLOODSTONE,
                    ore.getBlock().get().getBlock().defaultBlockState(), ore.getMaxVeinSize());

            // bottomOffset -> minimum height for the ore
            // maximum -> minHeight + maximum = top level (the vertical expansion of the ore, it grows x levels from bottomOffset)
            // topOffset -> subtracted from the maximum to give actual top level
            // ore effectively exists from bottomOffset to (bottomOffset + maximum - topOffset)
            ConfiguredPlacement<TopSolidRangeConfig> configuredPlacement = Placement.RANGE.configured(
                    new TopSolidRangeConfig(ore.getMinHeight(), ore.getMinHeight(), ore.getMaxHeight()));

            ConfiguredFeature<?, ?> oreFeature = registerBloodstoneGemOreFeature(ore, oreFeatureConfig, configuredPlacement);

            event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, oreFeature);

        }
    }

    private static ConfiguredFeature<?, ?> registerOreFeature(OreType ore, OreFeatureConfig oreFeatureConfig,
                                                              ConfiguredPlacement configuredPlacement) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, ore.getBlock().get().getRegistryName(),
                Feature.ORE.configured(oreFeatureConfig).decorated(configuredPlacement)
                        .countRandom(1).count(ore.getMaxVeinSize()));
    }

    private static ConfiguredFeature<?, ?> registerIronOreFeature(HematiteIronOreType ore, OreFeatureConfig oreFeatureConfig,
                                                              ConfiguredPlacement configuredPlacement) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, ore.getBlock().get().getRegistryName(),
                Feature.ORE.configured(oreFeatureConfig).decorated(configuredPlacement)
                        .countRandom(1).count(ore.getMaxVeinSize()));
    }

    private static ConfiguredFeature<?, ?> registerBloodstoneGemOreFeature(BloodstoneGemOreType ore, OreFeatureConfig oreFeatureConfig,
                                                                  ConfiguredPlacement configuredPlacement) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, ore.getBlock().get().getRegistryName(),
                Feature.ORE.configured(oreFeatureConfig).decorated(configuredPlacement)
                        .countRandom(1).count(ore.getMaxVeinSize()));
    }
}
