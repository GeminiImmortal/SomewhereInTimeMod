package net.geminiimmortal.mobius.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;

import java.util.function.Supplier;

public class GovernorCursePacket {
    private final int entityId;

    public GovernorCursePacket(int entityId) {
        this.entityId = entityId;
    }

    public static void encode(GovernorCursePacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
    }

    public static GovernorCursePacket decode(PacketBuffer buf) {
        return new GovernorCursePacket(buf.readInt());
    }

    public static void handle(GovernorCursePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && mc.level.getEntity(msg.entityId) instanceof GovernorEntity) {
                showGovernorOverlay((GovernorEntity) mc.level.getEntity(msg.entityId));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void showGovernorOverlay(GovernorEntity governor) {
        // Trigger the overlay rendering effect
        net.geminiimmortal.mobius.render.overlay.GovernorOverlayRenderer.trigger(governor, 30);
    }
}



