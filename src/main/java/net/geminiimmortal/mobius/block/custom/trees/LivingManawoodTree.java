package net.geminiimmortal.mobius.block.custom.trees;

import net.geminiimmortal.mobius.world.worldgen.features.ModConfiguredFeatures;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class LivingManawoodTree extends Tree {
    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random random, boolean b) {
        return ModConfiguredFeatures.CONFIGURED_LIVING_MANAWOOD_TREE_FEATURE;
    }
}
