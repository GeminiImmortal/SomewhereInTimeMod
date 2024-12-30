package net.geminiimmortal.mobius.world.worldgen;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.worldgen.features.ModMountainGeneration;
import net.geminiimmortal.mobius.world.worldgen.features.ModOreGeneration;
import net.geminiimmortal.mobius.world.worldgen.features.ModTreeGeneration;
import net.geminiimmortal.mobius.world.worldgen.structure.ModStructureGeneration;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import net.geminiimmortal.mobius.world.worldgen.structure.ModStructures;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModWorldEvents {

    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        ModStructureGeneration.generateStructures(event);
        ModOreGeneration.generateOres(event);
        ModTreeGeneration.generateTrees(event);
        ModMountainGeneration.generateMountains(event);


    }

    @SubscribeEvent
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            try {
                Method GETCODEC_METHOD =
                        ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey(
                        (Codec<? extends ChunkGenerator>)GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));

                if (cgRL != null && cgRL.getNamespace().equals("terraforged")) {
                    return;
                }
            } catch (Exception e) {
                LogManager.getLogger().error("Was unable to check if " + serverWorld.dimension().location()
                        + " is using Terraforged's ChunkGenerator.");
            }

            // Prevent spawning our structure in Vanilla's superflat world
            if (serverWorld.getChunkSource().generator instanceof FlatChunkGenerator &&
                    serverWorld.dimension().equals(World.OVERWORLD)) {
                return;
            }

            // Adding our Structure to the Map
            Map<Structure<?>, StructureSeparationSettings> tempMap =
                    new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig);
            tempMap.putIfAbsent(ModStructures.MOLVAN_SETTLEMENT_A.get(),
                    DimensionStructuresSettings.DEFAULTS.get(ModStructures.MOLVAN_SETTLEMENT_A.get()));
            tempMap.putIfAbsent(ModStructures.MOLVAN_SETTLEMENT_B.get(),
                    DimensionStructuresSettings.DEFAULTS.get(ModStructures.MOLVAN_SETTLEMENT_B.get()));
            tempMap.putIfAbsent(ModStructures.MOBIUS_VILLAGE.get(),
                    DimensionStructuresSettings.DEFAULTS.get(ModStructures.MOBIUS_VILLAGE.get()));
            tempMap.putIfAbsent(ModStructures.MOBIUS_PORTAL.get(),
                    DimensionStructuresSettings.DEFAULTS.get(ModStructures.MOBIUS_PORTAL.get()));
            tempMap.putIfAbsent(ModStructures.IMPERIAL_WATCHTOWER.get(),
                    DimensionStructuresSettings.DEFAULTS.get(ModStructures.IMPERIAL_WATCHTOWER.get()));
            tempMap.putIfAbsent(ModStructures.GOVERNOR_TOWER.get(),
                    DimensionStructuresSettings.DEFAULTS.get(ModStructures.GOVERNOR_TOWER.get()));
            serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }
}
