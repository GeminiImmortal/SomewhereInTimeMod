package net.geminiimmortal.mobius.world.worldgen.carver;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;

public class ModConfiguredCarvers {
    public static ConfiguredCarver<?> CONFIGURED_MOBIUS_CAVES_CARVER;

    public static void registerConfiguredCarvers() {
        CONFIGURED_MOBIUS_CAVES_CARVER = WorldGenRegistries.register(
                WorldGenRegistries.CONFIGURED_CARVER,
                new ResourceLocation(MobiusMod.MOD_ID, "mobius_caves_carver"),
                ModCarvers.MOBIUS_CAVES.get().configured(new ProbabilityConfig(0.14285715F))
        );
    }
}

