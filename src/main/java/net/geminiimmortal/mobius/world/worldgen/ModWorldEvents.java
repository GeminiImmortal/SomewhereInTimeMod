package net.geminiimmortal.mobius.world.worldgen;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.worldgen.features.ModMountainGeneration;
import net.geminiimmortal.mobius.world.worldgen.features.ModTreeGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID)
public class ModWorldEvents {

    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        ModTreeGeneration.generateTrees(event);
        ModMountainGeneration.generateMountains(event);
    }
}
