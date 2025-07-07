package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundEvents;

public class GovernorStartFightPendingGoal extends Goal {
    private boolean fightStarted;
    private GovernorEntity governor;

    public GovernorStartFightPendingGoal(boolean fightStarted, GovernorEntity governor){
        this.fightStarted = fightStarted;
        this.governor = governor;
    }

    @Override
    public boolean canUse() {
        return fightStarted;
    }

    @Override
    public void start() {
        governor.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0f, 1.0f);
        governor.setNoAi(false);
        governor.setGCD(70);
    }
}
