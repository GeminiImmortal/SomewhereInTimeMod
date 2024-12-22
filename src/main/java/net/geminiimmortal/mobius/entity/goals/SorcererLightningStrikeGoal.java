package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.SorcererEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.LightningBoltEntity;

public class SorcererLightningStrikeGoal extends Goal {
    private final SorcererEntity sorcerer;
    private final int strikeCooldown;
    private final double maxRange;
    private int cooldownTimer;

    public SorcererLightningStrikeGoal(SorcererEntity sorcerer, int strikeCooldown, double maxRange) {
        this.sorcerer = sorcerer;
        this.strikeCooldown = strikeCooldown;
        this.maxRange = maxRange;
        this.cooldownTimer = 0;
    }

    @Override
    public boolean canUse() {
        // Check if cooldown is over and the sorcerer has a target
        LivingEntity target = this.sorcerer.getTarget();
        return target != null && this.cooldownTimer <= 0 && this.sorcerer.distanceTo(target) <= maxRange;
    }

    @Override
    public void start() {
        LivingEntity target = this.sorcerer.getTarget();
        if (target != null) {
            // Strike lightning at the target's position
            strikeLightning(target.getX(), target.getY(), target.getZ());

            // Reset cooldown
            this.cooldownTimer = this.strikeCooldown;
        }
    }

    @Override
    public void tick() {
        // Reduce the cooldown timer each tick
        if (this.cooldownTimer > 0) {
            this.cooldownTimer--;
        }
    }

    private void strikeLightning(double x, double y, double z) {
        // Create and spawn a lightning bolt entity
        LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(this.sorcerer.level);
        if (lightning != null) {
            lightning.moveTo(x, y, z, 0.0f, 0.0f); // Set position
            this.sorcerer.level.addFreshEntity(lightning);
        }
    }
}

