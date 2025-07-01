package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.AbstractImperialEntity;
import net.geminiimmortal.mobius.faction.IRankedImperial;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.List;

public class ImperialFollowPatrolLeaderGoal extends Goal {
    private final AbstractImperialEntity soldier;
    private AbstractImperialEntity leader;
    private final double speed;
    private final double minDistance = 4.0D;
    private final double maxDistance = 16.0D;
    private final double stopDistance = 2.0D;
    private final double followDistance = 6.0D;

    public ImperialFollowPatrolLeaderGoal(AbstractImperialEntity soldier, double speed) {
        this.soldier = soldier;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (soldier.getRank().equals(IRankedImperial.Rank.OFFICER)) return false;
        if (!soldier.isPatrolMember()) return false;

        List<AbstractImperialEntity> nearby = soldier.level.getEntitiesOfClass(
                AbstractImperialEntity.class,
                soldier.getBoundingBox().inflate(maxDistance),
                e -> e != soldier && e.getRank().equals(IRankedImperial.Rank.OFFICER) && e.isAlive()
        );

        if (!nearby.isEmpty()) {
            leader = nearby.get(0); // could improve with closest selection
            return true;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return leader != null &&
                leader.isAlive() &&
                !soldier.getNavigation().isDone() &&
                soldier.distanceToSqr(leader) > stopDistance * stopDistance;
    }

    @Override
    public void start() {
        moveToLeader();
    }

    @Override
    public void stop() {
        leader = null;
        soldier.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (leader != null && soldier.distanceToSqr(leader) > followDistance * followDistance) {
            moveToLeader();
        }
    }

    private void moveToLeader() {
        if (leader != null) {
            soldier.getNavigation().moveTo(leader, speed);
        }
    }
}

