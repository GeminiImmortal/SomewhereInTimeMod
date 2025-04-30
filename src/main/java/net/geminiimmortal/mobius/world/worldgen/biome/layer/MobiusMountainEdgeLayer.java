package net.geminiimmortal.mobius.world.worldgen.biome.layer;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

public class MobiusMountainEdgeLayer implements IC0Transformer {
    private final Registry<Biome> biomeRegistry;
    private final int mountainBiomeId;
    private final int foothillsBiomeId;

    public MobiusMountainEdgeLayer(Registry<Biome> registry) {
        this.biomeRegistry = registry;
        this.mountainBiomeId = MobiusLayerUtil.DRACONIC_FORELANDS;
        this.foothillsBiomeId = MobiusLayerUtil.DRACONIC_FOOTHILLS;
    }

    @Override
    public int apply(INoiseRandom context, int value) {
        // Randomly insert foothills near mountain entries
        // Here we treat some % of mountain tiles as foothills
        if (value == mountainBiomeId) {
            return context.nextRandom(5) == 0 ? foothillsBiomeId : mountainBiomeId; // 20% chance
        }

        return value;
    }
}

