package net.geminiimmortal.mobius.entity.goals.util;

import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.function.Supplier;

public class TrackingLaserBeam {
    private final World level;
    private final Entity caster;
    private final Supplier<? extends LivingEntity> targetSupplier;
    private final int duration;
    private int ticksAlive = 0;
    private final double beamHeight = 2.0;
    private final float damageAmount;

    private final Deque<Vector3d> targetPositionHistory = new ArrayDeque<>();
    private final int maxDelayTicks = 10;
    private final int minDelayTicks = 2;

    public TrackingLaserBeam(World level, Entity caster, Supplier<? extends LivingEntity> targetSupplier, int duration, float damageAmount) {
        this.level = level;
        this.caster = caster;
        this.targetSupplier = targetSupplier;
        this.duration = duration;
        this.damageAmount = damageAmount;
    }

   /* public boolean isAlive() {
        return ticksAlive <= 0;
    }*/


    private Vector3d interpolate(Vector3d from, Vector3d to, double t) {
        double x = from.x + (to.x - from.x) * t;
        double y = from.y + (to.y - from.y) * t;
        double z = from.z + (to.z - from.z) * t;
        return new Vector3d(x, y, z);
    }

    private int getCurrentDelayTicks() {
        return MathHelper.clamp(maxDelayTicks - ticksAlive / 20, minDelayTicks, maxDelayTicks);
    }

    public void tick() {
        ticksAlive++;

        LivingEntity target = targetSupplier.get();
        if (target == null || !target.isAlive()) return;

        // Record eye position to track dodging more fairly
        Vector3d eyePos = target.getEyePosition(1.0f);
        targetPositionHistory.addLast(eyePos);

        int delayTicks = getCurrentDelayTicks();

        // Cap history size
        while (targetPositionHistory.size() > delayTicks) {
            targetPositionHistory.removeFirst();
        }

        if (targetPositionHistory.size() >= delayTicks) {
            Vector3d delayedAim = targetPositionHistory.peekFirst();
            Vector3d from = caster.position().add(0, beamHeight, 0);

            // Smooth visual: interpolate between oldest and newest
            Vector3d smoothedTarget = interpolate(delayedAim, targetPositionHistory.getLast(), 0.5);
            spawnWideLaserBeam(from, smoothedTarget);

            if (isHittingTarget(from, smoothedTarget, target)) {
                target.hurt(DamageSource.indirectMagic(caster, caster), this.damageAmount);
            }
        }

        if (ticksAlive == 1 && !level.isClientSide) {
            Vector3d soundPos = target.position();
            level.playSound(null, soundPos.x, soundPos.y, soundPos.z, ModSounds.ARCANE_BOLT_FX.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
        }

        if (ticksAlive % 10 == 0 && !level.isClientSide) {
            Vector3d soundPos = target.position();
            level.playSound(null, soundPos.x, soundPos.y, soundPos.z, ModSounds.ARCANE_BOLT_FX.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
        }

        if (ticksAlive % 4 == 0 && !level.isClientSide) {
            Vector3d soundPos = target.position();
            level.playSound(null, soundPos.x, soundPos.y, soundPos.z, SoundEvents.GENERIC_EXPLODE, SoundCategory.HOSTILE, 1.0F, 1.0F);
        }

    }

    private void spawnWideLaserBeam(Vector3d start, Vector3d end) {
        if (!level.isClientSide && level instanceof ServerWorld) {
            int steps = 30;
            double beamRadius = 0.3;
            Random rand = level.getRandom();

            for (int i = 0; i < steps; i++) {
                double t = i / (double) steps;
                Vector3d point = interpolate(start, end, t);

                // Fewer particles for better performance
                for (int j = 0; j < 4; j++) {
                    double angle = (Math.PI * 2) * (j / 4.0);
                    double dx = Math.cos(angle) * beamRadius * rand.nextDouble();
                    double dz = Math.sin(angle) * beamRadius * rand.nextDouble();

                    ((ServerWorld) level).sendParticles(ParticleTypes.SWEEP_ATTACK,
                            point.x + dx,
                            point.y,
                            point.z + dz,
                            2, 0, 0, 0, 0.0
                    );
                }
            }

            Vector3d endFlare = interpolate(start, end, 1.0);
            ((ServerWorld) level).sendParticles(ParticleTypes.EXPLOSION, endFlare.x, endFlare.y, endFlare.z, 5, 0, 0, 0, 0.0);
        }
    }

    private boolean isHittingTarget(Vector3d from, Vector3d to, LivingEntity target) {
        AxisAlignedBB targetBox = target.getBoundingBox().inflate(0.25);
        return targetBox.clip(from, to).isPresent();
    }
}
