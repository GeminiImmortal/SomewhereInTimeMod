package net.geminiimmortal.mobius.world.worldgen.feature;

import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.geminiimmortal.mobius.world.worldgen.feature.placement.DenserTreesPlacement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ModNoFeatureConfigGeneration {
    public static void generateNFC(final BiomeLoadingEvent event) {
        ResourceLocation forsakenThicket = ModBiomes.FORSAKEN_THICKET.getId();
        ResourceLocation mushroomForest = ModBiomes.MUSHROOM_FOREST.getId();
        ResourceLocation shatteredPlains = ModBiomes.SHATTERED_PLAINS.getId();
        ResourceLocation rollingExpanse = ModBiomes.ROLLING_EXPANSE.getId();

        if (Objects.equals(event.getName(), mushroomForest)) {
            List<Supplier<ConfiguredFeature<?, ?>>> base =
                    event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);

            base.add(() -> ModConfiguredFeatures.CONFIGURED_STANDING_GLOOMCAP_FEATURE
                    .decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE)
                    .decorated(DenserTreesPlacement.DARK_OAK_TREE.configured(
                            new NoPlacementConfig()))
                    .decorated(Placement.CHANCE.configured(
                            new ChanceConfig(1)))
                    .countRandom(10));

            base.add(() -> ModConfiguredFeatures.CONFIGURED_GIANT_GLOOMCAP_FEATURE
                    .decorated(Features.Placements.HEIGHTMAP_DOUBLE)
                    .decorated(DenserTreesPlacement.COUNT_EXTRA.configured(
                            new AtSurfaceWithExtraConfig(3, 0.7f, 1)))
                    .decorated(Placement.CHANCE.configured(
                            new ChanceConfig(2)))
                    .countRandom(5));
        }

        if (Objects.equals(event.getName(), forsakenThicket)) {
            List<Supplier<ConfiguredFeature<?, ?>>> base =
                    event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);

            base.add(() -> ModConfiguredFeatures.CONFIGURED_WILD_MANA_WART_FEATURE
                    .decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE)
                    .decorated(DenserTreesPlacement.DARK_OAK_TREE.configured(
                            new NoPlacementConfig()))
                    .decorated(Placement.CHANCE.configured(
                            new ChanceConfig(1)))
                    .countRandom(10));
        }

        if (Objects.equals(event.getName(), shatteredPlains)) {
            List<Supplier<ConfiguredFeature<?, ?>>> base =
                    event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);

            base.add(() -> ModConfiguredFeatures.CONFIGURED_FLOATING_BLOCK_FEATURE
                    .decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE)
                    .decorated(Placement.CHANCE.configured(
                            new ChanceConfig(1)))
                    .countRandom(10));
        }

        if (Objects.equals(event.getName(), rollingExpanse)) {
            List<Supplier<ConfiguredFeature<?, ?>>> base =
                    event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);

            base.add(() -> ModConfiguredFeatures.CONFIGURED_TALL_GRASS_CARPET_FEATURE);
        }
    }
}
