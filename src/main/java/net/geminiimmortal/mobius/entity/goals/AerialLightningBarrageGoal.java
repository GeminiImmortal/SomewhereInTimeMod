package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

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

    private final Map<UUID, Deque<Vector3d>> playerPositionHistory = new HashMap<>();
    private final int positionLagTicks = 10; // How many ticks behind the lightning lags


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
        return boss.getHealth() <= boss.getMaxHealth() * 0.5 && /*boss.getHealth() >= boss.getMaxHealth() * 0.34 &&*/ !isExecuting && cooldownTimer <= 0;
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
        if (boss.getTarget() != null) { boss.level.playSound(null, boss.getTarget().blockPosition(), ModSounds.LIGHTNING_SPELL_FX.get(), SoundCategory.HOSTILE ,1.0F, 1.0F); }
        Iterable<PlayerEntity> playersInRange = boss.level.getEntitiesOfClass(PlayerEntity.class, boss.getBoundingBox().inflate(25.0D), player -> true);
        for (PlayerEntity player : playersInRange) {
            UUID playerId = player.getUUID();
            Deque<Vector3d> history = new ArrayDeque<>();
            Vector3d currentPos = player.position();

            // Pre-fill the history with the current position N times
            for (int i = 0; i < positionLagTicks; i++) {
                history.add(currentPos);
            }

            playerPositionHistory.put(playerId, history);
        }

    }

    @Override
    public void tick() {
        if (strikeTimer > 0) {
            strikeTimer--;
            return;
        }

        Iterable<PlayerEntity> playersInRange = boss.level.getEntitiesOfClass(PlayerEntity.class, boss.getBoundingBox().inflate(25.0D), player -> true);

        for (PlayerEntity player : playersInRange) {
            // Store position history for lag tracking
            UUID playerId = player.getUUID();
            playerPositionHistory.putIfAbsent(playerId, new ArrayDeque<>());
            Deque<Vector3d> history = playerPositionHistory.get(playerId);

            // Add current position
            history.addLast(player.position());

            // Maintain history size
            if (history.size() > positionLagTicks) {
                history.removeFirst();
            }
        }

        if (executedStrikes < lightningStrikes) {
            strikeTimer = cooldownBetweenStrikes;

            for (PlayerEntity player : playersInRange) {
                UUID playerId = player.getUUID();
                Deque<Vector3d> history = playerPositionHistory.get(playerId);

                if (history != null && history.size() >= positionLagTicks) {
                    // Get the oldest tracked position (lagged)
                    Vector3d laggedPos = history.peekLast();

                    ((ServerWorld) boss.level).sendParticles(
                            ParticleTypes.DRAGON_BREATH,
                            laggedPos.x, laggedPos.y + 1, laggedPos.z,
                            10, // count
                            0.5, 0.5, 0.5, // x, y, z spread
                            0.0 // speed
                    );

                    LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(boss.level);
                    if (lightning != null) {
                        lightning.moveTo(laggedPos.x + Math.random(), laggedPos.y, laggedPos.z + Math.random());
                        lightning.moveTo(laggedPos.x + Math.random(), laggedPos.y + 1, laggedPos.z + Math.random());

                        boss.level.addFreshEntity(lightning);
                    }
                }
            }

            executedStrikes++;
        }
    }


    @Override
    public void stop() {
        boss.teleportTo(boss.getX(), boss.getY() - (teleportHeight - 4), boss.getZ());
        executedStrikes = 0;
        playerPositionHistory.clear();
        strikeTimer = 0;
        isExecuting = false;
        boss.setNoGravity(false);
        cooldownTimer = cooldownDuration;
    }
}

