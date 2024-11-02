package net.geminiimmortal.mobius.villager;

import com.google.common.collect.ImmutableSet;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.ModPOIs.ModPOIs;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModVillagers {
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.PROFESSIONS, MobiusMod.MOD_ID);

    public static final RegistryObject<VillagerProfession> MONSTER_HUNTER =
            VILLAGER_PROFESSIONS.register("monster_hunter", () -> new VillagerProfession("monster_hunter",
                    ModPOIs.HUNTING_TABLE.get(), ImmutableSet.of(), ImmutableSet.of(), ModSounds.MONSTER_HUNTER_WORKS.get()));

    public static final RegistryObject<VillagerProfession> MAGISMITH =
            VILLAGER_PROFESSIONS.register("magismith", () -> new VillagerProfession("magismith",
                    ModPOIs.SOUL_FORGE.get(), ImmutableSet.of(), ImmutableSet.of(), ModSounds.MAGISMITH_WORKS.get()));

    public static void register(IEventBus eventBus){
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
