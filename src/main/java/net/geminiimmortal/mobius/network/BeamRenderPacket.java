package net.geminiimmortal.mobius.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;
import java.util.function.Supplier;

public class BeamRenderPacket {
    private final double startX, startY, startZ;
    private final double endX, endY, endZ;
    private final float density; // Controls how many particles per unit distance
    private final ResourceLocation particleRegistryName; // The registry name of the particle type

    public BeamRenderPacket(Vector3d start, Vector3d end, ParticleType<?> particleType, float density) {
        this.startX = start.x;
        this.startY = start.y;
        this.startZ = start.z;
        this.endX = end.x;
        this.endY = end.y;
        this.endZ = end.z;
        this.particleRegistryName = ForgeRegistries.PARTICLE_TYPES.getKey(particleType); // Get the registry name of the particle
        this.density = density;
    }

    // Encoding the packet data to a PacketBuffer
    public static void encode(BeamRenderPacket packet, PacketBuffer buf) {
        buf.writeDouble(packet.startX);
        buf.writeDouble(packet.startY);
        buf.writeDouble(packet.startZ);
        buf.writeDouble(packet.endX);
        buf.writeDouble(packet.endY);
        buf.writeDouble(packet.endZ);
        buf.writeFloat(packet.density);
        buf.writeResourceLocation(packet.particleRegistryName); // Write the registry name of the particle type
    }

    // Decoding the packet data from a PacketBuffer
    public static BeamRenderPacket decode(PacketBuffer buf) {
        Vector3d start = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        Vector3d end = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        float density = buf.readFloat();
        ResourceLocation particleRegistryName = buf.readResourceLocation(); // Read the registry name of the particle type
        ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(particleRegistryName); // Get the particle type using the registry name
        return new BeamRenderPacket(start, end, particleType, density);
    }

    public static void spawnBeamParticles(ClientWorld world, Vector3d start, Vector3d end, double density, ParticleType<?> particle) {
        Vector3d diff = end.subtract(start);
        double length = diff.length();
        Vector3d step = diff.normalize().scale(1.0 / density); // space between each particle
        double thickness = 0.5; // Controls how wide the beam will be (increase for thicker beam)

        // Random offsets for particle positions around the beam
        Random random = new Random();

        for (int i = 0; i < length * density; i++) {
            Vector3d pos = start.add(step.scale(i));

            // Add slight random offsets to make the beam appear thicker
            double offsetX = random.nextGaussian() * thickness;  // Random horizontal offset
            double offsetY = random.nextGaussian() * thickness;  // Random vertical offset
            double offsetZ = random.nextGaussian() * thickness;  // Random depth offset

            world.addParticle((IParticleData) particle, pos.x + offsetX, pos.y + offsetY, pos.z + offsetZ, 0, 0, 0); // Add particles with slight offsets
        }
    }

    public static void handle(BeamRenderPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        // Get the context from the Supplier
        NetworkEvent.Context context = contextSupplier.get();

        // Ensuring the packet is executed on the main thread for client-side logic
        context.enqueueWork(() -> {
            // Now we access the client world, since this is running on the client thread
            ClientWorld world = Minecraft.getInstance().level;

            // Get the start and end points for the beam
            Vector3d start = new Vector3d(packet.startX, packet.startY, packet.startZ);
            Vector3d end = new Vector3d(packet.endX, packet.endY, packet.endZ);

            // Spawn the beam particles with added thickness
            spawnBeamParticles(world, start, end, packet.density, ForgeRegistries.PARTICLE_TYPES.getValue(packet.particleRegistryName));

            // Add explosion at the end of the beam (impact point)
            // Make the explosion bigger by adjusting the power and other parameters
            world.explode(null, end.x, end.y, end.z, 10.0f, true, Explosion.Mode.NONE);
        });
    }

}
