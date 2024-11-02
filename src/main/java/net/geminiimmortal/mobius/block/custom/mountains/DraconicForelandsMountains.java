package net.geminiimmortal.mobius.block.custom.mountains;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.world.worldgen.features.ModConfiguredFeatures;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.surfacebuilders.MountainSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import javax.annotation.Nullable;
import java.util.Random;

public class DraconicForelandsMountains {
    @Nullable

    protected ConfiguredFeature<?, ?> getConfiguredFeature(Random random, boolean b) {
        return ModConfiguredFeatures.DRACONIC_FORELANDS_MOUNTAIN;
    }
}
