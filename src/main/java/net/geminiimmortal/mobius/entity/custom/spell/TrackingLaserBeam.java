package net.geminiimmortal.mobius.entity.custom.spell;

import net.geminiimmortal.mobius.entity.custom.SpellEntity;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class TrackingLaserBeam implements SpellTypeEntity {
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

    private void bounceOffBarrier(BarrierEntity barrier) {
        if (!level.isClientSide) {
            Vector3d soundPos = barrier.position();
            level.playSound(null, soundPos.x, soundPos.y, soundPos.z, ModSounds.BARRIER_IMPACT.get(), SoundCategory.HOSTILE, 50.0F, 0.8F);
        }
    }

    @Override
    public void onCollideWith(SpellTypeEntity other) {

    }

    @Override
    public SpellType getSpellType() {
        return SpellType.OFFENSIVE;
    }

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

        Entity target = targetSupplier.get();
        if (target == null || !target.isAlive()) return;

        Vector3d eyePos = target.getEyePosition(1.0f);
        targetPositionHistory.addLast(eyePos);

        int delayTicks = getCurrentDelayTicks();

        while (targetPositionHistory.size() > delayTicks) {
            targetPositionHistory.removeFirst();
        }

        if (targetPositionHistory.size() >= delayTicks) {
            Vector3d delayedAim = targetPositionHistory.peekFirst();
            Vector3d from = caster.position().add(0, beamHeight, 0);

            Vector3d smoothedTarget = interpolate(delayedAim, targetPositionHistory.getLast(), 0.5);
            Vector3d to = smoothedTarget;

            Vector3d barrierHit = getBarrierCollisionPoint(from, smoothedTarget);
            if (barrierHit != null) {
                to = barrierHit;
            }

            spawnWideLaserBeam(from, to);


            // Check collision with the world (blocks)
            if (!isCollisionOnPath(from, smoothedTarget)) {
                // Check collision with a BarrierEntity
                BarrierEntity barrier = getHitBarrier(from, smoothedTarget);
                if (barrier != null) {
                    if (ticksAlive % 6 == 0 && !this.level.isClientSide) {
                        Vector3d soundPos = target.position();
                        level.playSound(null, soundPos.x, soundPos.y, soundPos.z, ModSounds.BARRIER_NEGATE.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
                    }
                    spawnImpactParticles(barrierHit);
                    bounceOffBarrier(barrier);
                } else {
                    // If no barrier, check hitting the target
                    if (isHittingTarget(from, smoothedTarget, (LivingEntity) target)) {
                        target.hurt(DamageSource.indirectMagic(caster, caster), this.damageAmount);
                    }
                }
            }
        }

        // Sound logic (unchanged)
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

    private boolean isHittingBarrier(Vector3d from, Vector3d to, BarrierEntity barrier) {
        AxisAlignedBB targetBox = barrier.getBoundingBox().inflate(0.25);
        return targetBox.clip(from, to).isPresent();
    }

    private boolean isCollisionOnPath(Vector3d from, Vector3d to) {
        RayTraceResult result = level.clip(new RayTraceContext(from, to, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, caster));
        return result.getType() != RayTraceResult.Type.MISS;
    }


    @Nullable
    private BarrierEntity getHitBarrier(Vector3d from, Vector3d to) {
        AxisAlignedBB laserBox = new AxisAlignedBB(from, to).inflate(0.25);
        List<Entity> entities = level.getEntities(null, laserBox);

        for (Entity entity : entities) {
            if (entity instanceof BarrierEntity) {
                BarrierEntity barrier = (BarrierEntity) entity;
                if (isHittingBarrier(from, to, barrier)) {
                    return barrier;
                }
            }
        }
        return null;
    }

    @Nullable
    private Vector3d getBarrierCollisionPoint(Vector3d from, Vector3d to) {
        AxisAlignedBB laserBox = new AxisAlignedBB(from, to).inflate(0.25);
        List<Entity> entities = level.getEntities(null, laserBox);

        for (Entity entity : entities) {
            if (entity instanceof BarrierEntity) {
                AxisAlignedBB barrierBox = entity.getBoundingBox().inflate(0.25);
                Optional<Vector3d> hitResult = barrierBox.clip(from, to);
                if (hitResult.isPresent()) {
                    return hitResult.get(); // Return the exact collision point
                }
            }
        }
        return null; // No collision
    }

    private void spawnImpactParticles(Vector3d impactPoint) {
        if (level.isClientSide || !(level instanceof ServerWorld)) return;

        ServerWorld serverWorld = (ServerWorld) level;
        int particleCount = 20;
        double baseSpeed = 0.5;

        Random random = serverWorld.getRandom();

        for (int i = 0; i < particleCount; i++) {
            // Random point on a sphere
            double theta = random.nextDouble() * 2 * Math.PI;
            double phi = Math.acos(2 * random.nextDouble() - 1);

            double xSpeed = Math.sin(phi) * Math.cos(theta) * baseSpeed * (0.5 + random.nextDouble() * 0.5);
            double ySpeed = Math.sin(phi) * Math.sin(theta) * baseSpeed * (0.5 + random.nextDouble() * 0.5);
            double zSpeed = Math.cos(phi) * baseSpeed * (0.5 + random.nextDouble() * 0.5);

            serverWorld.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    impactPoint.x,
                    impactPoint.y,
                    impactPoint.z,
                    1,
                    xSpeed, ySpeed, zSpeed, 1.0
            );
        }
    }






}
