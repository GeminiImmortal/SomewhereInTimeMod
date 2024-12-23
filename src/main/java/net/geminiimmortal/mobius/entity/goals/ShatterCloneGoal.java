package net.geminiimmortal.mobius.entity.goals;


import net.geminiimmortal.mobius.entity.custom.CloneEntity;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

import java.util.EnumSet;

public class ShatterCloneGoal extends Goal {

    private final CloneEntity clone;
    private final LivingEntity target;
    private int stuckCounter = 0;

    public ShatterCloneGoal(LivingEntity target, CloneEntity cloneEntity) {
        this.target = target;
        this.clone = cloneEntity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public void start() {
        if (target != null) {
            this.clone.getNavigation().moveTo(target, 0.35f);
        }
    }

    @Override
    public void tick() {
        if (target != null && target.isAlive()) {
            if (!this.clone.getNavigation().isInProgress()) {
                this.clone.getNavigation().moveTo(target, 0.35f);
            }
            if (this.clone.distanceTo(this.target) <= 2) {
                this.clone.kill();
                this.target.hurt(DamageSource.IN_FIRE, 7f);
                AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(this.clone.level, this.clone.getX(), this.clone.getY(), this.clone.getZ());
                this.clone.level.addFreshEntity(cloud);
                this.clone.level.playSound(null, this.target.getX(), this.target.getY(), this.target.getZ(), SoundEvents.GENERIC_EXPLODE, SoundCategory.HOSTILE, 1.0f, 1.0f);
            }
            this.clone.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        }
    }

    @Override
    public boolean canUse() {
        return this.target != null && this.target.isAlive();
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
        return this.target != null && this.target.isAlive() && this.clone.distanceTo(this.target) > 0.5;
    }
}
