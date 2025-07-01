package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.AbstractImperialEntity;
import net.geminiimmortal.mobius.faction.IRankedImperial;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class ImperialOfficerLeadPatrolGoal extends Goal {
    private final AbstractImperialEntity leader;
    private final double speed;
    private int cooldown;

    public ImperialOfficerLeadPatrolGoal(AbstractImperialEntity leader, double speed) {
        this.leader = leader;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return leader.getRank().equals(IRankedImperial.Rank.OFFICER) && leader.getTarget() == null && cooldown-- <= 0 && leader.isPatrolMember();
    }

    @Override
    public void start() {
        cooldown = 100 + leader.getRandom().nextInt(60); // cooldown before next move
        Vector3d vec = RandomPositionGenerator.getLandPos(leader, 20, 15);
        if (vec != null) {
            leader.getNavigation().moveTo(vec.x, vec.y, vec.z, speed);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return leader.getNavigation().isInProgress();
    }
}

