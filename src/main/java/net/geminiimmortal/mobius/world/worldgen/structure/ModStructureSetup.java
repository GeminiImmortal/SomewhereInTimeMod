package net.geminiimmortal.mobius.world.worldgen.structure;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.worldgen.structure.structures.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import java.util.HashMap;
import java.util.Map;

public class ModStructureSetup {

    // Registering structures via Deferred Register
    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, MobiusMod.MOD_ID);

    // Example of structure registration
    public static final RegistryObject<Structure<NoFeatureConfig>> MOLVAN_SETTLEMENT_A = STRUCTURES.register("molvan_settlement_a", () -> new MolvanSettlementA());
    public static final RegistryObject<Structure<NoFeatureConfig>> MOLVAN_SETTLEMENT_B = STRUCTURES.register("molvan_settlement_b", () -> new MolvanSettlementB());
    public static final RegistryObject<Structure<NoFeatureConfig>> IMPERIAL_WATCHTOWER = STRUCTURES.register("imperial_watchtower", () -> new ImperialWatchtower());
    public static final RegistryObject<Structure<NoFeatureConfig>> GOVERNOR_TOWER = STRUCTURES.register("governor_tower", () -> new GovernorTower());
    public static final RegistryObject<Structure<NoFeatureConfig>> MOBIUS_VILLAGE = STRUCTURES.register("mobius_village", () -> new MobiusVillage());
    public static final RegistryObject<Structure<NoFeatureConfig>> DRAGON_BONES = STRUCTURES.register("dragon_bones", () -> new DragonRibcage());
    public static final RegistryObject<Structure<NoFeatureConfig>> CELESTIAL_RUINS = STRUCTURES.register("celestial_ruins", () -> new CelestialRuins());
    public static final RegistryObject<Structure<NoFeatureConfig>> MOBIUS_PORTAL = STRUCTURES.register("mobius_portal", () -> new MobiusPortal());


    public static final class ConfiguredStructures {
        // Configure the structure with IFeatureConfig
        public static final StructureFeature<?, ?> MOLVAN_SETTLEMENT_A = ModStructureSetup.MOLVAN_SETTLEMENT_A.get().configured(IFeatureConfig.NONE);
        public static final StructureFeature<?, ?> MOLVAN_SETTLEMENT_B = ModStructureSetup.MOLVAN_SETTLEMENT_B.get().configured(IFeatureConfig.NONE);
        public static final StructureFeature<?, ?> IMPERIAL_WATCHTOWER = ModStructureSetup.IMPERIAL_WATCHTOWER.get().configured(IFeatureConfig.NONE);
        public static final StructureFeature<?, ?> GOVERNOR_TOWER = ModStructureSetup.GOVERNOR_TOWER.get().configured(IFeatureConfig.NONE);
        public static final StructureFeature<?, ?> MOBIUS_VILLAGE = ModStructureSetup.MOBIUS_VILLAGE.get().configured(IFeatureConfig.NONE);
        public static final StructureFeature<?, ?> DRAGON_BONES = ModStructureSetup.DRAGON_BONES.get().configured(IFeatureConfig.NONE);
        public static final StructureFeature<?, ?> CELESTIAL_RUINS = ModStructureSetup.CELESTIAL_RUINS.get().configured(IFeatureConfig.NONE);
        public static final StructureFeature<?, ?> MOBIUS_PORTAL = ModStructureSetup.MOBIUS_PORTAL.get().configured(IFeatureConfig.NONE);

    }

    // Register the structures to the world
    public static void registerStructures() {
        setupStructure(MOLVAN_SETTLEMENT_A.get(), new StructureSeparationSettings(24, 8, 276320045), false);
        setupStructure(MOLVAN_SETTLEMENT_B.get(), new StructureSeparationSettings(24, 8, 276321489), false);
        setupStructure(IMPERIAL_WATCHTOWER.get(), new StructureSeparationSettings(24, 8, 218590045), true);
        setupStructure(GOVERNOR_TOWER.get(), new StructureSeparationSettings(24, 8, 276357495), false);
        setupStructure(MOBIUS_VILLAGE.get(), new StructureSeparationSettings(24, 8, 221375889), true);
        setupStructure(DRAGON_BONES.get(), new StructureSeparationSettings(24, 8, 158390045), true);
        setupStructure(CELESTIAL_RUINS.get(), new StructureSeparationSettings(24, 8, 646357495), false);
        setupStructure(MOBIUS_PORTAL.get(), new StructureSeparationSettings(24, 8, 195837495), true);
    }

    // Register configured structures
    public static void registerConfiguredStructures() {
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(MobiusMod.MOD_ID, "molvan_settlement_a"), MOLVAN_SETTLEMENT_A.get().configured(IFeatureConfig.NONE));
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(MobiusMod.MOD_ID, "molvan_settlement_b"), MOLVAN_SETTLEMENT_B.get().configured(IFeatureConfig.NONE));
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(MobiusMod.MOD_ID, "imperial_watchtower"), IMPERIAL_WATCHTOWER.get().configured(IFeatureConfig.NONE));
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(MobiusMod.MOD_ID, "governor_tower"), GOVERNOR_TOWER.get().configured(IFeatureConfig.NONE));
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(MobiusMod.MOD_ID, "mobius_village"), MOBIUS_VILLAGE.get().configured(IFeatureConfig.NONE));
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(MobiusMod.MOD_ID, "dragon_bones"), DRAGON_BONES.get().configured(IFeatureConfig.NONE));
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(MobiusMod.MOD_ID, "celestial_ruins"), CELESTIAL_RUINS.get().configured(IFeatureConfig.NONE));
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(MobiusMod.MOD_ID, "mobius_portal"), MOBIUS_PORTAL.get().configured(IFeatureConfig.NONE));

    }

    // Adjust dimensional spacing logic for structures
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            // Skip if it's using a flat chunk generator and the Overworld dimension
            if (serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator && serverWorld.dimension().equals(World.OVERWORLD)) {
                return;
            }

            // Create the temporary map and add the structure
            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
            tempMap.put(ModStructureSetup.MOLVAN_SETTLEMENT_A.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureSetup.MOLVAN_SETTLEMENT_A.get()));
            tempMap.put(ModStructureSetup.MOLVAN_SETTLEMENT_B.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureSetup.MOLVAN_SETTLEMENT_B.get()));
            tempMap.put(ModStructureSetup.IMPERIAL_WATCHTOWER.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureSetup.IMPERIAL_WATCHTOWER.get()));
            tempMap.put(ModStructureSetup.GOVERNOR_TOWER.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureSetup.GOVERNOR_TOWER.get()));
            tempMap.put(ModStructureSetup.MOBIUS_VILLAGE.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureSetup.MOBIUS_VILLAGE.get()));
            tempMap.put(ModStructureSetup.DRAGON_BONES.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureSetup.DRAGON_BONES.get()));
            tempMap.put(ModStructureSetup.CELESTIAL_RUINS.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureSetup.CELESTIAL_RUINS.get()));
            tempMap.put(ModStructureSetup.MOBIUS_PORTAL.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructureSetup.MOBIUS_PORTAL.get()));
            serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }

    // Utility method to register and configure structures
    private static <F extends Structure<?>> void setupStructure(F structure, StructureSeparationSettings structureSeparationSettings, boolean transformSurroundingLand) {
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        // Optionally transform surrounding land based on the structure
        if (transformSurroundingLand) {
            Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder()
                    .addAll(Structure.NOISE_AFFECTING_FEATURES)
                    .add(structure)
                    .build();
        }

        // Update the structure separation settings in the defaults
        DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                .putAll(DimensionStructuresSettings.DEFAULTS)
                .put(structure, structureSeparationSettings)
                .build();
    }


}

