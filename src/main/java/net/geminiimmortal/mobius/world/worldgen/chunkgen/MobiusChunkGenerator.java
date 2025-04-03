package net.geminiimmortal.mobius.world.worldgen.chunkgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.geminiimmortal.mobius.world.dimension.SeedBearer;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;


public class MobiusChunkGenerator extends NoiseChunkGenerator {

    public static final Codec<MobiusChunkGenerator> CODEC = RecordCodecBuilder.create(
            (instance) -> instance.group(
                            BiomeProvider.CODEC.fieldOf("biome_source")
                                    .forGetter((chunkGenerator) -> chunkGenerator.biomeSource),
                            Codec.LONG.fieldOf("seed")
                                    .orElseGet(SeedBearer::giveMeSeed)
                                    .forGetter(chunkGenerator -> chunkGenerator.seed),
                            DimensionSettings.CODEC.fieldOf("settings")
                                    .forGetter((chunkGenerator) -> chunkGenerator.settings))
                    .apply(instance, instance.stable(MobiusChunkGenerator::new)));

    public MobiusChunkGenerator(BiomeProvider biomeProvider, long seed, Supplier<DimensionSettings> dimensionSettingsSupplier) {
        super(biomeProvider, seed, dimensionSettingsSupplier);
        System.out.println("Chunk generator seed: " + seed);
        Thread.dumpStack();
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ChunkGenerator withSeed(long seed) {
        return new MobiusChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }
}
