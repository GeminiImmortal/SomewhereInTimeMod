package net.geminiimmortal.mobius.entity.goals;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.EnumSet;

public class ShootFireballGoal extends Goal {
    private final MobEntity mob;
    private PlayerEntity target;
    private final double fireballRange;
    private final int fireballCooldown;
    private int cooldown;

    public ShootFireballGoal(MobEntity mob, double fireballRange, int fireballCooldown) {
        this.mob = mob;
        this.fireballRange = fireballRange;
        this.fireballCooldown = fireballCooldown;
        this.cooldown = 0;

        // Prevent the mob from moving during this goal
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if(this.mob.getTarget() == null) return false;
        // Check if the mob has a target and if it's a player within range
        PlayerEntity potentialTarget = (PlayerEntity) this.mob.getTarget();
        assert potentialTarget != null;
        if (potentialTarget.isCreative()) return false;
        if (potentialTarget != null && potentialTarget.isAlive()) {
            double distance = this.mob.distanceToSqr(potentialTarget);
            if (distance <= fireballRange * fireballRange) {
                this.target = potentialTarget;
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        this.cooldown = 0;
        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public boolean canContinueToUse() {
        if(this.mob.getTarget() == null) return false;
        // Check if the mob has a target and if it's a player within range
        PlayerEntity potentialTarget = (PlayerEntity) this.mob.getTarget();
        assert potentialTarget != null;
        if (potentialTarget.isCreative()) return false;

        return this.target != null && this.target.isAlive() &&
                this.mob.distanceToSqr(this.target) <= fireballRange * fireballRange;
    }

    @Override
    public void tick() {
        if (this.target == null) return;

        // Look at the target
        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        // Handle cooldown logic
        if (this.cooldown > 0) {
            this.cooldown--; // Decrement cooldown only if it's greater than 0
            return;
        }

        // Fire a fireball if cooldown has expired
        World world = this.mob.level;
        double dx = this.target.getX() - this.mob.getX();
        double dy = this.target.getY(0.5) - (this.mob.getY(0.5) + 0.5);
        double dz = this.target.getZ() - this.mob.getZ();

        SmallFireballEntity fireball = new SmallFireballEntity(
                world, this.mob, dx, dy, dz
        );
        fireball.setPos(
                this.mob.getX() + dx / this.mob.distanceTo(this.target) * 0.5,
                this.mob.getY(0.5) + 0.5,
                this.mob.getZ() + dz / this.mob.distanceTo(this.target) * 0.5
        );
        world.addFreshEntity(fireball);
        world.playSound(null, this.mob.getX(), this.mob.getY(), this.mob.getZ(), SoundEvents.FIRECHARGE_USE, SoundCategory.HOSTILE, 1.0f, 1.0f);

        // Reset cooldown
        this.cooldown = this.fireballCooldown;
    }

}

