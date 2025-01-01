package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.ClubGolemEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class CustomMeleeAttackGoal extends MeleeAttackGoal {
    private final ClubGolemEntity golem;

    public CustomMeleeAttackGoal(ClubGolemEntity golem, double speedModifier, boolean pauseWhenMobIdle) {
        super(golem, speedModifier, pauseWhenMobIdle);
        this.golem = golem;
    }

    @Override
    public void tick() {
        super.tick();
        golem.playAttackAnimation();

    }
}

