package net.geminiimmortal.mobius.entity.goals;


import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.GovernorCloneEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class CloneExplodeOnProximityGoal extends Goal {

    private final GovernorCloneEntity clone;
    private final double detonationRange;

    public CloneExplodeOnProximityGoal(GovernorCloneEntity clone, double detonationRange) {
        this.clone = clone;
        this.detonationRange = detonationRange;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        List<PlayerEntity> players = clone.level.getEntitiesOfClass(PlayerEntity.class, clone.getBoundingBox().inflate(detonationRange));
        return !players.isEmpty();
    }

    @Override
    public void start() {
            int delayTicks = new Random().nextInt(30);
            this.clone.level.getServer().tell(new TickDelayedTask(this.clone.level.getServer().getTickCount() + delayTicks, () -> {
                clone.startCountdown();
                clone.explode();
            }));
    }
}

