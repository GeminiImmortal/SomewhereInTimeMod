package net.geminiimmortal.mobius.network;

import net.geminiimmortal.mobius.entity.goals.util.ArcaneRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BeamEndPacket {
    private final BlockPos pos;

    public BeamEndPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(BeamEndPacket packet, PacketBuffer buf) {
        buf.writeBlockPos(packet.pos);
    }

    public static BeamEndPacket decode(PacketBuffer buf) {
        return new BeamEndPacket(buf.readBlockPos());
    }

    public static void handle(BeamEndPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
            //    ArcaneRenderer.stop(); // Start the rendering effect
                mc.level.playLocalSound(packet.pos, SoundEvents.GENERIC_EXPLODE, SoundCategory.HOSTILE, 2f, 0.5f, false);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

