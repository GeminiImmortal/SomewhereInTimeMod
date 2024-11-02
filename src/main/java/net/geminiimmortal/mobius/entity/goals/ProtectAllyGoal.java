package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.ClubGolemEntity;
import net.geminiimmortal.mobius.entity.custom.DiamondGolemEntity;
import net.geminiimmortal.mobius.entity.custom.HeartGolemEntity;
import net.geminiimmortal.mobius.entity.custom.SpadeGolemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.List;

public class ProtectAllyGoal extends Goal {
    private final DiamondGolemEntity golem;

    public ProtectAllyGoal(DiamondGolemEntity golem) {
        this.golem = golem;
    }

    @Override
    public boolean canUse() {
        // Check if an ally nearby is being attacked
        List<LivingEntity> nearbyAllies = golem.level.getEntitiesOfClass(LivingEntity.class, golem.getBoundingBox().inflate(10.0D), ally -> {
            return ally instanceof ClubGolemEntity || ally instanceof HeartGolemEntity|| ally instanceof SpadeGolemEntity;
        });

        for (LivingEntity ally : nearbyAllies) {
            if (ally.getLastHurtByMob() != null) {
                golem.setTarget(ally.getLastHurtByMob()); // Target the attacker
                return true;
            }
        }
        return false;
    }

    private LivingEntity findClosestAllyUnderAttack() {
        List<LivingEntity> allies = this.golem.level.getEntitiesOfClass(LivingEntity.class,
                this.golem.getBoundingBox().inflate(10.0D),
                entity -> entity != this.golem && entity.isAlive());

        for (LivingEntity ally : allies) {
            if (isBeingAttacked(ally)) { // Replace with your actual method to check if the ally is under attack
                return ally;
            }
        }
        return null;
    }

    private boolean isBeingAttacked(LivingEntity ally) {
        // Implement logic to determine if the ally is being attacked
        // For example, check for nearby enemies or attack indicators
        return ally.hurtTime > 0; // Simple example: return true if the ally is hurt
    }

    @Override
    public void start() {
        LivingEntity targetAlly = findClosestAllyUnderAttack();

        if (targetAlly != null) {
            // Move towards the ally being attacked
            this.golem.getNavigation().moveTo(targetAlly, 1.3); // 1.0 can be the speed
        }
        // Move towards the ally and block attacks
    }
}

