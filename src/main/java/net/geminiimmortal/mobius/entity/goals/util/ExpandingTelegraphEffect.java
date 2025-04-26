package net.geminiimmortal.mobius.entity.goals.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.vector.Vector3d;

public class ExpandingTelegraphEffect {
    private final Vector3d center;
    private final IParticleData particle;
    private final int maxTicks;
    private final double speed;
    private final int pointsPerRing;
    private int tick = 0;

    public ExpandingTelegraphEffect(Vector3d center, IParticleData particle, int maxTicks, double speed, int pointsPerRing) {
        this.center = center;
        this.particle = particle;
        this.maxTicks = maxTicks;
        this.speed = speed;
        this.pointsPerRing = pointsPerRing;
    }

    public void tick() {
        if (tick > maxTicks) return;

        ClientWorld level = Minecraft.getInstance().level;
        if (level == null) return;

        double radius = tick * speed;
        double angleStep = (2 * Math.PI) / pointsPerRing;

        for (int i = 0; i < pointsPerRing; i++) {
            double angle = i * angleStep;
            double dx = Math.cos(angle);
            double dz = Math.sin(angle);
            double px = center.x + dx * radius;
            double py = center.y + 0.01;
            double pz = center.z + dz * radius;

            level.addParticle(particle, px, py, pz, 0, 0, 0);
        }

        tick++;
    }

    public boolean isDone() {
        return tick > maxTicks;
    }
}

