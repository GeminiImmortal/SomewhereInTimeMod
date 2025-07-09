package net.geminiimmortal.mobius.world.worldgen.structure;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.worldgen.structure.structures.*;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class ModStructures {
    public static final DeferredRegister<Structure<?>> STRUCTURES =
            DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, MobiusMod.MOD_ID);

    public static final RegistryObject<Structure<NoFeatureConfig>> MOLVAN_SETTLEMENT_A =
            STRUCTURES.register("molvan_settlement_a", MolvanSettlementA::new);

    public static final RegistryObject<Structure<NoFeatureConfig>> MOBIUS_PORTAL =
            STRUCTURES.register("mobius_portal", MobiusPortal::new);

    public static final RegistryObject<Structure<NoFeatureConfig>> MOBIUS_VILLAGE =
            STRUCTURES.register("mobius_village", MobiusVillage::new);

    public static final RegistryObject<Structure<NoFeatureConfig>> IMPERIAL_WATCHTOWER =
            STRUCTURES.register("imperial_watchtower", ImperialWatchtower::new);

    public static final RegistryObject<Structure<NoFeatureConfig>> MOLVAN_SETTLEMENT_B =
            STRUCTURES.register("molvan_settlement_b", MolvanSettlementB::new);

    public static final RegistryObject<Structure<NoFeatureConfig>> GOVERNOR_TOWER =
            STRUCTURES.register("governor_tower", GovernorTower::new);

    public static final RegistryObject<Structure<NoFeatureConfig>> DRAGON_BONES =
            STRUCTURES.register("dragon_bones", DragonRibcage::new);

    public static final RegistryObject<Structure<NoFeatureConfig>> CELESTIAL_RUINS =
            STRUCTURES.register("celestial_ruins", CelestialRuins::new);

    public static final RegistryObject<Structure<NoFeatureConfig>> LUMBER_MILL_CAMP =
            STRUCTURES.register("lumber_mill_camp", LumberMillCamp::new);

    public static final RegistryObject<Structure<NoFeatureConfig>> FARM_CAMP =
            STRUCTURES.register("farm_camp", FarmCamp::new);

    public static final RegistryObject<Structure<NoFeatureConfig>> QUARRY_CAMP =
            STRUCTURES.register("quarry_camp", QuarryCamp::new);


    public static void setupStructures() {
        setupMapSpacingAndLand(MOLVAN_SETTLEMENT_A.get(),
                new StructureSeparationSettings(15,10, 1234567890),
                false);
        setupMapSpacingAndLand(MOLVAN_SETTLEMENT_B.get(),
                new StructureSeparationSettings(25,18, 875667890),
                false);
        setupMapSpacingAndLand(MOBIUS_VILLAGE.get(),
                new StructureSeparationSettings(35,25, 87135897),
                true);
        setupMapSpacingAndLand(MOBIUS_PORTAL.get(),
                new StructureSeparationSettings(30,20,1238091384),
                true);
        setupMapSpacingAndLand(IMPERIAL_WATCHTOWER.get(),
                new StructureSeparationSettings(40, 30, 91358318),
                true);
        setupMapSpacingAndLand(GOVERNOR_TOWER.get(),
                new StructureSeparationSettings(50, 35, 867530939),
                false);
        setupMapSpacingAndLand(DRAGON_BONES.get(),
                new StructureSeparationSettings(13,7, 172348790),
                false);
        setupMapSpacingAndLand(CELESTIAL_RUINS.get(),
                new StructureSeparationSettings(35,25, 110924590),
                false);
    }

    /**
     * Adds the provided structure to the registry, and adds the separation settings.
     * The rarity of the structure is determined based on the values passed into
     * this method in the structureSeparationSettings argument.
     * This method is called by setupStructures above.
     **/
    public static <F extends Structure<?>> void setupMapSpacingAndLand(F structure, StructureSeparationSettings structureSeparationSettings,
                                                                       boolean transformSurroundingLand) {
        //add our structures into the map in Structure class
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        /*
         * Whether surrounding land will be modified automatically to conform to the bottom of the structure.
         * Basically, it adds land at the base of the structure like it does for Villages and Outposts.
         * Doesn't work well on structure that have pieces stacked vertically or change in heights.
         *
         */
        if (transformSurroundingLand) {
            Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder()
                    .addAll(Structure.NOISE_AFFECTING_FEATURES)
                    .add(structure)
                    .build();
        }

        /*
         * This is the map that holds the default spacing of all structures.
         * Always add your structure to here so that other mods can utilize it if needed.
         *
         * However, while it does propagate the spacing to some correct dimensions from this map,
         * it seems it doesn't always work for code made dimensions as they read from this list beforehand.
         *
         * Instead, we will use the WorldEvent.Load event in ModWorldEvents to add the structure
         * spacing from this list into that dimension or to do dimension blacklisting properly.
         * We also use our entry in DimensionStructuresSettings.DEFAULTS in WorldEvent.Load as well.
         *
         * DEFAULTS requires AccessTransformer  (See resources/META-INF/accesstransformer.cfg)
         */
        DimensionStructuresSettings.DEFAULTS =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.DEFAULTS)
                        .put(structure, structureSeparationSettings)
                        .build();

        /*
         * There are very few mods that relies on seeing your structure in the
         * noise settings registry before the world is made.
         *
         * You may see some mods add their spacings to DimensionSettings.BUILTIN_OVERWORLD instead of the
         * NOISE_GENERATOR_SETTINGS loop below but that field only applies for the default overworld and
         * won't add to other worldtypes or dimensions (like amplified or Nether).
         * So yeah, don't do DimensionSettings.BUILTIN_OVERWORLD. Use the NOISE_GENERATOR_SETTINGS loop
         * below instead if you must.
         */
        WorldGenRegistries.NOISE_GENERATOR_SETTINGS.forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap =
                    settings.structureSettings().structureConfig;
            /*
             * Pre-caution in case a mod makes the structure map immutable like datapacks do.
             * I take no chances myself. You never know what another mods does...
             *
             * structureConfig requires AccessTransformer  (See resources/META-INF/accesstransformer.cfg)
             */
            if (structureMap instanceof ImmutableMap) {
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                settings.structureSettings().structureConfig().entrySet();

            } else {
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }

    public static void register(IEventBus eventBus) {
        STRUCTURES.register(eventBus);
    }
}
