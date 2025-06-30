package net.geminiimmortal.mobius.network;

import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SInfamySyncPacket {
    private final int infamy;

    public SInfamySyncPacket(int infamy) {
        this.infamy = infamy;
    }

    public static void encode(SInfamySyncPacket msg, PacketBuffer buf) {
        buf.writeVarInt(msg.infamy);
    }

    public static SInfamySyncPacket decode(PacketBuffer buf) {
        return new SInfamySyncPacket(buf.readVarInt());
    }

    public static void handle(SInfamySyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                mc.player.getCapability(ModCapabilities.INFAMY_CAPABILITY).ifPresent(cap -> {
                    cap.setInfamy(msg.infamy);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

