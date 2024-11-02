package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.DiamondGolemEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.List;

public class TauntGoal extends Goal {
    private final DiamondGolemEntity golem;
    private int tauntCooldown;

    public TauntGoal(DiamondGolemEntity golem) {
        this.golem = golem;
        this.tauntCooldown = 0;
    }

    @Override
    public boolean canUse() {
        return this.tauntCooldown == 0 && golem.getHealth() < golem.getMaxHealth() * 0.75F; // Taunt if health < 75%
    }

    @Override
    public void start() {
        // Force nearby mobs to target the golem
        List<MobEntity> nearbyEnemies = golem.level.getEntitiesOfClass(MobEntity.class, golem.getBoundingBox().inflate(10.0D));
        for (MobEntity enemy : nearbyEnemies) {
            if (enemy.getTarget() != golem) {
                enemy.setTarget(golem);
            }
        }
        tauntCooldown = 200; // Cooldown before next taunt
    }

    @Override
    public void tick() {
        if (tauntCooldown > 0) {
            tauntCooldown--;
        }
    }
}

