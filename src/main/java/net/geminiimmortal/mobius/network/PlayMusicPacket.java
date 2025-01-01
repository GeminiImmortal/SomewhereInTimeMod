package net.geminiimmortal.mobius.network;

import net.geminiimmortal.mobius.sound.ClientMusicHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayMusicPacket {
    private final String music;

    public PlayMusicPacket(String music) {
        this.music = music;
    }

    public static void encode(PlayMusicPacket packet, PacketBuffer buf) {
        buf.writeUtf(packet.music);
    }

    public static PlayMusicPacket decode(PacketBuffer buf) {
        return new PlayMusicPacket(buf.readUtf());
    }

    public static void handle(PlayMusicPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(packet.music.equals("governor_start")){
                ClientMusicHandler.playGovernorBossMusic();
                ClientMusicHandler.stopCustomMusic(Minecraft.getInstance());
            }

            if(packet.music.equals("governor_stop")) {
                ClientMusicHandler.setGovernor(false);
                ClientMusicHandler.stopCustomMusic(Minecraft.getInstance());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

