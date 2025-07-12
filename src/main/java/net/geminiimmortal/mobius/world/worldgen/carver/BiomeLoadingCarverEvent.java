package net.geminiimmortal.mobius.world.worldgen.carver;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID)
public class BiomeLoadingCarverEvent {

    @SubscribeEvent
    public static void carveBiomes(final BiomeLoadingEvent event) {
        ResourceLocation mushroomForest = ModBiomes.MUSHROOM_FOREST.getId();
        ResourceLocation draconicForelands = ModBiomes.DRACONIC_FORELANDS.getId();
        ResourceLocation forsakenThicket = ModBiomes.FORSAKEN_THICKET.getId();
        ResourceLocation tealGlades = ModBiomes.TEAL_GLADES.getId();
        ResourceLocation rollingExpanse = ModBiomes.ROLLING_EXPANSE.getId();

        if (Objects.equals(event.getName(), mushroomForest))
            event.getGeneration().addCarver(GenerationStage.Carving.AIR, ModConfiguredCarvers.CONFIGURED_MOBIUS_CAVES_CARVER);
        if (Objects.equals(event.getName(), draconicForelands))
            event.getGeneration().addCarver(GenerationStage.Carving.AIR, ModConfiguredCarvers.CONFIGURED_MOBIUS_CAVES_CARVER);
        if (Objects.equals(event.getName(), forsakenThicket))
            event.getGeneration().addCarver(GenerationStage.Carving.AIR, ModConfiguredCarvers.CONFIGURED_MOBIUS_CAVES_CARVER);
        if (Objects.equals(event.getName(), tealGlades))
            event.getGeneration().addCarver(GenerationStage.Carving.AIR, ModConfiguredCarvers.CONFIGURED_MOBIUS_CAVES_CARVER);
        if (Objects.equals(event.getName(), rollingExpanse))
            event.getGeneration().addCarver(GenerationStage.Carving.AIR, ModConfiguredCarvers.CONFIGURED_MOBIUS_CAVES_CARVER);
    }
}

