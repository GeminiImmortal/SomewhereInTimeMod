package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.ClubGolemEntity;
import net.geminiimmortal.mobius.entity.custom.HeartGolemEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.List;

public class DPSSupportOtherGolemsGoal extends Goal {

    private final ClubGolemEntity clubsGolem;
    private HeartGolemEntity heartsGolem;
    private final double speedModifier;
    private final float followDistance;
    private final float minDistance; // Minimum distance to maintain from the Hearts Golem
    private int delayCounter;

    public DPSSupportOtherGolemsGoal(ClubGolemEntity clubsGolem, double speedModifier, float followDistance, float minDistance) {
        this.clubsGolem = clubsGolem;
        this.speedModifier = speedModifier;
        this.followDistance = followDistance;
        this.minDistance = minDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // Find the nearest Hearts Golem within the followDistance
        List<HeartGolemEntity> heartsGolems = clubsGolem.level.getEntitiesOfClass(HeartGolemEntity.class,
                this.clubsGolem.getBoundingBox().inflate(followDistance));

        if (heartsGolems.isEmpty()) {
            return false; // No Hearts Golem nearby
        }

        for (HeartGolemEntity foundHeartsGolem : heartsGolems) {
            if (foundHeartsGolem != null && foundHeartsGolem.isAlive()) {
                this.heartsGolem = foundHeartsGolem;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.heartsGolem == null || !this.heartsGolem.isAlive()) {
            return false;
        }

        double distanceSq = this.clubsGolem.distanceToSqr(this.heartsGolem);
        return distanceSq > (double)(this.minDistance * this.minDistance) &&
                distanceSq < (double)(this.followDistance * this.followDistance);
    }

    @Override
    public void start() {
        this.delayCounter = 0;
    }

    @Override
    public void stop() {
        this.heartsGolem = null;
        this.clubsGolem.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (--this.delayCounter <= 0) {
            this.delayCounter = 10; // Navigation delay, adjust as needed

            double distanceSq = this.clubsGolem.distanceToSqr(this.heartsGolem);

            // Move only if the Diamonds Golem is further than the minDistance
            if (distanceSq > (double)(this.minDistance * this.minDistance)) {
                this.clubsGolem.getNavigation().moveTo(this.heartsGolem, this.speedModifier);
            } else {
                this.clubsGolem.getNavigation().stop(); // Stop moving if too close
            }

            // Look at the Hearts Golem while following
            this.clubsGolem.getLookControl().setLookAt(this.heartsGolem, 30.0F, 30.0F);
        }
    }
}
