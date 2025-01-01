package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.SorcererEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;

public class SorcererBackAwayGoal extends Goal {
    private final SorcererEntity sorcerer;
    private final double speed;
    private final double safeDistance; // Minimum distance from the player
    private PlayerEntity nearestPlayer;

    public SorcererBackAwayGoal(SorcererEntity sorcerer, double speed, double safeDistance) {
        this.sorcerer = sorcerer;
        this.speed = speed;
        this.safeDistance = safeDistance;
    //    this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        this.nearestPlayer = this.sorcerer.level.getNearestPlayer(this.sorcerer, safeDistance);
        return this.nearestPlayer != null && this.sorcerer.distanceToSqr(nearestPlayer) < safeDistance * safeDistance;
    }

    @Override
    public void start() {
        this.sorcerer.setFleeing(true);
        moveAwayFromPlayer();
    }

    @Override
    public void stop() {
        this.nearestPlayer = null;
        this.sorcerer.setFleeing(false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.sorcerer.distanceTo(nearestPlayer) < safeDistance * 0.9) { // Add a buffer
            moveAwayFromPlayer();
        }
    }


    private void moveAwayFromPlayer() {
        // Calculate the direction to move away from the player
        // Example of clamping rotation within valid bounds when moving the sorcerer away
        double dx = this.sorcerer.getX() - nearestPlayer.getX();
        double dt = nearestPlayer.getX();
        double ty = nearestPlayer.getY();
        double dz = this.sorcerer.getZ() - nearestPlayer.getZ();
        double tz = nearestPlayer.getZ();
    /*
        double distance = MathHelper.sqrt(dx * dx + dz * dz);

        if (distance > 0) {
            // Calculate the yaw based on the direction to the player, and clamp it
            float targetYaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI)) - 90.0F;
            this.sorcerer.setYBodyRot(MathHelper.clamp(targetYaw, -180.0F, 180.0F));
            this.sorcerer.setYHeadRot(MathHelper.clamp(targetYaw, -180.0F, 180.0F));
        }


        // Move the sorcerer in the opposite direction
        this.sorcerer.getMoveControl().setWantedPosition(this.sorcerer.getX() + dx, this.sorcerer.getY(), this.sorcerer.getZ() + dz, this.speed); */
        nearestPlayer.hurt(DamageSource.MAGIC, 2.5f);
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

