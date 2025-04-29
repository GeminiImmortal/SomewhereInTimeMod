package net.geminiimmortal.mobius.world.worldgen.biome.layer;

import com.google.common.collect.Lists;
import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;
import net.minecraftforge.common.BiomeManager;


import java.util.ArrayList;
import java.util.List;

public class MobiusBiomeLayer implements IC0Transformer {
    private final Registry<Biome> biomeRegistry;
    private final List<BiomeManager.BiomeEntry> biomes = Lists.newArrayList();
    private List<RegistryKey<Biome>> biomeKeys;

    public MobiusBiomeLayer(Registry<Biome> biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
    }

    private List<RegistryKey<Biome>> getBiomeKeys() {
        if (biomeKeys == null) {
            biomeKeys = new ArrayList<>();
            for (RegistryKey<Biome> key : ModBiomes.BIOME_KEYS) {
                // Change this line:
                if (key != null && biomeRegistry.getOptional(key.location()).isPresent()) {
                    biomeKeys.add(key);
                }
            }
            if (biomeKeys.size() < 5) {
                throw new IllegalStateException("Required 5 biomes but found: " + biomeKeys.size());
            }
        }
        return biomeKeys;
    }

    private void ensureInitialized() {
        if (!this.biomes.isEmpty()) return;

        List<RegistryKey<Biome>> keys = getBiomeKeys();
        if (keys.isEmpty()) {
            throw new IllegalStateException("No valid biome keys found in registry!");
        }

        // Clear any existing entries
        this.biomes.clear();

        // Add entries with weights
        addBiomeEntry(keys.get(0), 10); // Mushroom Forest
        addBiomeEntry(keys.get(1), 10); // Goo Lagoon
        addBiomeEntry(keys.get(2), 20); // Forsaken Thicket
        addBiomeEntry(keys.get(3), 20);  // Draconic Forelands
        addBiomeEntry(keys.get(4), 40); // Rolling Expanse
    //    addBiomeEntry(keys.get(5), 2); // Infected Bog
    //    addBiomeEntry(keys.get(6), 2);  // Crimson Cascades
    //    addBiomeEntry(keys.get(7), 1);  // Shattered Plains

    }




    private void addBiomeEntry(RegistryKey<Biome> key, int weight) {
        if (key != null && biomeRegistry.getOptional(key.location()).isPresent()) {
            this.biomes.add(new BiomeManager.BiomeEntry(key, weight));
        } else {
            System.err.println("Warning: Biome not found or not registered: " + key.location());
        }
    }

    @Override
    public int apply(INoiseRandom noiseRandom, int value) {
        ensureInitialized();

        if (value == biomeRegistry.getId(biomeRegistry.get(ModBiomes.BIOME_KEYS.get(1)))) {
            return value;
        }

        if (value == biomeRegistry.getId(biomeRegistry.get(ModBiomes.BIOME_KEYS.get(4)))) {
            return value;
        }

        int totalWeight = WeightedRandom.getTotalWeight(biomes);
        int weight = noiseRandom.nextRandom(totalWeight);
        RegistryKey<Biome> selectedKey = WeightedRandom.getWeightedItem(biomes, weight).getKey();

        // Directly use registry ID instead of list index
        return biomeRegistry.getId(biomeRegistry.get(selectedKey));
    }
}

