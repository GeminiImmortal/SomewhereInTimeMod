package net.geminiimmortal.mobius.spawn;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID)
public class ModBiomeSpawnsSetupEvent {

    @SubscribeEvent
    public static void setupHostileBiomeSpawns(final BiomeLoadingEvent event) {
        if (event.getName() == null) return;
        ResourceLocation biomeName = event.getName();
        final List<RegistryKey<Biome>> lookupRegistry = ModBiomes.BIOME_KEYS;

        if (lookupRegistry.stream().anyMatch(biomeRegistryKey -> biomeRegistryKey.location().equals(biomeName))) {
            event.getSpawns().getSpawner(EntityClassification.MONSTER)
                    .add(new MobSpawnInfo.Spawners(ModEntityTypes.BONE_WOLF.get(), 90, 1, 2));
            event.getSpawns().getSpawner(EntityClassification.MONSTER)
                    .add(new MobSpawnInfo.Spawners(ModEntityTypes.GIANT.get(), 1, 1, 1));
            event.getSpawns().getSpawner(EntityClassification.MONSTER)
                    .add(new MobSpawnInfo.Spawners(ModEntityTypes.INFERNAL_BRIAR.get(), 9, 1, 1));
        }


    }
}
