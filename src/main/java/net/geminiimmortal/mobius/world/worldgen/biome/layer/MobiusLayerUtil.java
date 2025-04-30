package net.geminiimmortal.mobius.world.worldgen.biome.layer;


import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.*;

import java.util.function.LongFunction;


public class MobiusLayerUtil {
    public static int CRIMSON_CASCADES;
    public static int DRACONIC_FORELANDS;
    public static int MUSHROOM_FOREST;
    public static int FORSAKEN_THICKET;
    public static int SHATTERED_PLAINS;
    public static int GOO_LAGOON;
    public static int ROLLING_EXPANSE;
    public static int INFECTED_BOG;
    public static int DRACONIC_FOOTHILLS;

    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> buildMobiusLayers(int biomeSize, int riverSize, LongFunction<C> context, Registry<Biome> biomeRegistry) {
        IAreaFactory<T> layer = IslandLayer.INSTANCE.run(context.apply(1L));

        // Modified zoom sequence for larger biomes
        layer = ZoomLayer.FUZZY.run(context.apply(2000L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2001L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2002L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2003L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2004L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2005L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2006L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2007L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2008L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2009L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2010L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2011L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2012L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2013L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2014L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2015L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2016L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2017L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2018L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2019L), layer);
        layer = ZoomLayer.NORMAL.run(context.apply(2020L), layer);

        IAreaFactory<T> biomeLayer = getBiomeLayer(layer, context, biomeRegistry);
        return biomeLayer;
    }

    private static <T extends IArea, C extends IExtendedNoiseRandom<T>>
    IAreaFactory<T> getBiomeLayer(IAreaFactory<T> parentLayer, LongFunction<C> context, Registry<Biome> biomeRegistry) {

        // Base ocean layer
        parentLayer = new MobiusBiomeLayer(biomeRegistry).run(context.apply(150L), parentLayer);
        parentLayer = ZoomLayer.NORMAL.run(context.apply(151L), parentLayer); // 1x zoom

        // Insert mountain layer EARLY (so its cells are large)
        parentLayer = new MobiusRollingExpanseLayer(biomeRegistry).run(context.apply(150L), parentLayer);
        parentLayer = ZoomLayer.FUZZY.run(context.apply(151L), parentLayer); // 1x zoom

        parentLayer = new MobiusForestLayer(biomeRegistry).run(context.apply(150L), parentLayer);
        parentLayer = ZoomLayer.NORMAL.run(context.apply(151L), parentLayer); // 1x zoom
        parentLayer = ZoomLayer.NORMAL.run(context.apply(151L), parentLayer); // 1x zoom

        parentLayer = new MobiusOceanLayer(biomeRegistry).run(context.apply(150L), parentLayer);
        parentLayer = new MobiusMountainLayer(biomeRegistry).run(context.apply(150L), parentLayer);
        parentLayer = ZoomLayer.NORMAL.run(context.apply(151L), parentLayer); // 1x zoom
        parentLayer = ZoomLayer.NORMAL.run(context.apply(151L), parentLayer); // 1x zoom

        // Final zoom to smooth things out (7x–8x)
        parentLayer = LayerUtil.zoom(1000L, ZoomLayer.NORMAL, parentLayer, 2, context);

        return parentLayer;
    }




    public static Layer getNoiseLayer(long seed, int biomeSize, int riverSize, Registry<Biome> biomeRegistry) {
        setupBiomeIntIDs(biomeRegistry);

        // Add edge protection layer
        int maxCacheSize = 25;
        LongFunction<IExtendedNoiseRandom<LazyArea>> context = salt ->
                new LazyAreaLayerContext(maxCacheSize, seed, salt);

        IAreaFactory<LazyArea> layer = buildMobiusLayers(biomeSize, riverSize, context, biomeRegistry);
        layer = EdgeLayer.CoolWarm.INSTANCE.run(context.apply(3000L), layer); // Add edge smoothing
        layer = SmoothLayer.INSTANCE.run(context.apply(3001L), layer); // Add final smoothing

        return new Layer(layer);
    }




    /**
     * Always call this in the Biome Source's constructor so we get the correct int biome id for this world.
     * (this is because the int id for the biome in the dynamic registry can be different in a different world)
     */
    private static void setupBiomeIntIDs(Registry<Biome> biomeRegistry) {
        // Verify all biomes exist before getting IDs
        CRIMSON_CASCADES = getVerifiedBiomeID(biomeRegistry, ModBiomes.CRIMSON_CASCADES.getId());
        DRACONIC_FORELANDS = getVerifiedBiomeID(biomeRegistry, ModBiomes.DRACONIC_FORELANDS.getId());
        FORSAKEN_THICKET = getVerifiedBiomeID(biomeRegistry, ModBiomes.FORSAKEN_THICKET.getId());
        GOO_LAGOON = getVerifiedBiomeID(biomeRegistry, ModBiomes.GOO_LAGOON.getId());
        ROLLING_EXPANSE = getVerifiedBiomeID(biomeRegistry, ModBiomes.ROLLING_EXPANSE.getId());
        MUSHROOM_FOREST = getVerifiedBiomeID(biomeRegistry, ModBiomes.MUSHROOM_FOREST.getId());
        INFECTED_BOG = getVerifiedBiomeID(biomeRegistry, ModBiomes.INFECTED_BOG.getId());
        SHATTERED_PLAINS = getVerifiedBiomeID(biomeRegistry, ModBiomes.SHATTERED_PLAINS.getId());
        DRACONIC_FOOTHILLS = getVerifiedBiomeID(biomeRegistry, ModBiomes.DRACONIC_FOOTHILLS.getId());
    }

    private static int getVerifiedBiomeID(Registry<Biome> registry, ResourceLocation id) {
        Biome biome = registry.get(id);
        if(biome == null) throw new IllegalStateException("Biome not registered: " + id);
        return registry.getId(biome);
    }
}
