package net.geminiimmortal.mobius.world.worldgen.structure;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class ModStructureGeneration {

    public static void generateStructures(final BiomeLoadingEvent event) {

        ResourceLocation valid = ModBiomes.DRACONIC_FORELANDS.getId();
        ResourceLocation validTower = ModBiomes.ROLLING_EXPANSE.getId();
        ResourceLocation validPortal = Biomes.PLAINS.location();

        if(Objects.equals(event.getName(), valid)) {
            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructures.MOLVAN_SETTLEMENT_A.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), validTower)) {
            List<Supplier<StructureFeature<?,?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructures.MOBIUS_VILLAGE.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), validTower)) {
            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructures.MOLVAN_SETTLEMENT_B.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), validPortal)) {
            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructures.MOBIUS_PORTAL.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), validTower)) {
            List<Supplier<StructureFeature<?,?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructures.IMPERIAL_WATCHTOWER.get().configured(NoFeatureConfig.INSTANCE));
        }
    }
}

