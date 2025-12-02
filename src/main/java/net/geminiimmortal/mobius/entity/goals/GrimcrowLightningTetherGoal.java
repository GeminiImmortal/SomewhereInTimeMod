package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.effects.ModEffects;
import net.geminiimmortal.mobius.entity.custom.GrimcrowCaptainBossEntity;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.geminiimmortal.mobius.network.ParticlePacket;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;

public class GrimcrowLightningTetherGoal extends Goal {
    private final GrimcrowCaptainBossEntity boss;
    private ServerPlayerEntity target;
    private int duration;
    private final int maxDuration = 200; // 10 seconds

    public GrimcrowLightningTetherGoal(GrimcrowCaptainBossEntity boss) {
        this.boss = boss;
    }

    @Override
    public boolean canUse() {
        return boss.getHealth() < boss.getMaxHealth() * 0.5F && boss.getTarget() instanceof ServerPlayerEntity && boss.lightningAttackCooldown == 0;
    }

    @Override
    public void start() {
        target = (ServerPlayerEntity) boss.getTarget();
        boss.startLightningTether();
        duration = 0;
        if (!boss.level.isClientSide()) {
            boss.level.playSound(null, target.blockPosition(), ModSounds.TESLA_SPARK.get(), SoundCategory.HOSTILE, 1.0f, 1.0f);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return boss.tetherActive && duration < maxDuration && target.isAlive();
    }

    @Override
    public void tick() {
        duration++;

        // Levitating & minor damage
        target.addEffect(new EffectInstance(Effects.LEVITATION, 10, duration / 40));
        target.hurt(DamageSource.MAGIC, 0.5F);

        // Visual tether effect
        if (!boss.level.isClientSide()) {
            createTetherParticles();
        }

        // Lightning strikes boss periodically
        if (duration % 40 == 0) {
            LightningBoltEntity bolt = EntityType.LIGHTNING_BOLT.create(boss.level);
            bolt.moveTo(boss.position());
            boss.level.addFreshEntity(bolt);
            boss.addEffect(new EffectInstance(ModEffects.CHARGED_EFFECT.get(), 2400));
        }

        // End after 10 seconds
        if (duration >= maxDuration) {
            boss.endLightningTether(false);
        }
    }

    private void createTetherParticles() {
        Vector3d bossPos = boss.position().add(0, boss.getEyeHeight(), 0);
        Vector3d targetPos = target.position().add(0, target.getEyeHeight(), 0);

        // Calculate direction and distance
        Vector3d direction = targetPos.subtract(bossPos);
        double distance = direction.length();
        Vector3d step = direction.scale(1.0 / distance); // Normalize

        // Create particles along the line between boss and player
        int particleCount = (int) (distance * 2); // More particles for longer distances

        for (int i = 0; i <= particleCount; i++) {
            double progress = (double) i / particleCount;
            Vector3d particlePos = bossPos.add(step.scale(distance * progress));

            // Add some randomness to make it look more energetic
            double offsetX = (boss.level.random.nextDouble() - 0.5) * 0.3;
            double offsetY = (boss.level.random.nextDouble() - 0.5) * 0.3;
            double offsetZ = (boss.level.random.nextDouble() - 0.5) * 0.3;

            // Send particle packet for each position
            ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(),
                    new ParticlePacket(
                            particlePos.x + offsetX,
                            particlePos.y + offsetY,
                            particlePos.z + offsetZ,
                            "tesla_spark" // or "flame", "soul", etc.
                    ));
        }

        // Additional effect: occasional larger particles at connection points
        if (duration % 5 == 0) {
            // At boss connection point
            ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(),
                    new ParticlePacket(bossPos.x, bossPos.y, bossPos.z, "flame"));

            // At player connection point
            ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(),
                    new ParticlePacket(targetPos.x, targetPos.y, targetPos.z, "flame"));
        }
    }

    @Override
    public void stop() {
        boss.endLightningTether(false);
        super.stop();
    }
}
