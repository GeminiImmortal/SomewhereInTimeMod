package net.geminiimmortal.mobius.world.worldgen.structure;

import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ModStructureGeneration {

    public static void generateStructures(final BiomeLoadingEvent event) {

        ResourceLocation draconicForelandsValid = ModBiomes.DRACONIC_FORELANDS.getId();
        ResourceLocation rollingExpanseValid = ModBiomes.ROLLING_EXPANSE.getId();
        ResourceLocation gooLagoonValid = ModBiomes.GOO_LAGOON.getId();
        ResourceLocation validPortal = Biomes.PLAINS.location();

        if(Objects.equals(event.getName(), draconicForelandsValid)) {
            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructures.MOLVAN_SETTLEMENT_A.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), rollingExpanseValid)) {
            List<Supplier<StructureFeature<?,?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructures.MOBIUS_VILLAGE.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), rollingExpanseValid)) {
            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructures.MOLVAN_SETTLEMENT_B.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), validPortal)) {
            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructures.MOBIUS_PORTAL.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), rollingExpanseValid)) {
            List<Supplier<StructureFeature<?,?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructures.IMPERIAL_WATCHTOWER.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), gooLagoonValid)) {
            List<Supplier<StructureFeature<?,?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructures.GOVERNOR_TOWER.get().configured(NoFeatureConfig.INSTANCE));
        }
    }
}

