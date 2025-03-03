package net.geminiimmortal.mobius.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Supplier;

public class ClientPlaySoundPacket {
    private final ResourceLocation sound;
    private final float volume;
    private final float pitch;

    public ClientPlaySoundPacket(ResourceLocation sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public static void encode(ClientPlaySoundPacket msg, PacketBuffer buf) {
        buf.writeResourceLocation(msg.sound);
        buf.writeFloat(msg.volume);
        buf.writeFloat(msg.pitch);
    }

    public static ClientPlaySoundPacket decode(PacketBuffer buf) {
        return new ClientPlaySoundPacket(buf.readResourceLocation(), buf.readFloat(), buf.readFloat());
    }

    public static void handle(ClientPlaySoundPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Ensure this runs on the client
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.playSound(
                    Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(msg.sound)),
                    msg.volume,
                    msg.pitch
            );
        });
        ctx.get().setPacketHandled(true);
    }
}

