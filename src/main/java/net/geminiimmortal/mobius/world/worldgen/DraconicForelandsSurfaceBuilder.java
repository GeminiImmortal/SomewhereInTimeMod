package net.geminiimmortal.mobius.world.worldgen;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.surfacebuilders.MountainSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;

public class DraconicForelandsSurfaceBuilder extends MountainSurfaceBuilder {


    public DraconicForelandsSurfaceBuilder(Codec<SurfaceBuilderConfig> p_i232129_1_) {
        super(p_i232129_1_);
    }

    public void apply(Random p_205610_1_, IChunk p_205610_2_, Biome p_205610_3_, int p_205610_4_, int p_205610_5_, int p_205610_6_, double p_205610_7_, BlockState p_205610_9_, BlockState p_205610_10_, int p_205610_11_, long p_205610_12_, SurfaceBuilderConfig p_205610_14_) {
        if (p_205610_7_ > 1.0) {
            SurfaceBuilder.DEFAULT.apply(p_205610_1_, p_205610_2_, p_205610_3_, p_205610_4_, p_205610_5_, p_205610_6_, p_205610_7_, p_205610_9_, p_205610_10_, p_205610_11_, p_205610_12_, new SurfaceBuilderConfig(ModBlocks.AURORA_DIRT.get().defaultBlockState(), ModBlocks.AURORA_DIRT.get().defaultBlockState(), ModBlocks.AURORA_DIRT.get().defaultBlockState()));
        } else {
            SurfaceBuilder.DEFAULT.apply(p_205610_1_, p_205610_2_, p_205610_3_, p_205610_4_, p_205610_5_, p_205610_6_, p_205610_7_, p_205610_9_, p_205610_10_, p_205610_11_, p_205610_12_, new SurfaceBuilderConfig(ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState(), ModBlocks.AURORA_DIRT.get().defaultBlockState(), ModBlocks.AURORA_DIRT.get().defaultBlockState()));
        }
    }

    public static final SurfaceBuilderConfig DRACONIC_FORELANDS_SURFACE_CONFIG = new SurfaceBuilderConfig(
            ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState(),
            ModBlocks.AURORA_DIRT.get().defaultBlockState(),
            ModBlocks.HEMATITE.get().defaultBlockState()
    );
}