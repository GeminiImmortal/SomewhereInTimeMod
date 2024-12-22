package net.geminiimmortal.mobius;

import com.google.common.collect.Maps;
import net.geminiimmortal.mobius.poi.ModPOIs;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.block.ModWoodTypes;
import net.geminiimmortal.mobius.container.ModContainers;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.*;
import net.geminiimmortal.mobius.entity.render.*;
import net.geminiimmortal.mobius.item.ModItems;
import net.geminiimmortal.mobius.network.ParticlePacket;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.geminiimmortal.mobius.recipe.ModRecipeTypes;
import net.geminiimmortal.mobius.sound.ClientMusicHandler;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.geminiimmortal.mobius.tileentity.ModTileEntities;
import net.geminiimmortal.mobius.tileentity.render.SoulForgeScreen;
import net.geminiimmortal.mobius.util.GolemTransformationHandler;
import net.geminiimmortal.mobius.villager.ModVillagers;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.geminiimmortal.mobius.world.worldgen.CustomSurfaceBuilders;
import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.WoodType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.AxeItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
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
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        ModSounds.register(eventBus);
        ModPOIs.register(eventBus);
        CustomSurfaceBuilders.register(eventBus);
        ModBiomes.register(eventBus);
        ModEntityTypes.register(eventBus);
        ModParticles.register(eventBus);
        ModVillagers.register(eventBus);
        ModTileEntities.register(eventBus);
        ModContainers.register(eventBus);
        ModRecipeTypes.register(eventBus);

        

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new GolemTransformationHandler());


    }



    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        
        event.enqueueWork(() -> {
            WoodType.register(ModWoodTypes.MARROWOOD);
            WoodType.register(ModWoodTypes.MANAWOOD);

            ModDimensions.setupDimension();

            AxeItem.STRIPABLES = Maps.newHashMap(AxeItem.STRIPABLES);
            AxeItem.STRIPABLES.put(ModBlocks.MARROWOOD_LOG.get(), ModBlocks.STRIPPED_MARROWOOD_LOG.get());
            AxeItem.STRIPABLES.put(ModBlocks.MARROWOOD_WOOD.get(), ModBlocks.STRIPPED_MARROWOOD_WOOD.get());
            AxeItem.STRIPABLES.put(ModBlocks.MANAWOOD_LOG.get(), ModBlocks.STRIPPED_MANAWOOD_LOG.get());
            AxeItem.STRIPABLES.put(ModBlocks.MANAWOOD_WOOD.get(), ModBlocks.STRIPPED_MANAWOOD_WOOD.get());

        });
    }



    @Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class NetworkHandler {
        public static final String PROTOCOL_VERSION = "1";
        public static SimpleChannel INSTANCE;

        @SubscribeEvent
        public static void setup(FMLCommonSetupEvent event) {
            INSTANCE = NetworkRegistry.newSimpleChannel(
                    new ResourceLocation(MobiusMod.MOD_ID, "particles"),
                    () -> PROTOCOL_VERSION,
                    PROTOCOL_VERSION::equals,
                    PROTOCOL_VERSION::equals
            );

            INSTANCE.registerMessage(0, ParticlePacket.class, ParticlePacket::encode, ParticlePacket::decode, ParticlePacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));


        }
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(ModBlocks.BONE_LEAVES.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.LIVING_MANAWOOD_LEAVES.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MARROWOOD_SAPLING.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MANAWOOD_DOOR.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENCE_CHANNELER.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MANAWOOD_SAPLING.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.MANAWOOD_TRAPDOOR.get(), RenderType.cutout());

            ClientRegistry.bindTileEntityRenderer(ModTileEntities.SIGN_TILE_ENTITIES.get(),
                    SignTileEntityRenderer::new);

            Atlases.addWoodType(ModWoodTypes.MARROWOOD);
            Atlases.addWoodType(ModWoodTypes.MANAWOOD);

            ScreenManager.register(ModContainers.SOUL_FORGE_CONTAINER.get(),
                    SoulForgeScreen::new);
        });

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CLUB_GOLEM.get(), ClubGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.DIAMOND_GOLEM.get(), DiamondGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.HEART_GOLEM.get(), HeartGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SPADE_GOLEM.get(), SpadeGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.FAEDEER.get(), FaedeerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MARROWOOD_BOAT.get(), CustomBoatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MANAWOOD_BOAT.get(), ManawoodBoatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SORCERER.get(), SorcererRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SPELL.get(), SpellRenderer::new);

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
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
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
        }

    }


    @Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEventSubscriber {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MinecraftForge.EVENT_BUS.register(new ClientMusicHandler());
            LOGGER.info("Setting up music manager.");
        }
    }


    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

    }
}
