package net.geminiimmortal.mobius;

import com.google.common.collect.Maps;
import net.geminiimmortal.mobius.effects.ModEffects;
import net.geminiimmortal.mobius.fluid.ModFluids;
import net.geminiimmortal.mobius.item.custom.ModSpawnEgg;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.geminiimmortal.mobius.poi.ModPOIs;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.block.ModWoodTypes;
import net.geminiimmortal.mobius.container.ModContainers;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.*;
import net.geminiimmortal.mobius.entity.render.*;
import net.geminiimmortal.mobius.item.ModItems;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.geminiimmortal.mobius.recipe.ModRecipeTypes;
import net.geminiimmortal.mobius.sound.ClientMusicHandler;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.geminiimmortal.mobius.tileentity.ModTileEntities;
import net.geminiimmortal.mobius.tileentity.render.*;
import net.geminiimmortal.mobius.util.CustomDamageEventHandler;
import net.geminiimmortal.mobius.villager.ModVillagers;
import net.geminiimmortal.mobius.world.worldgen.CustomSurfaceBuilders;
import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.geminiimmortal.mobius.world.worldgen.feature.ModFeatures;
import net.geminiimmortal.mobius.world.worldgen.feature.placement.ModFeaturePlacement;
import net.geminiimmortal.mobius.world.worldgen.structure.ModStructures;
import net.minecraft.block.Block;
import net.minecraft.block.WoodType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.AxeItem;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MobiusMod.MOD_ID)
public class MobiusMod
{

