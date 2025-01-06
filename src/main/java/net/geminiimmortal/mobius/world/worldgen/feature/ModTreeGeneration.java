package net.geminiimmortal.mobius.world.worldgen.feature;

import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.geminiimmortal.mobius.world.worldgen.feature.placement.DenserTreesPlacement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.*;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ModTreeGeneration {

    public static void generateTrees(final BiomeLoadingEvent event) {
        ResourceLocation valid = ModBiomes.FORSAKEN_THICKET.getId();
        ResourceLocation mushroomForest = ModBiomes.MUSHROOM_FOREST.getId();

        if(Objects.equals(event.getName(), valid)) {
            List<Supplier<ConfiguredFeature<?, ?>>> base =
                    event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);

            base.add(() -> ModConfiguredFeatures.CONFIGURED_LIVING_MANAWOOD_TREE_FEATURE
                    .decorated(Features.Placements.HEIGHTMAP)
                    .decorated(Placement.COUNT_EXTRA.configured(
                            new AtSurfaceWithExtraConfig(0, 0.3f, 1)))
                    .decorated(Placement.CHANCE.configured(
                            new ChanceConfig(50))));

            base.add(() -> ModConfiguredFeatures.CONFIGURED_MARROWOOD_TREE
                    .decorated(Features.Placements.HEIGHTMAP_DOUBLE)
                    .decorated(DenserTreesPlacement.COUNT_EXTRA.configured(
                            new AtSurfaceWithExtraConfig(3, 0.7f, 1)))
                    .decorated(Placement.CHANCE.configured(
                            new ChanceConfig(2)))
                    .countRandom(5));

            base.add(() -> ModConfiguredFeatures.CONFIGURED_MANAWOOD_TREE
                    .decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE)
                    .decorated(DenserTreesPlacement.DARK_OAK_TREE.configured(
                            new NoPlacementConfig()))
                    .decorated(Placement.CHANCE.configured(
                            new ChanceConfig(2)))
                    .countRandom(7));



        }

        if(Objects.equals(event.getName(), mushroomForest)) {
            List<Supplier<ConfiguredFeature<?, ?>>> base =
                    event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);

            base.add(() -> ModConfiguredFeatures.CONFIGURED_GLOAMTHORN_TREE
                    .decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE)
                    .decorated(DenserTreesPlacement.DARK_OAK_TREE.configured(
                            new NoPlacementConfig()))
                    .decorated(Placement.CHANCE.configured(
                            new ChanceConfig(2)))
                    .countRandom(6));
        }


    }
}

