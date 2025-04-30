package net.geminiimmortal.mobius.world.worldgen.biome.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

public class MobiusForestLayer implements IC0Transformer {
    private final Registry<Biome> biomeRegistry;
    private final int mushroomForestId;
    private final int forsakenThicketId;
    private final int rollingExpanseId;

    public MobiusForestLayer(Registry<Biome> biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
        this.mushroomForestId = MobiusLayerUtil.MUSHROOM_FOREST;
        this.forsakenThicketId = MobiusLayerUtil.FORSAKEN_THICKET;
        this.rollingExpanseId = MobiusLayerUtil.ROLLING_EXPANSE;
    }

    @Override
    public int apply(INoiseRandom noiseRandom ,int value) {
        if (value != rollingExpanseId) return value;

        int random = noiseRandom.nextRandom(value * 31); // Seeded randomness per input
        int roll = random % 10;

        if (roll < 2) {
            return mushroomForestId; // 30%
        } else if (roll < 4) {
            return forsakenThicketId; // 30%
        } else {
            return rollingExpanseId; // 40% stays as is
        }
    }
}


