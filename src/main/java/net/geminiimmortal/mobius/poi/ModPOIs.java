package net.geminiimmortal.mobius.poi;


import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.block.custom.CampClaimType;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.stream.Collectors;

import static net.geminiimmortal.mobius.block.custom.RebelClaimBlock.TYPE;
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

    public static final RegistryObject<PointOfInterestType> ESSENCE_CHANNELER =
            POI.register("essence_channeler_poi", () -> new PointOfInterestType("essence_channeler_poi",
                    PointOfInterestType.getBlockStates(ModBlocks.ESSENCE_CHANNELER.get()), 1, 1));

    public static final RegistryObject<PointOfInterestType> SMUGGLER_STRONGBOX =
            POI.register("smuggler_strongbox_poi", () -> new PointOfInterestType("smuggler_strongbox_poi",
                    PointOfInterestType.getBlockStates(ModBlocks.SMUGGLER_STRONGBOX.get()), 1, 1));

    public static final RegistryObject<PointOfInterestType> REBEL_CLAIM_MILL =
            POI.register("rebel_claim_mill_poi", () -> new PointOfInterestType("rebel_claim_mill_poi",
                    PointOfInterestType.getBlockStates(ModBlocks.REBEL_CLAIM.get()).stream().filter((state ->
                            state.getValue(TYPE) == CampClaimType.LUMBER_MILL)).collect(Collectors.toSet()), 1, 1));

    public static final RegistryObject<PointOfInterestType> REBEL_CLAIM_FARM =
            POI.register("rebel_claim_farm_poi", () -> new PointOfInterestType("rebel_claim_farm_poi",
                    PointOfInterestType.getBlockStates(ModBlocks.REBEL_CLAIM.get()).stream().filter((state ->
                            state.getValue(TYPE) == CampClaimType.FARM)).collect(Collectors.toSet()), 1, 1));

    public static final RegistryObject<PointOfInterestType> REBEL_CLAIM_QUARRY =
            POI.register("rebel_claim_quarry_poi", () -> new PointOfInterestType("rebel_claim_quarry_poi",
                    PointOfInterestType.getBlockStates(ModBlocks.REBEL_CLAIM.get()).stream().filter((state ->
                            state.getValue(TYPE) == CampClaimType.QUARRY)).collect(Collectors.toSet()), 1, 1));

    public static void register(IEventBus eventBus) {
        POI.register(eventBus);
    }
}
