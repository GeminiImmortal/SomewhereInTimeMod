package net.geminiimmortal.mobius.world.worldgen.feature.placement;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.world.worldgen.feature.surface.CustomSurfaceBuilders;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class ModConfiguredSurfaceBuilders {

    public static ConfiguredSurfaceBuilder<?> MUSHROOM_FOREST = register("mushroom_forest", SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderConfig(
            ModBlocks.SOUL_PODZOL.get().defaultBlockState(),
            ModBlocks.AURORA_DIRT.get().defaultBlockState(),
            ModBlocks.HEMATITE.get().defaultBlockState()
    )));

    public static ConfiguredSurfaceBuilder<?> GOO_LAGOON = register("goo_lagoon", CustomSurfaceBuilders.GOO_LAGOON_SURFACE_BUILDER.get().configured(new SurfaceBuilderConfig(
            ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState(),
            ModBlocks.AURORA_DIRT.get().defaultBlockState(),
            ModBlocks.AURORA_DIRT.get().defaultBlockState()
    )));

    public static ConfiguredSurfaceBuilder<?> FORSAKEN_THICKET = register("forsaken_thicket", SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderConfig(
            ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState(),
            ModBlocks.AURORA_DIRT.get().defaultBlockState(),
            ModBlocks.HEMATITE.get().defaultBlockState()
    )));

    public static ConfiguredSurfaceBuilder<?> DRACONIC_FORELANDS = register("draconic_forelands", CustomSurfaceBuilders.DRACONIC_FORELANDS_MOUNTAINS_SURFACE_BUILDER.get().configured(new SurfaceBuilderConfig(
            ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState(),
            ModBlocks.BLOODSTONE.get().defaultBlockState(),
            ModBlocks.HEMATITE.get().defaultBlockState()
    )));

    public static ConfiguredSurfaceBuilder<?> DRACONIC_FOOTHILLS = register("draconic_foothills", CustomSurfaceBuilders.DRACONIC_FOOTHILLS_SURFACE_BUILDER.get().configured(new SurfaceBuilderConfig(
            ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState(),
            ModBlocks.AURORA_DIRT.get().defaultBlockState(),
            ModBlocks.HEMATITE.get().defaultBlockState()
    )));

    public static ConfiguredSurfaceBuilder<?> ROLLING_EXPANSE = register("rolling_expanse", SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderConfig(
            ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState(),
            ModBlocks.AURORA_DIRT.get().defaultBlockState(),
            ModBlocks.HEMATITE.get().defaultBlockState()
    )));

    public static ConfiguredSurfaceBuilder<?> TEAL_GLADES = register("infected_bog", CustomSurfaceBuilders.INFECTED_BOG_SURFACE_BUILDER.get().configured(new SurfaceBuilderConfig(
            ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState(),
            ModBlocks.AURORA_DIRT.get().defaultBlockState(),
            ModBlocks.AURORA_DIRT.get().defaultBlockState()
    )));

    public static ConfiguredSurfaceBuilder<?> CRIMSON_CASCADES = register("crimson_cascades", SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderConfig(
            ModBlocks.BLOODSTONE.get().defaultBlockState(),
            Blocks.MAGMA_BLOCK.defaultBlockState(),
            ModBlocks.HEMATITE.get().defaultBlockState()
    )));

    public static ConfiguredSurfaceBuilder<?> SHATTERED_PLAINS = register("shattered_plains", SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderConfig(
            ModBlocks.HUECO_SAND.get().defaultBlockState(),
            ModBlocks.OBSECFUTORIA.get().defaultBlockState(),
            ModBlocks.HEMATITE.get().defaultBlockState()
    )));


    private static <SC extends ISurfaceBuilderConfig>ConfiguredSurfaceBuilder<SC> register(String name, ConfiguredSurfaceBuilder<SC> csb) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, new ResourceLocation(MobiusMod.MOD_ID, name), csb);
    }


}
