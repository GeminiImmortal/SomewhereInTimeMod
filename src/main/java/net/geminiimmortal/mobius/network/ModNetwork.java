package net.geminiimmortal.mobius.network;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModNetwork {
    public static SimpleChannel NETWORK_CHANNEL;
    public static final String PROTOCOL_VERSION = "1";

    public static void init() {
        NETWORK_CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MobiusMod.MOD_ID, "main_channel"))
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();
        

        int id = 0;
        NETWORK_CHANNEL.messageBuilder(ParticlePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ParticlePacket::encode)
                .decoder(ParticlePacket::decode)
                .consumer(ParticlePacket::handle)
                .add();
        NETWORK_CHANNEL.messageBuilder(GiantStompPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(GiantStompPacket::encode)
                .decoder(GiantStompPacket::decode)
                .consumer(GiantStompPacket::handle)
                .add();
        NETWORK_CHANNEL.messageBuilder(ClientPlaySoundPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientPlaySoundPacket::encode)
                .decoder(ClientPlaySoundPacket::decode)
                .consumer(ClientPlaySoundPacket::handle)
                .add();
        NETWORK_CHANNEL.messageBuilder(BeamRenderPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(BeamRenderPacket::encode)
                .decoder(BeamRenderPacket::decode)
                .consumer(BeamRenderPacket::handle)
                .add();
        NETWORK_CHANNEL.messageBuilder(BeamEndPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(BeamEndPacket::encode)
                .decoder(BeamEndPacket::decode)
                .consumer(BeamEndPacket::handle)
                .add();
        NETWORK_CHANNEL.messageBuilder(BeamCirclePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(BeamCirclePacket::encode)
                .decoder(BeamCirclePacket::decode)
                .consumer(BeamCirclePacket::handle)
                .add();
        NETWORK_CHANNEL.messageBuilder(SInfamySyncPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SInfamySyncPacket::encode)
                .decoder(SInfamySyncPacket::decode)
                .consumer(SInfamySyncPacket::handle)
                .add();
        NETWORK_CHANNEL.messageBuilder(LanceHitPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(LanceHitPacket::encode)
                .decoder(LanceHitPacket::decode)
                .consumer(LanceHitPacket::handle)
                .add();
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            init();
        });
    }
}


