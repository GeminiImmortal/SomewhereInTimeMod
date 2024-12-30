package net.geminiimmortal.mobius.entity.goals;


import net.geminiimmortal.mobius.effects.ModEffects;
import net.geminiimmortal.mobius.entity.custom.CloneEntity;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

import java.util.EnumSet;
import java.util.Objects;

public class ShatterCloneGoal extends Goal {

    private final CloneEntity clone;
    private int stuckCounter = 0;

    public ShatterCloneGoal(CloneEntity cloneEntity) {
        this.clone = cloneEntity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public void start() {
            this.clone.getNavigation().moveTo(Objects.requireNonNull(clone.getTarget()), 0.6f);
        
    }

    @Override
    public void tick() {
        if (clone.getTarget() != null && clone.getTarget().isAlive()) {
            if (!this.clone.getNavigation().isInProgress()) {
                this.clone.getNavigation().moveTo(clone.getTarget(), 0.35f);
            }
            if (this.clone.distanceTo(clone.getTarget()) <= 2) {
                this.clone.getTarget().hurt(DamageSource.IN_FIRE, 7f);
                AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(this.clone.level, this.clone.getX(), this.clone.getY(), this.clone.getZ());
                this.clone.level.addFreshEntity(cloud);
                this.clone.level.playSound(null, this.clone.getTarget().getX(), this.clone.getTarget().getY(), this.clone.getTarget().getZ(), SoundEvents.GENERIC_EXPLODE, SoundCategory.HOSTILE, 1.0f, 1.0f);
                cloud.addEffect(new EffectInstance(ModEffects.LORD_DECREE_EFFECT.get(), 40));
                this.clone.level.playSound(null, this.clone.getX(), this.clone.getY(), this.clone.getZ(), SoundEvents.GLASS_BREAK, SoundCategory.HOSTILE, 1.0f, 1.0f);
                this.clone.kill();
            }
            this.clone.getLookControl().setLookAt(this.clone.getTarget(), 30.0F, 30.0F);
        }
    }

    @Override
    public boolean canUse() {
        return this.clone.getTarget() != null && this.clone.getTarget().isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.clone.getNavigation().isStuck()) {
            stuckCounter++;
            if (stuckCounter > 40) {
                return false;
            }
        } else {
            stuckCounter = 0;
        }
        return this.clone.getTarget() != null && this.clone.getTarget().isAlive() && this.clone.distanceTo(this.clone.getTarget()) > 0.5;
    }
}
