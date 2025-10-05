package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.GovernorCloneEntity;
import net.geminiimmortal.mobius.entity.goals.util.SonicBoomHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SonicBoomGoal extends Goal {
    private final GovernorCloneEntity caster;
    private int cooldown;
    private LivingEntity cachedTarget;

    public SonicBoomGoal(GovernorCloneEntity caster) {
        this.caster = caster;
        this.cooldown = 0;
        this.cachedTarget = caster.getTarget();
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        if (cachedTarget == null || !cachedTarget.isAlive()) return false;
        double distSq = caster.distanceToSqr(cachedTarget);
        return distSq < 300.0D && caster.getRandom().nextInt(1400) == 0;
    }

    @Override
    public void start() {
        this.cachedTarget = caster.getTarget();

        if (cachedTarget != null && cooldown == 0) {
            SonicBoomHelper.doSonicBoom(caster, cachedTarget, caster.level);
        }
    }

    @Override
    public void tick() {
        LivingEntity target = caster.getTarget();
        if (cachedTarget == null || !cachedTarget.isAlive()) {
            stop();
            return;
        }

        if (--cooldown >= 0) {
            stop();
        }
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void stop() {
        cooldown = 80;


    }

    public void tickCooldown() {
        if (cooldown > 0) cooldown--;
    }
}

