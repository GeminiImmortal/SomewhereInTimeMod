package net.geminiimmortal.mobius.network;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
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

        NETWORK_CHANNEL.messageBuilder(ParticlePacket.class, 0, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ParticlePacket::encode)
                .decoder(ParticlePacket::decode)
                .consumer(ParticlePacket::handle)
                .add();
        NETWORK_CHANNEL.messageBuilder(PlayMusicPacket.class, 1, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PlayMusicPacket::encode)
                .decoder(PlayMusicPacket::decode)
                .consumer(PlayMusicPacket::handle)
                .add();
        NETWORK_CHANNEL.messageBuilder(ClientPlaySoundPacket.class, 2, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientPlaySoundPacket::encode)
                .decoder(ClientPlaySoundPacket::decode)
                .consumer(ClientPlaySoundPacket::handle)
                .add();
    }
}

