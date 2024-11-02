package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.DiamondGolemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class DefensiveStanceGoal extends Goal {
    private final DiamondGolemEntity golem;

    public DefensiveStanceGoal(DiamondGolemEntity golem) {
        this.golem = golem;
    }

    @Override
    public boolean canUse() {
        return golem.getHealth() < golem.getMaxHealth() * 0.50F; // Trigger when health is below 50%
    }

    @Override
    public void start() {
        golem.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 600, 1)); // Increase resistance
        golem.addEffect(new EffectInstance(Effects.REGENERATION, 300, 1));
    }
}

