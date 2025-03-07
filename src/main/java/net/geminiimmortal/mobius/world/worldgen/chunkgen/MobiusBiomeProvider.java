package net.geminiimmortal.mobius.world.worldgen.chunkgen;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.dimension.SeedBearer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import net.minecraft.world.gen.layer.Layer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.stream.Collectors;

public class MobiusBiomeProvider extends BiomeProvider {
    private Layer customBiomeLayer;
    private final Registry<Biome> biomeRegistry;
    private final long seed;
    private static final List<RegistryKey<Biome>> mobiusBiomes;
    private final SimplexNoiseGenerator noiseGenerator;

    public static final Codec<MobiusBiomeProvider> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(provider -> provider.biomeRegistry),
                    Codec.LONG.fieldOf("seed").stable()
                            .orElseGet(SeedBearer::giveMeSeed)
                            .forGetter((provider) -> provider.seed)
            ).apply(instance, MobiusBiomeProvider::new)
    );

    private static final RegistryKey<Biome> FORSAKEN_THICKET = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "forsaken_thicket"));
    private static final RegistryKey<Biome> INFECTED_BOG = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "infected_bog"));
    private static final RegistryKey<Biome> CRIMSON_CASCADES = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "crimson_cascades"));
    private static final RegistryKey<Biome> GOO_LAGOON = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "goo_lagoon"));
    private static final RegistryKey<Biome> DRACONIC_FORELANDS = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "draconic_forelands"));
    private static final RegistryKey<Biome> ROLLING_EXPANSE = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "rolling_expanse"));
    private static final RegistryKey<Biome> MUSHROOM_FOREST = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "mushroom_forest"));
    private static final RegistryKey<Biome> SHATTERED_PLAINS = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "shattered_plains"));



    public MobiusBiomeProvider(Registry<Biome> biomeRegistry, Long seed) {
        super(getFilteredBiomes(biomeRegistry));
        this.biomeRegistry = biomeRegistry;
        this.seed = seed;
        this.noiseGenerator = new SimplexNoiseGenerator(new SharedSeedRandom(seed));
    }

    private Biome getBiomeFromNoise(double noiseValue) {
        if (noiseValue < -0.5) {
            return biomeRegistry.get(new ResourceLocation("mobius:forsaken_thicket"));
        } else if (noiseValue < -0.4) {
            return biomeRegistry.get(new ResourceLocation("mobius:mushroom_forest"));
        } else if (noiseValue < -0.3) {
            return biomeRegistry.get(new ResourceLocation("mobius:rolling_expanse"));
        } else if (noiseValue < 0) {
            return biomeRegistry.get(new ResourceLocation("mobius:goo_lagoon"));
        } else if (noiseValue < 0.5) {
            return biomeRegistry.get(new ResourceLocation("mobius:draconic_forelands"));
        } else if (noiseValue < 0.6){
            return biomeRegistry.get(new ResourceLocation("mobius:crimson_cascades"));
        } else if (noiseValue < 0.9) {
            return biomeRegistry.get(new ResourceLocation("mobius:infected_bog"));
        } else {
            return biomeRegistry.get(new ResourceLocation("mobius:shattered_plains"));
        }
    }


    // Filters out only the "mobius" biomes
    private static List<Biome> getFilteredBiomes(Registry<Biome> registry) {
        return registry.stream()
                .filter(biome -> biome.getRegistryName().getNamespace().equals("mobius"))
                .collect(Collectors.toList());
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        int biomeX = x >> 2;
        int biomeZ = z >> 2;

        // Use Minecraft's built-in Perlin noise generator
        double noiseValue = noiseGenerator.getValue((double) biomeX / 100.0, (double) biomeZ / 100.0);

        // Map the noise value to a biome
        return getBiomeFromNoise(noiseValue);
    }



    @Override
    protected Codec<? extends BiomeProvider> codec() {
        return CODEC;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public BiomeProvider withSeed(long seed) {
        return new MobiusBiomeProvider(this.biomeRegistry, seed);
    }

    static {
        mobiusBiomes = ImmutableList.of(FORSAKEN_THICKET, INFECTED_BOG,
        CRIMSON_CASCADES, GOO_LAGOON, DRACONIC_FORELANDS, ROLLING_EXPANSE,
        MUSHROOM_FOREST, SHATTERED_PLAINS);
    }


}

