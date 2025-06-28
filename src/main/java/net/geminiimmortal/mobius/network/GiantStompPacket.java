package net.geminiimmortal.mobius.network;

import net.geminiimmortal.mobius.entity.custom.GiantEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class GiantStompPacket {
    private final int giantId;

    public GiantStompPacket(int giantId) {
        this.giantId = giantId;
    }

    public static void encode(GiantStompPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.giantId);
    }

    public static GiantStompPacket decode(PacketBuffer buf) {
        return new GiantStompPacket(buf.readInt());
    }

    public static void handle(GiantStompPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null) {
                ServerWorld world = (ServerWorld) ctx.get().getSender().level;
                Entity entity = world.getEntity(msg.giantId);
                if (entity instanceof GiantEntity) {
                    ((GiantEntity) entity).stompAttack();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


