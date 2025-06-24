package net.geminiimmortal.mobius.integration;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.item.custom.patchouli.LoreEntry;
import net.geminiimmortal.mobius.item.custom.patchouli.LoreEntryType;
import net.geminiimmortal.mobius.item.custom.patchouli.MobiusGuidebook;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MobiusModPatchouli {
    public static final DeferredRegister<Item> PATCHOULI_BOOKS = DeferredRegister.create(ForgeRegistries.ITEMS, MobiusMod.MOD_ID);

    public static final RegistryObject<Item> MOBIUS_GUIDEBOOK = PATCHOULI_BOOKS.register("mobius_guidebook", MobiusGuidebook::new);
    public static final RegistryObject<Item> LORE_FRAGMENT_SMUGGLER_CAMP_0 = PATCHOULI_BOOKS.register("lore_fragment_smuggler_camp_0", () -> new LoreEntry(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1), LoreEntryType.LORE_FRAGMENT_SMUGGLER_CAMP_0));
    public static final RegistryObject<Item> LORE_FRAGMENT_SMUGGLER_CAMP_1 = PATCHOULI_BOOKS.register("lore_fragment_smuggler_camp_1", () -> new LoreEntry(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1), LoreEntryType.LORE_FRAGMENT_SMUGGLER_CAMP_1));


    public static void register(IEventBus eventBus){
       PATCHOULI_BOOKS.register(eventBus);
    }
}
