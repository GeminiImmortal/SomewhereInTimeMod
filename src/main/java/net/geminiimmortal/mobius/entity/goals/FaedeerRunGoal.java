package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.FaedeerEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

public class FaedeerRunGoal extends Goal {
    private final FaedeerEntity deer;
    private final double fleeSpeed;
    private final double spookRadius;
    private PlayerEntity player;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected boolean isRunning;

    public FaedeerRunGoal(FaedeerEntity deer, double spookRadius, double fleeSpeed) {
        this.deer = deer;
        this.spookRadius = spookRadius;
        this.fleeSpeed = fleeSpeed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        this.player = this.deer.level.getNearestPlayer(this.deer, spookRadius);
        return this.player != null && !this.player.isCreative() && this.deer.distanceToSqr(player) < spookRadius * spookRadius;
    }

    @Override
    public void start() {
        this.deer.moveControl = new GroundPathNavigator(this.deer, this.deer.level);

        moveAwayFromPlayer();
        this.deer.setFleeing(true);
        this.deer.getNavigation().setCanFloat(true);
    }

    @Override
    public void stop() {
        this.player = null;
        this.deer.setFleeing(false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.deer.distanceTo(player) < spookRadius * 8) {
            moveAwayFromPlayer();
        } else {
            stop();
        }
    }


    @Override
    public boolean canContinueToUse() {
        return this.deer.distanceTo(this.player) < spookRadius * 8;
    }

    private void moveAwayFromPlayer() {
        double dx = this.deer.getX() - player.getX();
        double dz = this.deer.getZ() - player.getZ();

        double distance = MathHelper.sqrt(dx * dx + dz * dz);

        if (distance > 0) {
            // Calculate the yaw based on the direction to the player, and clamp it
            float targetYaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI)) - 90.0F;
            this.deer.setYBodyRot(MathHelper.clamp(targetYaw, -180.0F, 180.0F));
            this.deer.setYHeadRot(MathHelper.clamp(targetYaw, -180.0F, 180.0F));
        }

        this.deer.getMoveControl().setWantedPosition(this.deer.getX() + dx * 6, this.deer.getY(), this.deer.getZ() + dz * 6, this.fleeSpeed);
    }


}
