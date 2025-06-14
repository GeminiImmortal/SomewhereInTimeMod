package net.geminiimmortal.mobius.world.dimension;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.worldgen.chunkgen.MobiusBiomeProvider;
import net.geminiimmortal.mobius.world.worldgen.chunkgen.MobiusChunkGenerator;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

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
}