    public static final String MOD_ID = "mobius";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public MobiusMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        if (FMLEnvironment.dist == Dist.CLIENT) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        }

        ModFeaturePlacement.DECORATORS.register(eventBus);
        ModFeatures.FEATURES.register(eventBus);
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        ModFluids.register(eventBus);
        ModSounds.register(eventBus);
        ModPOIs.register(eventBus);
        ModEffects.register(eventBus);
        CustomSurfaceBuilders.register(eventBus);
        ModBiomes.register(eventBus);
        ModEntityTypes.register(eventBus);
        ModParticles.register(eventBus);
        ModVillagers.register(eventBus);
        ModTileEntities.register(eventBus);
        ModContainers.register(eventBus);
        ModStructures.register(eventBus);
        ModRecipeTypes.register(eventBus);
        

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new CustomDamageEventHandler());


    }



    private void setup(final FMLCommonSetupEvent event)
    {
        
        ModNetwork.init();


        event.enqueueWork(() -> {
            BiomeDictionary.addTypes(
                    RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "draconic_forelands")),
                    BiomeDictionary.Type.MOUNTAIN,
                    BiomeDictionary.Type.COLD
            );

            BiomeDictionary.addTypes(
                    RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "rolling_expanse")),
                    BiomeDictionary.Type.PLAINS,
                    BiomeDictionary.Type.LUSH
            );

            BiomeDictionary.addTypes(
                    RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "goo_lagoon")),
                    BiomeDictionary.Type.OCEAN,
                    BiomeDictionary.Type.LUSH
            );

            BiomeDictionary.addTypes(
                    RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "mushroom_forest")),
                    BiomeDictionary.Type.JUNGLE,
                    BiomeDictionary.Type.LUSH,
                    BiomeDictionary.Type.DENSE
            );

            BiomeDictionary.addTypes(
                    RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "forsaken_thicket")),
                    BiomeDictionary.Type.FOREST,
                    BiomeDictionary.Type.CONIFEROUS,
                    BiomeDictionary.Type.COLD
            );

            BiomeDictionary.addTypes(
                    RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MOD_ID, "infected_bog")),
                    BiomeDictionary.Type.SPOOKY,
                    BiomeDictionary.Type.SWAMP,
                    BiomeDictionary.Type.LUSH
            );

            BiomeDictionary.addTypes(
                    RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "crimson_cascades")),
                    BiomeDictionary.Type.MOUNTAIN,
                    BiomeDictionary.Type.HOT,
                    BiomeDictionary.Type.DRY
            );

            BiomeDictionary.addTypes(
                    RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobiusMod.MOD_ID, "shattered_plains")),
                    BiomeDictionary.Type.DRY,
                    BiomeDictionary.Type.HOT,
                    BiomeDictionary.Type.DEAD
            );

            WoodType.register(ModWoodTypes.MARROWOOD);
            WoodType.register(ModWoodTypes.MANAWOOD);
            WoodType.register(ModWoodTypes.GLOAMTHORN);

            ModStructures.setupStructures();

            AxeItem.STRIPABLES = Maps.newHashMap(AxeItem.STRIPABLES);
            AxeItem.STRIPABLES.put(ModBlocks.MARROWOOD_LOG.get(), ModBlocks.STRIPPED_MARROWOOD_LOG.get());
            AxeItem.STRIPABLES.put(ModBlocks.MARROWOOD_WOOD.get(), ModBlocks.STRIPPED_MARROWOOD_WOOD.get());
            AxeItem.STRIPABLES.put(ModBlocks.MANAWOOD_LOG.get(), ModBlocks.STRIPPED_MANAWOOD_LOG.get());
            AxeItem.STRIPABLES.put(ModBlocks.MANAWOOD_WOOD.get(), ModBlocks.STRIPPED_MANAWOOD_WOOD.get());
            AxeItem.STRIPABLES.put(ModBlocks.GLOAMTHORN_LOG.get(), ModBlocks.STRIPPED_GLOAMTHORN_LOG.get());
            AxeItem.STRIPABLES.put(ModBlocks.GLOAMTHORN_WOOD.get(), ModBlocks.STRIPPED_GLOAMTHORN_WOOD.get());

            EntitySpawnPlacementRegistry.register(
                    ModEntityTypes.ANGLERFISH.get(),
                    EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    AnglerfishEntity::canMobSpawn
            );
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(ModBlocks.BONE_LEAVES.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.LIVING_MANAWOOD_LEAVES.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MARROWOOD_SAPLING.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MANAWOOD_DOOR.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENCE_CHANNELER.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MANAWOOD_SAPLING.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MANAWOOD_TRAPDOOR.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.ASTRAL_CONDUIT.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.GLOAMTHORN_LEAVES.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.GLOAMTHORN_SAPLING.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.GLOAMTHORN_DOOR.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.GLOAMTHORN_TRAPDOOR.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.STANDING_GLOOMCAP.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.MANA_WART.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.WILD_MANA_WART.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.GLOOMCAP.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.LATENT_MANA_COLLECTOR.get(), RenderType.cutout());

            RenderTypeLookup.setRenderLayer(ModBlocks.MOBIUS_PORTAL.get(), RenderType.translucent());

            RenderTypeLookup.setRenderLayer(ModBlocks.GIANT_GLOOMCAP_CAP.get(), RenderType.translucent());

            RenderTypeLookup.setRenderLayer(ModFluids.BOG_WATER_BLOCK.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModFluids.FLOWING_BOG_WATER.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModFluids.BOG_WATER_FLUID.get(), RenderType.translucent());

            RenderTypeLookup.setRenderLayer(ModFluids.ECTOPLASM_BLOCK.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModFluids.FLOWING_ECTOPLASM.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModFluids.ECTOPLASM_FLUID.get(), RenderType.translucent());

            ClientRegistry.bindTileEntityRenderer(ModTileEntities.SIGN_TILE_ENTITIES.get(),
                    SignTileEntityRenderer::new);
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.GLOWING_BLOCK.get(),
                    GlowingBlockRenderer::new);


            Atlases.addWoodType(ModWoodTypes.MARROWOOD);
            Atlases.addWoodType(ModWoodTypes.MANAWOOD);
            Atlases.addWoodType(ModWoodTypes.GLOAMTHORN);

            ScreenManager.register(ModContainers.SOUL_FORGE_CONTAINER.get(),
                    SoulForgeScreen::new);
            ScreenManager.register(ModContainers.ASTRAL_CONDUIT_CONTAINER.get(),
                    AstralConduitScreen::new);
            ScreenManager.register(ModContainers.ESSENCE_CHANNELER_CONTAINER.get(),
                    EssenceChannelerScreen::new);
            ScreenManager.register(ModContainers.LATENT_MANA_COLLECTOR_CONTAINER.get(),
                    LatentManaCollectorScreen::new);
        });


        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CLUB_GOLEM.get(), ClubGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.DIAMOND_GOLEM.get(), DiamondGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.HEART_GOLEM.get(), HeartGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SPADE_GOLEM.get(), SpadeGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.FAEDEER.get(), FaedeerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MARROWOOD_BOAT.get(), CustomBoatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MANAWOOD_BOAT.get(), ManawoodBoatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GLOAMTHORN_BOAT.get(), GloamthornBoatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SORCERER.get(), SorcererRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SPELL.get(), SpellRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GOVERNOR.get(), GovernorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.KNIVES_OUT.get(), GovernorKnivesOutRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CLONE.get(), CloneRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SHATTER_CLONE.get(), ShatterCloneRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MOLVAN.get(), MolvanRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BONE_WOLF.get(), BoneWolfRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.INFERNAL_BRIAR.get(), InfernalBriarRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ANGLERFISH.get(), AnglerfishRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.TORNADO.get(), TornadoRenderer::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("[MOBIUS] Mod has initialized successfully.");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            //LOGGER.info("HELLO from Register Block");
        }
    }

    @Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventSubscriber {


        @SubscribeEvent
        public static void onRegisterAttributes(EntityAttributeCreationEvent event) {
            event.put(ModEntityTypes.CLUB_GOLEM.get(), ClubGolemEntity.setCustomAttributes().build());
            event.put(ModEntityTypes.DIAMOND_GOLEM.get(), DiamondGolemEntity.setCustomAttributes().build());
            event.put(ModEntityTypes.HEART_GOLEM.get(), HeartGolemEntity.setCustomAttributes().build());
            event.put(ModEntityTypes.SPADE_GOLEM.get(), SpadeGolemEntity.setCustomAttributes().build());
            event.put(ModEntityTypes.FAEDEER.get(), FaedeerEntity.setCustomAttributes().build());
            event.put(ModEntityTypes.SORCERER.get(), SorcererEntity.setCustomAttributes().build());
            event.put(ModEntityTypes.GOVERNOR.get(), GovernorEntity.setCustomAttributes().build());
            event.put(ModEntityTypes.CLONE.get(), CloneEntity.setCustomAttributes().build());
            event.put(ModEntityTypes.MOLVAN.get(), MolvanEntity.setCustomAttributes().build());
            event.put(ModEntityTypes.BONE_WOLF.get(), BoneWolfEntity.setCustomAttributes().build());
            event.put(ModEntityTypes.INFERNAL_BRIAR.get(), InfernalBriarEntity.setCustomAttributes().build());
            event.put(ModEntityTypes.ANGLERFISH.get(), AnglerfishEntity.setCustomAttributes().build());
        }

        @SubscribeEvent
        public static void onRegisterEntities(RegistryEvent.Register<EntityType<?>> event) {
            ModSpawnEgg.initSpawnEggs();
        }

    }


    @Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEventSubscriber {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MinecraftForge.EVENT_BUS.register(new ClientMusicHandler());
            LOGGER.info("[MOBIUS] Setting up music manager.");
        }
    }
}
