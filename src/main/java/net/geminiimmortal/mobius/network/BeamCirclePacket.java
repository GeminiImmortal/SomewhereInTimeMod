package net.geminiimmortal.mobius.network;

import net.geminiimmortal.mobius.entity.goals.util.ExpandingTelegraphEffect;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BeamCirclePacket {
    private final BlockPos pos;

    public BeamCirclePacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(BeamCirclePacket packet, PacketBuffer buf) {
        buf.writeBlockPos(packet.pos);
    }

    public static BeamCirclePacket decode(PacketBuffer buf) {
        return new BeamCirclePacket(buf.readBlockPos());
    }

    public static void handle(BeamCirclePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
            //    ArcaneRenderer.startCircle(packet.pos); // Start the rendering effect
                mc.level.playLocalSound(packet.pos, ModSounds.AOE_TELEGRAPH_FX.get(), SoundCategory.HOSTILE, 1f, 1f, false);
                mc.level.playLocalSound(packet.pos, ModSounds.ARCANE_NUKE_FX.get(), SoundCategory.HOSTILE, 2f, 0.5f, false);

            }
            ExpandingTelegraphEffect effect = new ExpandingTelegraphEffect(
                    new Vector3d(packet.pos.getX(), packet.pos.getY(), packet.pos.getZ()),               // beam impact position
                    ParticleTypes.FIREWORK,              // particle type
                    30,                              // duration in ticks
                    0.2,                             // expansion speed per tick
                    80                               // particles per ring
            );
            ClientEffectHandler.spawnExpandingTelegraph(new Vector3d(packet.pos.getX(), packet.pos.getY(), packet.pos.getZ()));

        });
        ctx.get().setPacketHandled(true);
    }
}

