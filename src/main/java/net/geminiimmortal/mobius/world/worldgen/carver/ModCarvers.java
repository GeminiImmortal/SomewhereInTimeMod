package net.geminiimmortal.mobius.world.worldgen.carver;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModCarvers {
    public static final DeferredRegister<WorldCarver<?>> CARVERS = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, MobiusMod.MOD_ID);

    public static final RegistryObject<WorldCarver<ProbabilityConfig>> MOBIUS_CAVES = CARVERS.register("mobius_caves_carver", () -> new MobiusCaveCarver(ProbabilityConfig.CODEC, 256));
}
