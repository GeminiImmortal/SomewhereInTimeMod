package net.geminiimmortal.mobius.integration;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.item.custom.patchouli.MobiusGuidebook;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MobiusModPatchouli {
    public static final DeferredRegister<Item> PATCHOULI_BOOKS = DeferredRegister.create(ForgeRegistries.ITEMS, MobiusMod.MOD_ID);

    public static final RegistryObject<Item> MOBIUS_GUIDEBOOK = PATCHOULI_BOOKS.register("mobius_guidebook", MobiusGuidebook::new);

    public static void register(IEventBus eventBus){
       PATCHOULI_BOOKS.register(eventBus);
    }
}
