package net.geminiimmortal.mobius.entity.goals;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;

public class AerialLightningBarrageGoal extends Goal {
    private final MobEntity boss;
    private final double teleportHeight;
    private final int lightningStrikes;
    private final int cooldownBetweenStrikes;
    private int strikeTimer = 0;
    private int executedStrikes = 0;
    private boolean isExecuting = false;
    private int cooldownTimer = 0;
    private final int cooldownDuration = 240;

    public AerialLightningBarrageGoal(MobEntity boss, double teleportHeight, int lightningStrikes, int cooldownBetweenStrikes) {
        this.boss = boss;
        this.teleportHeight = teleportHeight;
        this.lightningStrikes = lightningStrikes;
        this.cooldownBetweenStrikes = cooldownBetweenStrikes;
    }

    @Override
    public boolean canUse() {
        if (cooldownTimer > 0) {
            cooldownTimer--;  // Decrease cooldown
        }
        // Activate when health is 10% or below
        return boss.getHealth() <= boss.getMaxHealth() * 0.25 && !isExecuting && cooldownTimer <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        // Continue until all strikes are executed
        return executedStrikes < lightningStrikes;
    }

    @Override
    public void start() {
        // Teleport the boss high into the air
        isExecuting = true;
        boss.teleportTo(boss.getX(), boss.getY() + teleportHeight, boss.getZ());
        boss.setNoGravity(true);
    }

    @Override
    public void tick() {
        if (strikeTimer > 0) {
            strikeTimer--;
            return;
        }

        if (executedStrikes < lightningStrikes) {
            strikeTimer = cooldownBetweenStrikes;

            Iterable<PlayerEntity> playersInRange = boss.level.getEntitiesOfClass(PlayerEntity.class, boss.getBoundingBox().inflate(25.0D), player -> true);

            for (PlayerEntity player : playersInRange) {
                LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(boss.level);
                assert lightning != null;
                lightning.moveTo(player.getX() + Math.random(), player.getY(), player.getZ() + Math.random());
                boss.level.addFreshEntity(lightning);  // Summon lightning at the player's position
            }

            executedStrikes++;
        }
    }

    @Override
    public void stop() {
        boss.teleportTo(boss.getX(), boss.getY() - (teleportHeight - 4), boss.getZ());
        executedStrikes = 0;
        strikeTimer = 0;
        isExecuting = false;
        cooldownTimer = cooldownDuration;
        boss.setNoGravity(false);
    }
}

