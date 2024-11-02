package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.SpadeGolemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

import static org.jline.terminal.MouseEvent.Modifier.Control;

public class GolemBackAwayGoal extends Goal {
    private final SpadeGolemEntity golem;
    private final double speed;
    private final double safeDistance; // Minimum distance from the player
    private PlayerEntity nearestPlayer;

    public GolemBackAwayGoal(SpadeGolemEntity golem, double speed, double safeDistance) {
        this.golem = golem;
        this.speed = speed;
        this.safeDistance = safeDistance;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        this.nearestPlayer = this.golem.level.getNearestPlayer(this.golem, safeDistance);
        return this.nearestPlayer != null && this.golem.distanceToSqr(nearestPlayer) < safeDistance * safeDistance;
    }

    @Override
    public void start() {
        moveAwayFromPlayer();
    }

    @Override
    public void stop() {
        this.nearestPlayer = null;
    }

    @Override
    public void tick() {
        // Keep backing away while the player is too close
        if (this.golem.distanceTo(nearestPlayer) < safeDistance) {
            moveAwayFromPlayer();
        }
    }

    private void moveAwayFromPlayer() {
        // Calculate the direction to move away from the player
        // Example of clamping rotation within valid bounds when moving the golem away
        double dx = this.golem.getX() - nearestPlayer.getX();
        double dz = this.golem.getZ() - nearestPlayer.getZ();

        double distance = MathHelper.sqrt(dx * dx + dz * dz);

        if (distance > 0) {
            // Calculate the yaw based on the direction to the player, and clamp it
            float targetYaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI)) - 90.0F;
            this.golem.setYBodyRot(MathHelper.clamp(targetYaw, -180.0F, 180.0F));
            this.golem.setYHeadRot(MathHelper.clamp(targetYaw, -180.0F, 180.0F));
        }


        // Move the golem in the opposite direction
        this.golem.getMoveControl().setWantedPosition(this.golem.getX() + dx, this.golem.getY(), this.golem.getZ() + dz, this.speed);
    }
}

