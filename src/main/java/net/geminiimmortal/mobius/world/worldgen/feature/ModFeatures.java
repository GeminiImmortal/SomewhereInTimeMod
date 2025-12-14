package net.geminiimmortal.mobius.world.worldgen.feature;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MobiusMod.MOD_ID);

    public static final RegistryObject<Feature<NoFeatureConfig>> CLUSTERED_STANDING_GLOOMCAP_FEATURE = registerFeat("clustered_standing_gloomcap_feature", () ->
            new ClusteredStandingGloomcapMushroomFeature(NoFeatureConfig.CODEC, ModBlocks.STANDING_GLOOMCAP.get().defaultBlockState()));

    public static final RegistryObject<Feature<NoFeatureConfig>> CLUSTERED_STANDING_SKYCAP_FEATURE = registerFeat("clustered_standing_skycap_feature", () ->
            new ClusteredStandingSkycapMushroomFeature(NoFeatureConfig.CODEC, ModBlocks.STANDING_SKYCAP.get().defaultBlockState()));

    public static final RegistryObject<Feature<NoFeatureConfig>> CLUSTERED_WILD_MANA_WART_FEATURE = registerFeat("clustered_wild_mana_wart_feature", () ->
            new ClusteredWildManaWartFeature(NoFeatureConfig.CODEC, ModBlocks.WILD_MANA_WART.get().defaultBlockState()));

    public static final RegistryObject<Feature<NoFeatureConfig>> GIANT_GLOOMCAP_MUSHROOM_FEATURE = registerFeat("giant_gloomcap_mushroom_feature", () ->
            new GiantGloomcapMushroomFeature(NoFeatureConfig.CODEC, ModBlocks.GIANT_GLOOMCAP_STEM.get(), ModBlocks.GIANT_GLOOMCAP_CAP.get()));

    public static final RegistryObject<Feature<NoFeatureConfig>> FLOATING_BLOCK_FEATURE = registerFeat("floating_block_feature", () ->
            new FloatingBlockFeature(NoFeatureConfig.CODEC));

    public static final RegistryObject<Feature<NoFeatureConfig>> TALL_GRASS_CARPET_FEATURE = registerFeat("tall_grass_carpet_feature", () ->
            new TallGrassCarpetFeature(NoFeatureConfig.CODEC));

    public static final RegistryObject<Feature<NoFeatureConfig>> BOULDER = registerFeat("boulder", () ->
            new BoulderFeature(NoFeatureConfig.CODEC, Blocks.MOSSY_COBBLESTONE.defaultBlockState()));

    public static final RegistryObject<Feature<NoFeatureConfig>> CLUSTERED_AURORA_BERRY_BUSHES_FEATURE = registerFeat("clustered_aurora_berry_bushes_feature", () ->
            new ClusteredAuroraBerryBushesFeature(NoFeatureConfig.CODEC, ModBlocks.AURORA_BERRY.get().defaultBlockState()));

    public static final RegistryObject<Feature<NoFeatureConfig>> BLOODSTONE_BLOB_FEATURE = registerFeat("bloodstone_blob_feature", () ->
            new BloodstoneBlobFeature(NoFeatureConfig.CODEC, ModBlocks.BLOODSTONE.get().defaultBlockState()));

    public static final RegistryObject<Feature<NoFeatureConfig>> MYSTIC_TEAR = registerFeat("mystic_tear", () ->
            new MysticTearFeature(NoFeatureConfig.CODEC, ModBlocks.MYSTIC_TEAR.get().defaultBlockState()));


    private static <T extends Feature<?>> RegistryObject<T> registerFeat(String name, final Supplier<T> sup) {
        return FEATURES.register(name, sup);
    }

}

