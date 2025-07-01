package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.goals.util.SonicBoomHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * Goal that makes the boss perform a sonic boom attack with dragon-breath particles,
 * damaging and stunning the target.
 */
public class SonicBoomGoal extends Goal {
    private final MobEntity caster;
    private int cooldown;
    private LivingEntity cachedTarget;

    public SonicBoomGoal(MobEntity caster) {
        this.caster = caster;
        this.cooldown = 0;
        // This goal affects movement and looking.
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
        // Only use within 10 blocks (100 sq) and randomly (roughly once per 200 ticks)
        return distSq < 100.0D && caster.getRandom().nextInt(200) == 0;
    }

    @Override
    public void start() {
        this.cachedTarget = caster.getTarget();
        System.out.println("Target is: " + cachedTarget);
        if (cachedTarget != null && cooldown == 0) {
            SonicBoomHelper.doSonicBoom(caster, cachedTarget, caster.level);
            // 200-tick cooldown (~10 seconds)

        }
    }

    @Override
    public void tick() {
        LivingEntity target = caster.getTarget();
        System.out.println("Sonic goal ticking...");
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
        cooldown = 200;


    }

    public void tickCooldown() {
        if (cooldown > 0) cooldown--;
    }
}

