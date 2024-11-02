package net.geminiimmortal.mobius.world.worldgen.biome;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.world.worldgen.CustomSurfaceBuilders;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowyDirtBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class ModConfiguredSurfaceBuilders {

    public static ConfiguredSurfaceBuilder<?> MUSHROOM_FOREST = register("mushroom_forest", SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderConfig(
            Blocks.MYCELIUM.defaultBlockState(),
            Blocks.DIRT.defaultBlockState(),
            ModBlocks.HEMATITE.get().defaultBlockState()
    )));

    public static ConfiguredSurfaceBuilder<?> GOO_LAGOON = register("goo_lagoon", CustomSurfaceBuilders.LAKE_GEN_HELPER.get().configured(LakeGenHelper.LAKE_SURFACE_CONFIG
    ));

    public static ConfiguredSurfaceBuilder<?> FORSAKEN_THICKET = register("forsaken_thicket", SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderConfig(
            Blocks.GRASS_BLOCK.defaultBlockState().setValue(SnowyDirtBlock.SNOWY, true),
            Blocks.DIRT.defaultBlockState(),
            ModBlocks.HEMATITE.get().defaultBlockState()
    )));

    public static ConfiguredSurfaceBuilder<?> DRACONIC_FORELANDS = register("draconic_forelands", SurfaceBuilder.MOUNTAIN.configured(new SurfaceBuilderConfig(
            Blocks.GRASS_BLOCK.defaultBlockState(),
            Blocks.DIRT.defaultBlockState(),
            ModBlocks.HEMATITE.get().defaultBlockState()
    )));

    public static ConfiguredSurfaceBuilder<?> ROLLING_EXPANSE = register("rolling_expanse", SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderConfig(
            Blocks.GRASS_BLOCK.defaultBlockState(),
            Blocks.DIRT.defaultBlockState(),
            ModBlocks.HEMATITE.get().defaultBlockState()
    )));

    private static <SC extends ISurfaceBuilderConfig>ConfiguredSurfaceBuilder<SC> register(String name, ConfiguredSurfaceBuilder<SC> csb) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, new ResourceLocation(MobiusMod.MOD_ID, name), csb);
    }


}
