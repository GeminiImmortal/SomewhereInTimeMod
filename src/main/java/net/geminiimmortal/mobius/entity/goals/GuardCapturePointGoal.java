package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.AbstractImperialEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

public class GuardCapturePointGoal extends Goal {
    private final AbstractImperialEntity entity;
    private final BlockPos origin;
    private final double maxDistance;
    private final double speed;

    public GuardCapturePointGoal(AbstractImperialEntity entity, BlockPos origin, double maxDistance, double speed) {
        this.entity = entity;
        this.origin = origin;
        this.maxDistance = maxDistance;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        if (entity.getTarget() != null && entity.getTarget().isAlive()) {
            return false;
        }

        double distanceSq = entity.blockPosition().distSqr(origin);
        return distanceSq > maxDistance * maxDistance;
    }

    @Override
    public void start() {
        entity.getNavigation().moveTo(origin.getX() + 0.5, origin.getY(), origin.getZ() + 0.5, speed);
    }

    @Override
    public boolean canContinueToUse() {
        return entity.getNavigation().isInProgress();
    }
}

