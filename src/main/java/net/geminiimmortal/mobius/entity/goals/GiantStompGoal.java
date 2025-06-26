package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.GiantEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

import java.util.List;

public class GiantStompGoal extends MeleeAttackGoal {
    private final GiantEntity giant;

    public GiantStompGoal(GiantEntity giant, double speedModifier, boolean pauseWhenMobIdle) {
        super(giant, speedModifier, pauseWhenMobIdle);
        this.giant = giant;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity p_190102_1_, double p_190102_2_) {
        double d0 = this.getAttackReachSqr(p_190102_1_);
        if (p_190102_2_ <= d0 && this.getTicksUntilNextAttack() <= 0) {
            this.giant.setAttacking(true);
            this.resetAttackCooldown();
        }

    }

    @Override
    public void stop() {
        super.stop();
        this.giant.setAttacking(false);
    }
}

