package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.FaedeerEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

public class LookForPlayerGoal extends Goal {
    private final FaedeerEntity deer;
    private final double detectionRadius;
    private PlayerEntity player;

    public LookForPlayerGoal(FaedeerEntity deer, double detectionRadius) {
        this.deer = deer;
        this.detectionRadius = detectionRadius;
    }

    @Override
    public boolean canUse() {
        this.player = this.deer.level.getNearestPlayer(this.deer, detectionRadius);
        return player != null;
    }

    @Override
    public void start() {
        this.deer.setAlerted(true);
    }

    @Override
    public void stop() {
        this.deer.setAlerted(false);
        this.player = null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.deer.distanceTo(this.player) < detectionRadius;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.deer.distanceTo(this.player) > detectionRadius) stop();
    }
}

