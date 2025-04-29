package net.geminiimmortal.mobius.world.worldgen.biome.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;
import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;

public class MobiusOceanLayer implements IC0Transformer {
    private final Registry<Biome> biomeRegistry;

    public MobiusOceanLayer(Registry<Biome> biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
    }

    @Override
    public int apply(INoiseRandom context, int value) {
        // Example: 1 in 6 chance to generate Goo Lagoon
        if (context.nextRandom(4) == 0) {
            return biomeRegistry.getId(biomeRegistry.get(ModBiomes.BIOME_KEYS.get(1)));
        }
        return value; // fallback to parent value
    }
}

