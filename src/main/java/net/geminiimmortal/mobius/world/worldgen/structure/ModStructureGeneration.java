package net.geminiimmortal.mobius.world.worldgen.structure;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModStructureGeneration {

    public static void generateStructures(final BiomeLoadingEvent event) {

        ResourceLocation draconicForelandsValid = ModBiomes.DRACONIC_FORELANDS.getId();
        ResourceLocation rollingExpanseValid = ModBiomes.ROLLING_EXPANSE.getId();
        ResourceLocation gooLagoonValid = ModBiomes.GOO_LAGOON.getId();
        ResourceLocation forsakenThicketValid = ModBiomes.FORSAKEN_THICKET.getId();
        ResourceLocation validPortal = Biomes.PLAINS.location();

        if(Objects.equals(event.getName(), draconicForelandsValid)) {
            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructureSetup.MOLVAN_SETTLEMENT_A.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), rollingExpanseValid)) {
            List<Supplier<StructureFeature<?,?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructureSetup.MOBIUS_VILLAGE.get().configured(NoFeatureConfig.INSTANCE));
            structures.add(() -> ModStructureSetup.LUMBER_MILL_CAMP.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), rollingExpanseValid)) {
            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructureSetup.MOLVAN_SETTLEMENT_B.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), validPortal)) {
            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructureSetup.MOBIUS_PORTAL.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), rollingExpanseValid)) {
            List<Supplier<StructureFeature<?,?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructureSetup.IMPERIAL_WATCHTOWER.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), rollingExpanseValid)) {
            List<Supplier<StructureFeature<?,?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructureSetup.SMUGGLER_CAMP.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), gooLagoonValid)) {
            List<Supplier<StructureFeature<?,?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructureSetup.GOVERNOR_TOWER.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), draconicForelandsValid)) {
            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructureSetup.DRAGON_BONES.get().configured(NoFeatureConfig.INSTANCE));
        }

        if(Objects.equals(event.getName(), forsakenThicketValid)) {
            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
            structures.add(() -> ModStructureSetup.CELESTIAL_RUINS.get().configured(NoFeatureConfig.INSTANCE));
        }
    }

    public static void setStructureSpawns (final StructureSpawnListGatherEvent event) {
        if(event.getStructure().equals(ModStructureSetup.IMPERIAL_WATCHTOWER.get())) {
            List<MobSpawnInfo.Spawners> spawners = new ArrayList<>();
            spawners.add(new MobSpawnInfo.Spawners(ModEntityTypes.FOOTMAN.get(), 100, 6, 9));
            event.addEntitySpawns(EntityClassification.MONSTER, spawners);
        }
        if(event.getStructure().equals(ModStructureSetup.GOVERNOR_TOWER.get())) {
            MobSpawnInfo.Spawners footmanSpawn = new MobSpawnInfo.Spawners(ModEntityTypes.FOOTMAN.get(), 75, 6, 9);
            MobSpawnInfo.Spawners diamondGolemSpawn = new MobSpawnInfo.Spawners(ModEntityTypes.DIAMOND_GOLEM.get(), 10, 2, 4);
            MobSpawnInfo.Spawners heartGolemSpawn = new MobSpawnInfo.Spawners(ModEntityTypes.HEART_GOLEM.get(), 2, 1, 3);
            MobSpawnInfo.Spawners clubGolemSpawn = new MobSpawnInfo.Spawners(ModEntityTypes.CLUB_GOLEM.get(), 8, 2, 4);
            MobSpawnInfo.Spawners spadeGolemSpawn = new MobSpawnInfo.Spawners(ModEntityTypes.SPADE_GOLEM.get(), 10, 1, 1);
            List<MobSpawnInfo.Spawners> spawners = new ArrayList<>();
            spawners.add(footmanSpawn);
            MobSpawnInfo spawnInfo = new MobSpawnInfo.Builder().creatureGenerationProbability(0.001f).addMobCharge(ModEntityTypes.FOOTMAN.get(), 1000, 900).addSpawn(EntityClassification.MONSTER, spawners.stream().findAny().get()).build();
            event.addEntitySpawns(EntityClassification.MONSTER, spawnInfo.getMobs(EntityClassification.MONSTER));
        }

    }
}

