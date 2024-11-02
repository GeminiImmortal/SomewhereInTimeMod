package net.geminiimmortal.mobius.world.worldgen.features;

import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
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
                    .decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE)
                    .decorated(Placement.COUNT_EXTRA.configured(
                            new AtSurfaceWithExtraConfig(2, 0.2f, 1)))
                    .decorated(Placement.CHANCE.configured(
                            new ChanceConfig(4))));

            base.add(() -> ModConfiguredFeatures.CONFIGURED_MANAWOOD_TREE
                    .decorated(Features.Placements.HEIGHTMAP_DOUBLE)
                    .decorated(Placement.COUNT_EXTRA.configured(
                            new AtSurfaceWithExtraConfig(1,0.4f, 2)))
                    .decorated(Placement.CHANCE.configured(
                            new ChanceConfig(2))));

        }
    }
}

