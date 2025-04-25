package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.SorcererEntity;
import net.geminiimmortal.mobius.entity.goals.util.TrackingLaserBeam;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

public class LaserTrackerAttackGoal extends Goal {
    private final SorcererEntity sorcerer;
    private final int duration;
    private final int cooldownDuration;
    private LivingEntity cachedTarget; // Add this field
    private int cooldownTimer = 0;
    private int laserTimer = 0;
    private TrackingLaserBeam currentBeam;

    public LaserTrackerAttackGoal(SorcererEntity sorcerer, int duration, int cooldown) {
        this.sorcerer = sorcerer;
        this.duration = duration;
        this.cooldownDuration = cooldown;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = sorcerer.getTarget();
        return cooldownTimer <= 0 && target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return laserTimer > 0 &&
                cachedTarget != null &&
                cachedTarget.isAlive() &&
                sorcerer.distanceToSqr(cachedTarget) < 50 * 50; // Within follow range
    }

    @Override
    public void start() {
        this.cachedTarget = sorcerer.getTarget(); // Store target when attack starts
        this.laserTimer = duration;

        if (cachedTarget != null && cachedTarget.isAlive()) {
            currentBeam = new TrackingLaserBeam(
                    sorcerer.level,
                    sorcerer,
                    () -> cachedTarget, // Use cached target instead of dynamic lookup
                    duration,
                    2f
            );
            sorcerer.setCasting(true);
        }
    }

    @Override
    public void tick() {
        LivingEntity target = sorcerer.getTarget();

        if (cachedTarget == null || !cachedTarget.isAlive()) {
            stop();
            return;
        }

        if (--laserTimer <= 0) {
            stop();
            return;
        }

        if (currentBeam != null) {
            currentBeam.tick();
        }
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void stop() {
        cooldownTimer = cooldownDuration;
        laserTimer = 0;
        sorcerer.setCasting(false);
        currentBeam = null;
    }

    public void tickCooldown() {
        if (cooldownTimer > 0) cooldownTimer--;
    }
}
