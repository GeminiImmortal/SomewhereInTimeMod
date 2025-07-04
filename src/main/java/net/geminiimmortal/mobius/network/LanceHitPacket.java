package net.geminiimmortal.mobius.network;

import net.geminiimmortal.mobius.entity.custom.AbstractImperialEntity;
import net.geminiimmortal.mobius.entity.custom.ImperialCommanderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class LanceHitPacket {
    private final int imperialId;

    public LanceHitPacket(int imperialId) {
        this.imperialId = imperialId;
    }

    public static void encode(LanceHitPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.imperialId);
    }

    public static LanceHitPacket decode(PacketBuffer buf) {
        return new LanceHitPacket(buf.readInt());
    }

    public static void handle(LanceHitPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null) {
                ServerWorld world = (ServerWorld) ctx.get().getSender().level;
                Entity entity = world.getEntity(msg.imperialId);
                if (entity instanceof AbstractImperialEntity) {
                    if (entity instanceof ImperialCommanderEntity) {
                        ((ImperialCommanderEntity) entity).lanceHit();
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


