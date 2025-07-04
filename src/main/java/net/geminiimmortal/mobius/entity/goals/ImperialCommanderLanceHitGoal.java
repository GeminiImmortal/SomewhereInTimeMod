package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.GiantEntity;
import net.geminiimmortal.mobius.entity.custom.ImperialCommanderEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class ImperialCommanderLanceHitGoal extends MeleeAttackGoal {
    private final ImperialCommanderEntity commanderEntity;

    public ImperialCommanderLanceHitGoal(ImperialCommanderEntity commanderEntity, double speedModifier, boolean pauseWhenMobIdle) {
        super(commanderEntity, speedModifier, pauseWhenMobIdle);
        this.commanderEntity = commanderEntity;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity p_190102_1_, double p_190102_2_) {
        double d0 = this.getAttackReachSqr(p_190102_1_);
        if (p_190102_2_ <= d0 && this.getTicksUntilNextAttack() <= 0) {
            this.commanderEntity.setAttacking(true);
            this.resetAttackCooldown();
        }

    }

    @Override
    public void stop() {
        super.stop();
    }
}

