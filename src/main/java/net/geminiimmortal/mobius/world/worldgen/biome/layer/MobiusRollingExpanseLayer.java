package net.geminiimmortal.mobius.world.worldgen.biome.layer;

import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

public class MobiusRollingExpanseLayer implements IC0Transformer {
    private final Registry<Biome> biomeRegistry;

    public MobiusRollingExpanseLayer(Registry<Biome> biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
    }

    @Override
    public int apply(INoiseRandom context, int value) {
        if (context.nextRandom(3) == 0) {
            if (context.nextRandom(8) == 0) {
                return biomeRegistry.getId(biomeRegistry.get(ModBiomes.BIOME_KEYS.get(5)));
            }
            return biomeRegistry.getId(biomeRegistry.get(ModBiomes.BIOME_KEYS.get(4)));
        }

        return value;
    }
}
