package net.geminiimmortal.mobius.world.worldgen.biome;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.fluid.ModFluids;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.geminiimmortal.mobius.world.worldgen.features.ModConfiguredFeatures;
import net.geminiimmortal.mobius.world.worldgen.structure.structures.MolvanSettlementA;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.*;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBiomes {
    public static final DeferredRegister<Biome> BIOMES
            = DeferredRegister.create(ForgeRegistries.BIOMES, MobiusMod.MOD_ID);

    public static final RegistryKey<Biome> ROLLING_EXPANSE_KEY = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation("mobius", "rolling_expanse"));

    public static final RegistryObject<Biome> MUSHROOM_FOREST = BIOMES.register("mushroom_forest",
            () -> makeMushroomForest(() -> ModConfiguredSurfaceBuilders.MUSHROOM_FOREST, 0.125f, 0.05f));

    public static final RegistryObject<Biome> GOO_LAGOON = BIOMES.register("goo_lagoon",
            () -> makeGooLagoon(() -> ModConfiguredSurfaceBuilders.GOO_LAGOON, -0.8f, 0.011f));

    public static final RegistryObject<Biome> FORSAKEN_THICKET = BIOMES.register("forsaken_thicket",
            () -> makeForsakenThicket(() -> ModConfiguredSurfaceBuilders.FORSAKEN_THICKET, 0.12f, 0.03f));

    public static final RegistryObject<Biome> DRACONIC_FORELANDS = BIOMES.register("draconic_forelands",
            () -> makeDraconicForelands(() -> ModConfiguredSurfaceBuilders.DRACONIC_FORELANDS, 2f, 0.7f));

    public static final RegistryObject<Biome> ROLLING_EXPANSE = BIOMES.register("rolling_expanse",
            () -> makeRollingExpanse(() -> ModConfiguredSurfaceBuilders.ROLLING_EXPANSE, 0.1f, 0.01f));


    private static Biome makeMushroomForest(final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder, float depth, float scale) {
        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
        DefaultBiomeFeatures.farmAnimals(mobspawninfo$builder);
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(EntityType.MOOSHROOM, 100, 7, 10));
        BiomeGenerationSettings.Builder biomegenerationsettings$builder =
                (new BiomeGenerationSettings.Builder()).surfaceBuilder(surfaceBuilder);
        DefaultBiomeFeatures.addMushroomFieldVegetation(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addTaigaGrass(biomegenerationsettings$builder);

        return (new Biome.Builder()).precipitation(Biome.RainType.NONE).biomeCategory(Biome.Category.FOREST).depth(depth).scale(scale)
                .temperature(1.5F).downfall(0.9F).specialEffects((new BiomeAmbience.Builder()).waterColor(16220377).waterFogColor(-3407872)
                        .fogColor(7535809).skyColor(11532160).foliageColorOverride(16220377).grassColorOverride(-3407872)
                        .ambientParticle(new ParticleEffectAmbience(ParticleTypes.BUBBLE_POP, 0.003f))
                        .ambientLoopSound(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
                        .ambientMoodSound(new MoodSoundAmbience(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0D))
                        .ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111D))
                        .build())
                .mobSpawnSettings(mobspawninfo$builder.build()).generationSettings(biomegenerationsettings$builder.build()).build();
    }

    private static Biome makeGooLagoon(final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder, float depth, float scale) {
        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(EntityType.SQUID, 100, 7, 10));
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(EntityType.COD, 100, 7, 10));
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(EntityType.SALMON, 100, 7, 10));
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(EntityType.PUFFERFISH, 100, 7, 10));
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(EntityType.TROPICAL_FISH, 100, 7, 10));
        BiomeGenerationSettings.Builder biomegenerationsettings$builder =
                (new BiomeGenerationSettings.Builder()).surfaceBuilder(surfaceBuilder);

        return (new Biome.Builder()).precipitation(Biome.RainType.NONE).biomeCategory(Biome.Category.OCEAN).depth(depth).scale(scale)
                .temperature(1.5F).downfall(0.9F).specialEffects((new BiomeAmbience.Builder()).waterColor(16220377).waterFogColor(-3407872)
                        .fogColor(7535809).skyColor(11532160).foliageColorOverride(16220377).grassColorOverride(-3407872)
                        .ambientParticle(new ParticleEffectAmbience(ParticleTypes.BUBBLE_POP, 0.003f))
                        .ambientLoopSound(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
                        .ambientMoodSound(new MoodSoundAmbience(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0D))
                        .ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111D))
                        .build())
                .mobSpawnSettings(mobspawninfo$builder.build()).generationSettings(biomegenerationsettings$builder.build()).build();
    }

    private static Biome makeForsakenThicket(final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder, float depth, float scale) {
        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(EntityType.BAT, 40, 7, 10));
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(ModEntityTypes.FAEDEER.get(), 60, 3,5));
        BiomeGenerationSettings.Builder biomegenerationsettings$builder =
                (new BiomeGenerationSettings.Builder()).surfaceBuilder(surfaceBuilder);
        DefaultBiomeFeatures.addTaigaGrass(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addBerryBushes(biomegenerationsettings$builder);
        //DefaultBiomeFeatures.addTaigaTrees(biomegenerationsettings$builder);
        //DefaultBiomeFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);

        return (new Biome.Builder()).precipitation(Biome.RainType.SNOW).biomeCategory(Biome.Category.TAIGA).depth(depth).scale(scale)
                .temperature(0.0F).downfall(0.5F).specialEffects((new BiomeAmbience.Builder()).waterColor(16220377).waterFogColor(4658242)
                        .fogColor(7535809).skyColor(11532160).foliageColorOverride(4658242).grassColorOverride(4658242)
                        .ambientParticle(new ParticleEffectAmbience(ModParticles.FAEDEER_PARTICLE.get(), 0.001f))
                        .ambientLoopSound(ModSounds.WINDING_WOODS_AMBIENT_LOOP.get())
                        .ambientMoodSound(new MoodSoundAmbience(ModSounds.WINDING_WOODS_AMBIENT_MOOD.get(), 6000, 8, 2.0D))
                        .ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111D))
                        .build())
                .mobSpawnSettings(mobspawninfo$builder.build()).generationSettings(biomegenerationsettings$builder.build()).build();
    }

    private static Biome makeDraconicForelands(final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder, float depth, float scale) {
        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(EntityType.SHEEP, 100, 7, 10));
        BiomeGenerationSettings.Builder biomegenerationsettings$builder =
                (new BiomeGenerationSettings.Builder()).surfaceBuilder(surfaceBuilder);
        DefaultBiomeFeatures.addTaigaGrass(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addMountainTrees(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);

        return (new Biome.Builder()).precipitation(Biome.RainType.SNOW).biomeCategory(Biome.Category.EXTREME_HILLS).depth(depth).scale(scale)
                .temperature(0.0F).downfall(0.5F).specialEffects((new BiomeAmbience.Builder()).waterColor(4159204).waterFogColor(329011)
                        .fogColor(13557759).skyColor(13557759).foliageColorOverride(1064741).grassColorOverride(310708)
                        .ambientParticle(new ParticleEffectAmbience(ParticleTypes.WHITE_ASH, 0.03f))
                        .ambientLoopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
                        .ambientMoodSound(new MoodSoundAmbience(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0D))
                        .ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111D))
                        .build())
                .mobSpawnSettings(mobspawninfo$builder.build()).generationSettings(biomegenerationsettings$builder.build()).build();
    }

    private static Biome makeRollingExpanse(final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder, float depth, float scale) {
        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(EntityType.HORSE, 10, 2, 3));
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(EntityType.SHEEP, 40, 1,3));
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(EntityType.COW, 50, 1,1));
        BiomeGenerationSettings.Builder biomegenerationsettings$builder =
                (new BiomeGenerationSettings.Builder()).surfaceBuilder(surfaceBuilder);
        DefaultBiomeFeatures.addTaigaGrass(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addPlainGrass(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addJungleExtraVegetation(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultGrass(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addPlainVegetation(biomegenerationsettings$builder);

        return (new Biome.Builder()).precipitation(Biome.RainType.RAIN).biomeCategory(Biome.Category.PLAINS).depth(depth).scale(scale)
                .temperature(0.8F).downfall(0.5F).specialEffects((new BiomeAmbience.Builder()).waterColor(12057592).waterFogColor(7535809)
                        .fogColor(7535809).skyColor(11532160).foliageColorOverride(3570695).grassColorOverride(7008979)
                        .ambientLoopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
                        .ambientMoodSound(new MoodSoundAmbience(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0D))
                        .ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111D))
                        .build())
                .mobSpawnSettings(mobspawninfo$builder.build()).generationSettings(biomegenerationsettings$builder.build()).build();
    }



    public static void register(IEventBus eventBus) {
        BIOMES.register(eventBus);
    }

    private static int getSkyColorWithTemperatureModifier(float temperature) {
        float lvt_1_1_ = temperature / 3.0F;
        lvt_1_1_ = MathHelper.clamp(lvt_1_1_, -1.0F, 1.0F);
        return MathHelper.hsvToRgb(0.62222224F - lvt_1_1_ + 0.05F, 0.5F + lvt_1_1_ * 0.1F, 1.0F);
    }
}
