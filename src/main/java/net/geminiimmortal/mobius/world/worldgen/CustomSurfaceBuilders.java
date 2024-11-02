package net.geminiimmortal.mobius.world.worldgen;

import net.geminiimmortal.mobius.world.worldgen.biome.LakeGenHelper;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CustomSurfaceBuilders {

    // Create a DeferredRegister instance for Surface Builders
    public static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, "mobius");

    // Register the custom surface builder
    public static final RegistryObject<SurfaceBuilder<SurfaceBuilderConfig>> LAKE_GEN_HELPER = SURFACE_BUILDERS.register(
            "lake_gen_helper", () -> new LakeGenHelper(SurfaceBuilderConfig.CODEC)
    );

    // Call this method in your main mod class
    public static void register(IEventBus eventBus) {
        SURFACE_BUILDERS.register(eventBus);
    }
}



