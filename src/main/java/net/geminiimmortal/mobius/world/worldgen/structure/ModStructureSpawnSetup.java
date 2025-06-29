package net.geminiimmortal.mobius.world.worldgen.structure;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModStructureSpawnSetup {

    @SubscribeEvent
    public static void structureSpawnListGatherEvent(final StructureSpawnListGatherEvent event) {
        ModStructureGeneration.setStructureSpawns(event);
    }
}
