package net.geminiimmortal.mobius.poi;


import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import static net.minecraftforge.registries.ForgeRegistries.POI_TYPES;

public class ModPOIs {
    public static final DeferredRegister<PointOfInterestType> POI
            = DeferredRegister.create(POI_TYPES, MobiusMod.MOD_ID);

    public static final RegistryObject<PointOfInterestType> MOBIUS_PORTAL =
            POI.register("mobius_portal", () -> new PointOfInterestType("mobius_portal",
                    PointOfInterestType.getBlockStates(ModBlocks.MOBIUS_PORTAL.get()), 0, 1));

    public static final RegistryObject<PointOfInterestType> HUNTING_TABLE =
            POI.register("hunting_table_poi", () -> new PointOfInterestType("hunting_table_poi",
                    PointOfInterestType.getBlockStates(ModBlocks.HUNTING_TABLE.get()), 1, 1));

    public static final RegistryObject<PointOfInterestType> SOUL_FORGE =
            POI.register("soul_forge_poi", () -> new PointOfInterestType("soul_forge_poi",
                    PointOfInterestType.getBlockStates(ModBlocks.SOUL_FORGE.get()), 1, 1));

    public static void register(IEventBus eventBus) {
        POI.register(eventBus);
    }
}
