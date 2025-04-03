package net.geminiimmortal.mobius.world.worldgen.feature;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.worldgen.structure.ModStructureGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModFeatureSetup {

    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        ModOreGeneration.generateOres(event);
        ModStructureGeneration.generateStructures(event);
        ModNoFeatureConfigGeneration.generateNFC(event);
        ModTreeGeneration.generateTrees(event);
    }
}
