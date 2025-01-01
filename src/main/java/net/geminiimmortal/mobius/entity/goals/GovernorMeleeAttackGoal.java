package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class GovernorMeleeAttackGoal extends MeleeAttackGoal {
    private final GovernorEntity boss;

    public GovernorMeleeAttackGoal(GovernorEntity boss, double speedModifier, boolean pauseWhenMobIdle) {
        super(boss, speedModifier, pauseWhenMobIdle);
        this.boss = boss;
    }

    @Override
    public void tick() {
        super.tick();

    }
}

