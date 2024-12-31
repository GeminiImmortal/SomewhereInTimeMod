package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.*;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;
import java.util.Objects;

public class GovernorCloneSummonGoal extends Goal {
    private final GovernorEntity boss;
    private int cooldown = 200;

    public GovernorCloneSummonGoal(GovernorEntity boss) {
        this.boss = boss;
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
        }

        if (boss.getTarget() != null && cooldown <= 0 && boss.canSummonMoreClones()) {
            cooldown = 200;
            return true;
        }

        return false;
    }


    @Override
    public boolean canContinueToUse() {
        return boss.getTarget() != null && cooldown <= 0 && boss.canSummonMoreClones();
    }

    @Override
    public void start() {
        boss.setCasting(true);
        summonClones(Objects.requireNonNull(boss.getTarget()));
    }

    @Override
    public void tick() {
        super.tick();

        List<LivingEntity> nearbyClones = boss.level.getEntitiesOfClass(CloneEntity.class, boss.getBoundingBox().inflate(20.0D), clone -> clone instanceof CloneEntity);

        for (LivingEntity clone : nearbyClones) {
            if (clone != null) {
                boss.addEffect(new EffectInstance(Effects.REGENERATION));
                boss.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE));
                clone.setLastHurtByPlayer((PlayerEntity) boss.getTarget());
                boss.setCloned(true);
            } else {
                boss.setCloned(false);
                boss.removeEffect(Effects.REGENERATION);
                boss.removeEffect(Effects.DAMAGE_RESISTANCE);
                List<Entity> nearbyCircles = boss.level.getEntitiesOfClass(GovernorKnivesOutEntity.class, boss.getBoundingBox().inflate(20.0D), circle -> circle instanceof GovernorKnivesOutEntity);

                for (Entity circle : nearbyCircles) {
                    circle.kill();
                }
                this.stop();
            }
        }
    }

    private void summonClones(LivingEntity target) {
        // Positioning the clones in a square around the player
        Vector3d targetPos = target.position();
        double distance = 5.0; // Distance from the player

        for (int i = 0; i < 4; i++) {
            double xOffset = (i % 2 == 0) ? distance : -distance;
            double zOffset = (i < 2) ? distance : -distance;

            CloneEntity clone = new CloneEntity(ModEntityTypes.CLONE.get(), boss.level);
            clone.setOwner(boss);
            boss.addClone(clone);
            clone.setPos(targetPos.x + xOffset, targetPos.y, targetPos.z + zOffset);
            clone.lookAt(EntityAnchorArgument.Type.EYES, targetPos);
            boss.level.addFreshEntity(clone);
            boss.level.playSound(null, target, SoundEvents.ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0f, 1.0f);
        }
    }

    private void summonCircle() {
        LivingEntity target = this.boss.getTarget();
        if (target != null && target.isAlive()) {
            // Summon the spell entity
            ShatterCloneEntity spell = new ShatterCloneEntity(ModEntityTypes.SHATTER_CLONE.get(), this.boss.level);
            spell.setPos(this.boss.getX(), this.boss.getY(), this.boss.getZ());
            this.boss.level.addFreshEntity(spell);

        }
    }
}
