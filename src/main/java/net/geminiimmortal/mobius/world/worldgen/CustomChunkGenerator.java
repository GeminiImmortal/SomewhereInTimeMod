package net.geminiimmortal.mobius.world.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;

import java.util.Random;

public class CustomChunkGenerator extends ChunkGenerator {

    private static final net.minecraft.world.gen.settings.DimensionStructuresSettings DimensionStructuresSettings = null;

    public CustomChunkGenerator(BiomeProvider biomeProvider, long seed) {
        super(biomeProvider, biomeProvider, DimensionStructuresSettings, seed);
    }

    long seed = new Random().nextLong();
    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return null;
    }

    @Override
    public ChunkGenerator withSeed(long l) {
        return new CustomChunkGenerator(this.getBiomeSource(), getSeed());
    }

    public long getSeed(){
        return seed;
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion worldGenRegion, IChunk iChunk) {

    }

    @Override
    public void fillFromNoise(IWorld iWorld, StructureManager structureManager, IChunk iChunk) {

    }

    @Override
    public int getBaseHeight(int i, int i1, Heightmap.Type type) {
        return 0;
    }

    @Override
    public IBlockReader getBaseColumn(int i, int i1) {
        return null;
    }

}
