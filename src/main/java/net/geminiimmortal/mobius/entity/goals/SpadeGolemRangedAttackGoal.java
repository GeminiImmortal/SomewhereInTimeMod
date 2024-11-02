package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.ClubGolemEntity;
import net.geminiimmortal.mobius.entity.custom.DiamondGolemEntity;
import net.geminiimmortal.mobius.entity.custom.HeartGolemEntity;
import net.geminiimmortal.mobius.entity.custom.SpadeGolemEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;

public class SpadeGolemRangedAttackGoal extends Goal {
    private final SpadeGolemEntity mob; // The golem (attacker)
    private final double speed;  // Movement speed towards target
    private final int attackCooldown; // Cooldown between attacks
    private final float maxAttackRange; // Maximum range of attack
    private int attackTimer;// Timer for controlling attack cooldown
    private final float safetyRange;

    public SpadeGolemRangedAttackGoal(SpadeGolemEntity mob, double speed, int attackCooldown, float maxAttackRange, float safetyRange) {
        this.mob = mob;
        this.speed = speed;
        this.attackCooldown = attackCooldown;
        this.maxAttackRange = maxAttackRange;
        this.attackTimer = -1; // Initialize the attack timer
        this.safetyRange = safetyRange;
    }

    @Override
    public boolean canUse() {
        // Check if the golem has a target and if the target is alive
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive() || mob.distanceTo(target) < safetyRange) {
            return false;
        }
        return true;

    }

    @Override
    public void start() {
        this.attackTimer = 0; // Reset the attack timer when the goal starts
    }

    @Override
    public void stop() {
        this.attackTimer = -1; // Reset the attack timer when the goal stops
    }

    @Override
    public boolean canContinueToUse() {
        // Continue if the target is still alive and within range
        LivingEntity target = this.mob.getTarget();
        return target != null && target.isAlive() && this.mob.distanceToSqr(target) <= maxAttackRange * maxAttackRange;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();

        if (target == null) return;
        if (!this.mob.canSee(target)) return;

        double distanceToTarget = this.mob.distanceToSqr(target);

        boolean canSeeTarget = this.mob.canSee(target);

        if (canSeeTarget && distanceToTarget <= maxAttackRange * maxAttackRange) {
            this.mob.getNavigation().stop(); // Stop moving if in range and can see target

            if (this.attackTimer <= 0) {
                // Check for line of sight and whether any allies are in the way
                //if (isSafeToAttack(target)) {
                    this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
                    // Fire the ranged attack (e.g., shoot crossbow bolts)
                    performRangedAttack(target);
                //}
                this.attackTimer = attackCooldown; // Reset attack cooldown
            }
        } else {
            // Move closer to the target if out of range or cannot see target
            this.mob.getNavigation().moveTo(target, this.speed);
        }

        if (this.attackTimer > 0) {
            this.attackTimer--;
        }
        super.tick();
    }

    private boolean isSafeToAttack(LivingEntity target) {
        // Check if there is a clear line of sight between the golem and the target
        Vector3d startPos = new Vector3d(this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        Vector3d endPos = new Vector3d(target.getX(), target.getEyeY(), target.getZ());
        World currentWorld = this.mob.level;

        // Raytrace between the golem and the target
        BlockRayTraceResult result = new BlockRayTraceResult(
                startPos, this.mob.getDirection(), target.getEntity().blockPosition(), true);

        // If raytrace doesn't hit anything (no blocks in the way), check for allies
        if (result.getType() != RayTraceResult.Type.MISS) {
            return false; // Something is in the way, so it's not safe to attack
        }

        AxisAlignedBB attackBox = new AxisAlignedBB(startPos, endPos); // Create a bounding box for the line of sight
        List<Entity> nearbyEntities = this.mob.level.getEntitiesOfClass(Entity.class,
                attackBox, // Check entities inside this box
                entity -> isAlly(entity) && entity.isAlive() && entity != this.mob); // Custom method to check for allies

        // Ensure no allies are in the direct path between the golem and the target
        for (Entity entity : nearbyEntities) {
            if (entity.getBoundingBox().intersects(attackBox)) {
                return false; // An ally is in the way, so it's not safe to attack
            }
        }

        return true; // No blocks or allies in the way, it's safe to attack
    }

    private boolean isAlly(Entity entity) {
        // Check if the entity is an ally of the golem (could be other golems or players)
        // Customize this method depending on your criteria for allies
        return entity instanceof ClubGolemEntity || entity instanceof DiamondGolemEntity || entity instanceof HeartGolemEntity || entity instanceof SpadeGolemEntity && entity != this.mob;
    }

    private void performRangedAttack(LivingEntity target) {
        // Perform the actual ranged attack here (e.g., shoot a crossbow bolt)
        // This example assumes the golem fires crossbow bolts
        ArrowEntity bolt = new ArrowEntity(this.mob.level, this.mob);
        double dx = target.getX() - this.mob.getX();
        double dy = target.getY(0.3333333333333333D) - bolt.getY();
        double dz = target.getZ() - this.mob.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);
        this.mob.level.addFreshEntity(bolt);
        bolt.setDeltaMovement(dx, dy + distance * 0.20000000298023224D, dz);

    }
}

