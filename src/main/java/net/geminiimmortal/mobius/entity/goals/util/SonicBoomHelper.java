package net.geminiimmortal.mobius.entity.goals.util;


import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SonicBoomHelper {
    /**
     * Spawns particle rings and applies damage and stun.
     * @param caster the entity casting the attack
     * @param target the target entity
     * @param world  the world in which to spawn particles
     */
    public static void doSonicBoom(LivingEntity caster, LivingEntity target, World world) {
        // Use server world for particle broadcasting
        if (!(world instanceof ServerWorld)) return;
        ServerWorld serverWorld = (ServerWorld) world;

        Vector3d start = caster.position().add(0, caster.getEyeHeight() * 0.5, 0);
        Vector3d end = target.position().add(0, target.getEyeHeight() * 0.5, 0);
        Vector3d direction = end.subtract(start);
        double totalDist = direction.length();
        Vector3d unitDir = direction.normalize();

        int rings = 3;
        int segments = 32;
        double baseRadius = 1.0D;
        int durationTicks = 40; // 2 seconds at 20 TPS

        // Spawn rings at evenly-spaced points along the line
        for (int i = 1; i <= rings; i++) {
            double fraction = (double) i / (rings + 1);
            Vector3d center = start.add(unitDir.scale(fraction * totalDist));
            double radius = baseRadius * i;

            for (int j = 0; j < segments; j++) {
                double angle = 2 * Math.PI * j / segments;
                double x = center.x + Math.cos(angle) * radius;
                double y = center.y;
                double z = center.z + Math.sin(angle) * radius;
                serverWorld.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, 0, 0, 0, 0);
            }
        }

        // Damage and stun
        target.hurt(DamageSource.MAGIC, 4.0F);
        // Apply heavy slowness to simulate stun
        target.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, durationTicks, 255, false, false));
    }
}

