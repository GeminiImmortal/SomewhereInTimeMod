package net.geminiimmortal.mobius.world.worldgen.chunkgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.geminiimmortal.mobius.world.dimension.SeedBearer;
import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.geminiimmortal.mobius.world.worldgen.biome.layer.MobiusLayerUtil;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.biome.provider.BiomeProvider;

import net.minecraft.world.gen.layer.Layer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class MobiusBiomeProvider extends BiomeProvider {
    public static final Codec<MobiusBiomeProvider> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.BOOL.fieldOf("large_biomes").orElse(false).stable().forGetter((mobiusBiomeProvider) -> {
            return mobiusBiomeProvider.largeBiomes;
        }), net.minecraft.util.registry.RegistryLookupCodec.create(net.minecraft.util.registry.Registry.BIOME_REGISTRY).forGetter((mobiusBiomeProvider) -> {
            return mobiusBiomeProvider.lookupRegistry;
        }), Codec.LONG.fieldOf("seed").orElseGet(SeedBearer::getSeed).forGetter(mobiusBiomeProvider -> {
            return mobiusBiomeProvider.lastSeed;
        })).apply(builder, builder.stable(MobiusBiomeProvider::new));
    });
    private long lastSeed = Long.MIN_VALUE;
    private Layer genBiomes;
    private final boolean largeBiomes;
    private final net.minecraft.util.registry.Registry<Biome> lookupRegistry;

    public MobiusBiomeProvider(boolean largeBiomes, Registry<Biome> lookupRegistry, long seed) {
        super(ModBiomes.BIOME_KEYS.stream().map(lookupRegistry::getOrThrow).collect(Collectors.toList()));

        this.largeBiomes = largeBiomes;
        this.lookupRegistry = lookupRegistry;
        updateLayers(seed);
    }

    private void updateLayers(long seed) {
        if (seed != lastSeed) {
            this.genBiomes = MobiusLayerUtil.getNoiseLayer(seed, largeBiomes ? 6 : 4, 6, lookupRegistry);
            this.lastSeed = seed;
        }
    }

    @Override
    @Nonnull
    protected Codec<? extends BiomeProvider> codec() {
        return CODEC;
    }

    @Override
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public BiomeProvider withSeed(long seed) {
        return new MobiusBiomeProvider(this.largeBiomes, this.lookupRegistry, this.lastSeed);
    }

    /**
     * Returns the correct dynamic registry biome instead of using get method
     * which actually returns the incorrect biome instance because it resolves the biome
     * with WorldGenRegistry first instead of the dynamic registry which is... bad.
     */
    @Override
    @Nonnull
    public Biome getNoiseBiome(int x, int y, int z) {
        updateLayers(SeedBearer.getSeed());
        int k = this.genBiomes.area.get(x, z);
        Biome biome = this.lookupRegistry.byId(k);

        if (biome != null) {
            // Dynamic Registry biome (this should always be returned ideally)
            return biome;
        } else {
            //fallback to WorldGenRegistry registry if dynamic registry doesn't have biome
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                throw Util.pauseInIde(new IllegalStateException("Unknown biome id: " + k));
            } else {
                biome = this.lookupRegistry.get(BiomeRegistry.byId(0));
                if (biome == null) {
                    // If this is reached, it is the end of the world lol
                    return BiomeRegistry.THE_VOID;
                } else {
                    // WorldGenRegistry biome (this is not good, but we need to return something)
                    return biome;
                }
            }
        }
    }
}