package net.geminiimmortal.mobius.world.worldgen;

import net.geminiimmortal.mobius.world.worldgen.feature.surface.DraconicForelandsSurfaceBuilder;
import net.geminiimmortal.mobius.world.worldgen.feature.surface.GooLagoonSurfaceBuilder;
import net.geminiimmortal.mobius.world.worldgen.feature.surface.InfectedBogSurfaceBuilder;
import net.geminiimmortal.mobius.world.worldgen.feature.surface.LakeGenHelper;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CustomSurfaceBuilders {

    public static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, "mobius");

    public static final RegistryObject<SurfaceBuilder<SurfaceBuilderConfig>> LAKE_GEN_HELPER = SURFACE_BUILDERS.register(
            "lake_gen_helper", () -> new LakeGenHelper(SurfaceBuilderConfig.CODEC)
    );

    public static final RegistryObject<SurfaceBuilder<SurfaceBuilderConfig>> INFECTED_BOG_SURFACE_BUILDER = SURFACE_BUILDERS.register(
            "infected_bog_surface_builder", () -> new InfectedBogSurfaceBuilder(SurfaceBuilderConfig.CODEC)
    );

    public static final RegistryObject<SurfaceBuilder<SurfaceBuilderConfig>> GOO_LAGOON_SURFACE_BUILDER = SURFACE_BUILDERS.register(
            "goo_lagoon_surface_builder", () -> new GooLagoonSurfaceBuilder(SurfaceBuilderConfig.CODEC)
    );

    public static final RegistryObject<SurfaceBuilder<SurfaceBuilderConfig>> DRACONIC_FORELANDS_SURFACE_BUILDER = SURFACE_BUILDERS.register(
            "draconic_forelands_surface_builder", () -> new DraconicForelandsSurfaceBuilder(SurfaceBuilderConfig.CODEC)
    );

    public static void register(IEventBus eventBus) {
        SURFACE_BUILDERS.register(eventBus);
    }
}



