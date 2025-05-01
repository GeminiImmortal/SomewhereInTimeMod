package net.geminiimmortal.mobius.world.dimension;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.worldgen.chunkgen.MobiusBiomeProvider;
import net.geminiimmortal.mobius.world.worldgen.chunkgen.MobiusChunkGenerator;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.server.ChunkManager;

public class ModDimensions {

        public static final RegistryKey<World> MOBIUS_WORLD = RegistryKey.create(Registry.DIMENSION_REGISTRY,
                new ResourceLocation(MobiusMod.MOD_ID, "mobius"));

        public static final RegistryKey<DimensionType> MOBIUS_TYPE = RegistryKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "mobius_type"));

        private static ResourceLocation name(String name) {
                return new ResourceLocation(MobiusMod.MOD_ID, name);
        }

        public static void registerDimensionStuff() {
                Registry.register(Registry.CHUNK_GENERATOR, name("chunk_generator"), MobiusChunkGenerator.CODEC);
                Registry.register(Registry.BIOME_SOURCE, name("biome_provider"), MobiusBiomeProvider.CODEC);
        }



     /*   private static ChunkGenerator createChunkGenerator() {

                Registry<Biome> biomeRegistry = WorldGenRegistries.BIOME;
                long seed = SeedBearer.giveMeSeed();

                MobiusBiomeProvider biomeProvider = new MobiusBiomeProvider(false, biomeRegistry, seed);

                // Return chunk generator with your settings
                return new MobiusChunkGenerator(biomeProvider, SeedBearer.giveMeSeed(), () -> WorldGenRegistries.NOISE_GENERATOR_SETTINGS.getOrThrow(DimensionSettings.OVERWORLD));
        }*/


}



