package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.SorcererEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class CircleAroundPlayerGoal extends Goal {
    private final SorcererEntity sorcerer;
    private PlayerEntity targetPlayer;
    private final double radius;
    private final double speed;
    private final int updateInterval;
    private int tickCounter = 0;
    private double angle = 0;

    public CircleAroundPlayerGoal(SorcererEntity sorcerer, double radius, double speed, int updateInterval) {
        this.sorcerer = sorcerer;
        this.radius = radius;
        this.speed = speed;
        this.updateInterval = updateInterval;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    @Override
    public void start(){

    }

    @Override
    public boolean canUse() {
        // Check if the sorcerer has a valid target

        this.targetPlayer = this.sorcerer.level.getNearestPlayer(this.sorcerer, 20.0); // Adjust range as needed
        return this.targetPlayer != null;
    }

    @Override
    public boolean canContinueToUse() {
        // Continue circling as long as the player is valid
        return this.targetPlayer != null && this.targetPlayer.isAlive();
    }

    @Override
    public void tick() {
        super.tick();
        //if (tickCounter % updateInterval == 0) {
            // Calculate the next point on the circle
            angle += Math.PI / 16; // Adjust for smoother or sharper turns
            if (angle >= Math.PI * 2) {
                angle -= Math.PI * 2;
        //    }

            // Calculate the new position
            Vector3d targetPos = this.targetPlayer.position();
            double targetX = targetPos.x + radius * Math.cos(angle);
            double targetZ = targetPos.z + radius * Math.sin(angle);

            // Make the sorcerer move towards the calculated position
            this.sorcerer.getMoveControl().setWantedPosition(targetX, this.sorcerer.getY(), targetZ, this.speed);
        }

        tickCounter++;
    }

    @Override
    public void stop() {
        // Stop moving when the goal ends
        this.sorcerer.getNavigation().stop();
        tickCounter = 0;
        angle = 0;
    }
}

