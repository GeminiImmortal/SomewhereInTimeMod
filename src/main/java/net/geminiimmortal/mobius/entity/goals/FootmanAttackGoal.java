package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class FootmanAttackGoal extends MeleeAttackGoal {
    private final FootmanEntity footman;

    public FootmanAttackGoal(FootmanEntity footman, double speedModifier, boolean pauseWhenMobIdle) {
        super(footman, speedModifier, pauseWhenMobIdle);
        this.footman = footman;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity p_190102_1_, double p_190102_2_) {
        this.footman.setAttacking(true);
        super.checkAndPerformAttack(p_190102_1_, p_190102_2_);
    }

    @Override
    public void stop() {
        super.stop();
    }
}

