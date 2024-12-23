package net.geminiimmortal.mobius.entity.goals;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.EnumSet;

public class BubbleAttackGoal extends Goal {
    private final MobEntity mob;
    private final double attackRange;
    private final int preparationTime;
    private final int effectDuration;
    private final EffectInstance levitationEffect;
    private int preparationTicks;

    public BubbleAttackGoal(MobEntity mob, double attackRange, int preparationTime, int effectDuration) {
        this.mob = mob;
        this.attackRange = attackRange;
        this.preparationTime = preparationTime;
        this.effectDuration = effectDuration;
        this.levitationEffect = new EffectInstance(Effects.LEVITATION, effectDuration, 3);
        //this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // Perform this attack if a player is nearby
        return this.mob.getTarget() != null && this.mob.getTarget().distanceTo(this.mob) <= 8 && this.mob.getHealth() > this.mob.getMaxHealth() * 0.34;
    }

    @Override
    public void start() {
        preparationTicks = 0;
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            spawnYellowCloud(target);
        }
    }

    @Override
    public void stop() {
        preparationTicks = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return;
        }
        if (this.mob.getTarget().distanceTo(this.mob) > 8) return;

        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
    }

    private void spawnYellowCloud(LivingEntity target) {
        AreaEffectCloudEntity areaEffectCloud = new AreaEffectCloudEntity(this.mob.level, target.getX(), target.getY(), target.getZ());
        areaEffectCloud.setRadius((float) attackRange);
        areaEffectCloud.setDuration(preparationTime);
        areaEffectCloud.setFixedColor(0xFFFFFF);
        areaEffectCloud.addEffect(levitationEffect);
        this.mob.level.addFreshEntity(areaEffectCloud);
    }

    private void applyBubbleAttack(LivingEntity target) {
            target.addEffect(levitationEffect);
    }
}

