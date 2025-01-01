package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.DiamondGolemEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class CustomDiamondGolemMeleeAttackGoal extends MeleeAttackGoal {
    private final DiamondGolemEntity golem;

    public CustomDiamondGolemMeleeAttackGoal(DiamondGolemEntity golem, double speedModifier, boolean pauseWhenMobIdle) {
        super(golem, speedModifier, pauseWhenMobIdle);
        this.golem = golem;
    }

    @Override
    public void tick() {
        super.tick();
        golem.playAttackAnimation();

    }
}

