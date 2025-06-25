package net.geminiimmortal.mobius.world.worldgen.biome;

import com.google.common.collect.Lists;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.geminiimmortal.mobius.world.worldgen.feature.placement.ModConfiguredSurfaceBuilders;
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
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class ModBiomes {
    public static final List<RegistryKey<Biome>> BIOME_KEYS = Lists.newArrayList();


    public static final DeferredRegister<Biome> BIOMES
            = DeferredRegister.create(ForgeRegistries.BIOMES, MobiusMod.MOD_ID);

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

    public static final RegistryObject<Biome> TEAL_GLADES = BIOMES.register("teal_glades",
            () -> makeTealGlades(() -> ModConfiguredSurfaceBuilders.TEAL_GLADES, -0.1f, 0.01f));

    public static final RegistryObject<Biome> CRIMSON_CASCADES = BIOMES.register("crimson_cascades",
            () -> makeCrimsonCascades(() -> ModConfiguredSurfaceBuilders.CRIMSON_CASCADES, 1.3f, 1f));

    public static final RegistryObject<Biome> SHATTERED_PLAINS = BIOMES.register("shattered_plains",
            () -> makeShatteredPlains(() -> ModConfiguredSurfaceBuilders.SHATTERED_PLAINS, 0.111f, 0.45f));

    public static final RegistryObject<Biome> DRACONIC_FOOTHILLS = BIOMES.register("draconic_foothills",
            () -> makeDraconicFoothills(() -> ModConfiguredSurfaceBuilders.DRACONIC_FOOTHILLS, 0.111f, 0.4f));


    public static RegistryKey<Biome> registerBiomeKey(String biomeName) {
        RegistryKey<Biome> biomeKey = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MobiusMod.MOD_ID, biomeName));
        BIOME_KEYS.add(biomeKey);
        return biomeKey;
    }

    public static void registerBiomes() {
        BIOME_KEYS.add(RegistryKey.create(Registry.BIOME_REGISTRY,
                new ResourceLocation(MobiusMod.MOD_ID, "mushroom_forest")));
        BIOME_KEYS.add(RegistryKey.create(Registry.BIOME_REGISTRY,
                new ResourceLocation(MobiusMod.MOD_ID, "goo_lagoon")));
        BIOME_KEYS.add(RegistryKey.create(Registry.BIOME_REGISTRY,
                new ResourceLocation(MobiusMod.MOD_ID, "forsaken_thicket")));
        BIOME_KEYS.add(RegistryKey.create(Registry.BIOME_REGISTRY,
                new ResourceLocation(MobiusMod.MOD_ID, "draconic_forelands")));
        BIOME_KEYS.add(RegistryKey.create(Registry.BIOME_REGISTRY,
                new ResourceLocation(MobiusMod.MOD_ID, "rolling_expanse")));
//        BIOME_KEYS.add(RegistryKey.create(Registry.BIOME_REGISTRY,
//                new ResourceLocation(MobiusMod.MOD_ID, "draconic_foothills")));
        BIOME_KEYS.add(RegistryKey.create(Registry.BIOME_REGISTRY,
                new ResourceLocation(MobiusMod.MOD_ID, "teal_glades")));
//        BIOME_KEYS.add(RegistryKey.create(Registry.BIOME_REGISTRY,
//                new ResourceLocation(MobiusMod.MOD_ID, "crimson_cascades")));
//        BIOME_KEYS.add(RegistryKey.create(Registry.BIOME_REGISTRY,
//                new ResourceLocation(MobiusMod.MOD_ID, "shattered_plains")));

        System.out.println("Registered: " + BIOME_KEYS);
    }

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
                        .ambientLoopSound(ModSounds.WINDING_WOODS_AMBIENT_LOOP.get())
                        .ambientMoodSound(new MoodSoundAmbience(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0D))
                        .ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111D))
                        .build())
                .mobSpawnSettings(mobspawninfo$builder.build()).generationSettings(biomegenerationsettings$builder.build()).build();
    }

    private static Biome makeGooLagoon(final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder, float depth, float scale) {
        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
        mobspawninfo$builder.addSpawn(EntityClassification.WATER_CREATURE,
                new MobSpawnInfo.Spawners(EntityType.SQUID, 10, 2, 6));
        BiomeGenerationSettings.Builder biomegenerationsettings$builder =
                (new BiomeGenerationSettings.Builder()).surfaceBuilder(surfaceBuilder);
        DefaultBiomeFeatures.addDefaultCarvers(biomegenerationsettings$builder);

        return (new Biome.Builder()).precipitation(Biome.RainType.NONE).biomeCategory(Biome.Category.OCEAN).depth(depth).scale(scale)
                .temperature(1.5F).downfall(0.9F).specialEffects((new BiomeAmbience.Builder()).waterColor(16220377).waterFogColor(-3407872)
                        .fogColor(7535809).skyColor(11532160).foliageColorOverride(16220377).grassColorOverride(-3407872)
                        .ambientParticle(new ParticleEffectAmbience(ParticleTypes.BUBBLE_POP, 0.003f))
                        .ambientLoopSound(ModSounds.AMBIENT_LOOP_GOO_LAGOON.get())
                        .ambientMoodSound(new MoodSoundAmbience(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0D))
                        .ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111D))
                        .build())
                .mobSpawnSettings(mobspawninfo$builder.build()).generationSettings(biomegenerationsettings$builder.build()).build();
    }

    private static Biome makeForsakenThicket(final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder, float depth, float scale) {
        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(EntityType.BAT, 35, 7, 10));
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(ModEntityTypes.FAEDEER.get(), 60, 3,5));
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(ModEntityTypes.BONE_WOLF.get(), 5, 2, 3));
        BiomeGenerationSettings.Builder biomegenerationsettings$builder =
                (new BiomeGenerationSettings.Builder()).surfaceBuilder(surfaceBuilder);
        DefaultBiomeFeatures.addTaigaGrass(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addBerryBushes(biomegenerationsettings$builder);

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
        DefaultBiomeFeatures.addDefaultCarvers(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);

        return (new Biome.Builder()).precipitation(Biome.RainType.SNOW).biomeCategory(Biome.Category.EXTREME_HILLS).depth(depth).scale(scale)
                .temperature(0.0F).downfall(0.5F).specialEffects((new BiomeAmbience.Builder()).waterColor(4159204).waterFogColor(329011)
                        .fogColor(13557759).skyColor(11532160).foliageColorOverride(1064741).grassColorOverride(310708)
                        .ambientParticle(new ParticleEffectAmbience(ParticleTypes.WHITE_ASH, 0.03f))
                        .ambientLoopSound(ModSounds.WINDING_WOODS_AMBIENT_LOOP.get())
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
                new MobSpawnInfo.Spawners(ModEntityTypes.FAECOW.get(), 10, 1,2));
        BiomeGenerationSettings.Builder biomegenerationsettings$builder =
                (new BiomeGenerationSettings.Builder()).surfaceBuilder(surfaceBuilder);
        biomegenerationsettings$builder.addFeature(GenerationStage.Decoration.LAKES, Features.SEAGRASS_RIVER);


        return (new Biome.Builder()).precipitation(Biome.RainType.RAIN).biomeCategory(Biome.Category.PLAINS).depth(depth).scale(scale)
                .temperature(0.8F).downfall(0.5F).specialEffects((new BiomeAmbience.Builder()).waterColor(12057592).waterFogColor(7535809)
                        .fogColor(7535809).skyColor(11532160).foliageColorOverride(3570695).grassColorOverride(7008979)
                        .ambientLoopSound(ModSounds.WINDING_WOODS_AMBIENT_LOOP.get())
                        .ambientMoodSound(new MoodSoundAmbience(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0D))
                        .ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111D))
                        .build())
                .mobSpawnSettings(mobspawninfo$builder.build()).generationSettings(biomegenerationsettings$builder.build()).build();
    }

    private static Biome makeTealGlades(final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder, float depth, float scale) {
        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
        mobspawninfo$builder.addSpawn(EntityClassification.MONSTER,
                new MobSpawnInfo.Spawners(EntityType.ZOMBIE, 40, 1,3));
        mobspawninfo$builder.addSpawn(EntityClassification.MONSTER,
                new MobSpawnInfo.Spawners(EntityType.SKELETON, 50, 1,1));
        BiomeGenerationSettings.Builder biomegenerationsettings$builder =
                (new BiomeGenerationSettings.Builder()).surfaceBuilder(surfaceBuilder);
        DefaultBiomeFeatures.addSwampVegetation(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addSwampExtraVegetation(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addSwampClayDisk(biomegenerationsettings$builder);

        return (new Biome.Builder()).precipitation(Biome.RainType.RAIN).biomeCategory(Biome.Category.SWAMP).depth(depth).scale(scale)
                .temperature(0.8F).downfall(0.5F).specialEffects((new BiomeAmbience.Builder()).waterColor(12057592).waterFogColor(7535809)
                        .fogColor(7535809).skyColor(11532160).foliageColorOverride(3570695).grassColorOverride(7008979)
                        .ambientLoopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
                        .ambientMoodSound(new MoodSoundAmbience(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0D))
                        .ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111D))
                        .build())
                .mobSpawnSettings(mobspawninfo$builder.build()).generationSettings(biomegenerationsettings$builder.build()).build();
    }

    private static Biome makeCrimsonCascades(final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder, float depth, float scale) {
        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
        mobspawninfo$builder.addSpawn(EntityClassification.MONSTER,
                new MobSpawnInfo.Spawners(EntityType.MAGMA_CUBE, 100, 7, 10));
        mobspawninfo$builder.addSpawn(EntityClassification.MONSTER,
                new MobSpawnInfo.Spawners(ModEntityTypes.INFERNAL_BRIAR.get(), 2, 1, 2));
        BiomeGenerationSettings.Builder biomegenerationsettings$builder =
                (new BiomeGenerationSettings.Builder()).surfaceBuilder(surfaceBuilder);
        DefaultBiomeFeatures.addBadlandExtraVegetation(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addBadlandGrass(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultCarvers(biomegenerationsettings$builder);

        return (new Biome.Builder()).precipitation(Biome.RainType.NONE).biomeCategory(Biome.Category.EXTREME_HILLS).depth(depth).scale(scale)
                .temperature(0.0F).downfall(0.5F).specialEffects((new BiomeAmbience.Builder()).waterColor(-3407872).waterFogColor(-3407872)
                        .fogColor(-3407872).skyColor(11532160).foliageColorOverride(-3407872).grassColorOverride(-3407872)
                        .ambientParticle(new ParticleEffectAmbience(ParticleTypes.WHITE_ASH, 0.065f))
                        .ambientLoopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
                        .ambientMoodSound(new MoodSoundAmbience(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 2.0D))
                        .ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS, 0.0111D))
                        .build())
                .mobSpawnSettings(mobspawninfo$builder.build()).generationSettings(biomegenerationsettings$builder.build()).build();
    }

    private static Biome makeShatteredPlains(final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder, float depth, float scale) {
        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
        BiomeGenerationSettings.Builder biomegenerationsettings$builder =
                (new BiomeGenerationSettings.Builder()).surfaceBuilder(surfaceBuilder);

        return (new Biome.Builder()).precipitation(Biome.RainType.NONE).biomeCategory(Biome.Category.PLAINS).depth(depth).scale(scale)
                .temperature(0.0F).downfall(0.0F).specialEffects((new BiomeAmbience.Builder()).waterColor(3242335).waterFogColor(7958391)
                        .fogColor(3608884).skyColor(11532160).foliageColorOverride(7958391).grassColorOverride(7958391)
                        .ambientLoopSound(SoundEvents.AMBIENT_WARPED_FOREST_LOOP)
                        .ambientMoodSound(new MoodSoundAmbience(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0D))
                        .ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111D))
                        .build())
                .mobSpawnSettings(mobspawninfo$builder.build()).generationSettings(biomegenerationsettings$builder.build()).build();
    }

    public static Biome makeDraconicFoothills(final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder, float depth, float scale) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder();

        generation.surfaceBuilder(() -> ModConfiguredSurfaceBuilders.DRACONIC_FOOTHILLS);
        DefaultBiomeFeatures.addDefaultCarvers(generation);
        DefaultBiomeFeatures.addDefaultFlowers(generation);

        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(EntityType.BAT, 35, 7, 10));
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(ModEntityTypes.FAEDEER.get(), 60, 3,5));
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE,
                new MobSpawnInfo.Spawners(ModEntityTypes.BONE_WOLF.get(), 5, 2, 3));

        return new Biome.Builder()
                .precipitation(Biome.RainType.RAIN)
                .biomeCategory(Biome.Category.EXTREME_HILLS)
                .depth(0.45F)
                .scale(0.3F)
                .temperature(0.3F)
                .downfall(0.4F)
                .specialEffects(new BiomeAmbience.Builder()
                        .fogColor(0x605070)
                        .waterColor(0x3f76e4)
                        .waterFogColor(0x050533)
                        .skyColor(11532160)
                        .ambientParticle(new ParticleEffectAmbience(ModParticles.FAEDEER_PARTICLE.get(), 0.01f))
                        .build())
                .mobSpawnSettings(mobspawninfo$builder.build())
                .generationSettings(generation.build())
                .build();
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
