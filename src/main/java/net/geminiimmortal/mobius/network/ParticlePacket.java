package net.geminiimmortal.mobius.network;

import net.geminiimmortal.mobius.particle.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ParticlePacket {
    private final double x;
    private final double y;
    private final double z;
    private final String particleType;

    public ParticlePacket(double x, double y, double z, String particleType) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.particleType = particleType;
    }

    public static void encode(ParticlePacket packet, PacketBuffer buf) {
        buf.writeDouble(packet.x);
        buf.writeDouble(packet.y);
        buf.writeDouble(packet.z);
        buf.writeUtf(packet.particleType);
    }

    public static ParticlePacket decode(PacketBuffer buf) {
        return new ParticlePacket(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readUtf());
    }

    public static void handle(ParticlePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Handle the packet on the client side (rendering the particles)
            // You can use Minecraft.getInstance() here to access the client
            // For example, call a method to spawn particles
            spawnParticles(packet.x, packet.y, packet.z, packet.particleType);
        });
        ctx.get().setPacketHandled(true);
    }

    private static void spawnParticles(double x, double y, double z, String particleType) {
        // Example for spawning particles
        // Replace with your specific particle spawning logic
        if (particleType.equals("HAPPY_VILLAGER")) {
            // Spawn flame particles at the specified location
            assert Minecraft.getInstance().level != null;
            Minecraft.getInstance().level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0.0D, 0.05D, 0.0D);
        }

        if (particleType.equals("knife_rain")) {
            assert Minecraft.getInstance().level != null;
            Minecraft.getInstance().level.addParticle(ModParticles.KNIFE_RAIN_PARTICLE.get(), x, y, z, 0.0D, 0.0D, 0.0D);
        }

        if (particleType.equals("tornado")) {
            assert Minecraft.getInstance().level != null;
            Minecraft.getInstance().level.addParticle(ParticleTypes.SWEEP_ATTACK, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}

