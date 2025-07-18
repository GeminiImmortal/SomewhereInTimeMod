package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.AbstractImperialEntity;
import net.geminiimmortal.mobius.entity.custom.SorcererEntity;
import net.geminiimmortal.mobius.entity.custom.spell.ObliteratorEntity;
import net.geminiimmortal.mobius.network.BeamCirclePacket;
import net.geminiimmortal.mobius.network.BeamRenderPacket;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;

public class ArcaneBeamAttackGoal extends Goal {
    private final SorcererEntity sorcerer;
    private LivingEntity target;
    private int attackTick;
    private int cooldownTicker = 0;
    private int cooldown;
    private BlockPos beamTarget;
    private boolean hasFired;
    ObliteratorEntity obliterator;

    public ArcaneBeamAttackGoal(SorcererEntity sorcerer, ObliteratorEntity obliterator, int cooldown) {
        this.sorcerer = sorcerer;
        this.obliterator = obliterator;
        this.cooldown = cooldown;
    }

    @Override
    public boolean canUse() {
        this.target = sorcerer.getTarget();
        return target != null && target.isAlive() && cooldownTicker >= cooldown;
    }

    @Override
    public void start() {
        attackTick = 0;
        hasFired = false;
        beamTarget = target.blockPosition();
        sorcerer.getNavigation().stop();

        if (!sorcerer.level.isClientSide) {
            // Tell clients tracking the boss to render the arcane circle
            ModNetwork.NETWORK_CHANNEL.send(
                    PacketDistributor.TRACKING_ENTITY.with(() -> sorcerer),
                    new BeamCirclePacket(beamTarget)
            );
        }

        this.obliterator = new ObliteratorEntity(ModEntityTypes.OBLITERATOR.get(), sorcerer.level);
        obliterator.setPos(beamTarget.getX(), beamTarget.getY() + 16, beamTarget.getZ());
        sorcerer.level.addFreshEntity(obliterator);

        if (this.obliterator.level.isClientSide) {
            spawnGroundTelegraph((ClientWorld) this.obliterator.level, new Vector3d(beamTarget.getX(), beamTarget.getY() + 0.25, beamTarget.getZ()), 4D, 0.5, 100, ParticleTypes.FIREWORK);
            obliterator.startBeam();
        }

        sorcerer.level.playSound(null, beamTarget, ModSounds.ARCANE_NUKE_FX.get(), SoundCategory.HOSTILE, 50f, 1f);
    }


    @Override
    public void tick() {
        attackTick++;

        if (!this.sorcerer.isAlive()) {
            stop();
        }

        if (attackTick == 80 && !hasFired && target != null) {
            hasFired = true;

            if (!sorcerer.level.isClientSide) {
                ModNetwork.NETWORK_CHANNEL.send(
                        PacketDistributor.TRACKING_ENTITY.with(() -> sorcerer),
                        new BeamRenderPacket(
                                new Vector3d(beamTarget.getX(), beamTarget.getY() + 24, beamTarget.getZ()),
                                new Vector3d(beamTarget.getX(), beamTarget.getY(), beamTarget.getZ()),
                                ParticleTypes.SWEEP_ATTACK,
                                5f
                        )
                );

                ModNetwork.NETWORK_CHANNEL.send(
                        PacketDistributor.TRACKING_ENTITY.with(() -> sorcerer),
                        new BeamRenderPacket(
                                new Vector3d(beamTarget.getX(), beamTarget.getY() + 24, beamTarget.getZ()),
                                new Vector3d(beamTarget.getX(), beamTarget.getY(), beamTarget.getZ()),
                                ParticleTypes.FLAME,
                                25f
                        )
                );

                ModNetwork.NETWORK_CHANNEL.send(
                        PacketDistributor.TRACKING_ENTITY.with(() -> sorcerer),
                        new BeamRenderPacket(
                                new Vector3d(beamTarget.getX(), beamTarget.getY() + 24, beamTarget.getZ()),
                                new Vector3d(beamTarget.getX(), beamTarget.getY(), beamTarget.getZ()),
                                ParticleTypes.DRAGON_BREATH,
                                25f
                        )
                );
            }

            sorcerer.level.playSound(null, beamTarget, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.HOSTILE, 3f, 0.7f);

            AxisAlignedBB aoe = new AxisAlignedBB(beamTarget).inflate(4);
            for (LivingEntity entity : sorcerer.level.getEntitiesOfClass(LivingEntity.class, aoe)) {
                if (entity != sorcerer && !entity.isInvulnerable() && !(entity instanceof AbstractImperialEntity)) {
                    entity.hurt(DamageSource.indirectMagic(sorcerer, sorcerer), 20.0f);
                }
            }
        }
    }


    @Override
    public boolean canContinueToUse() {
        return attackTick < 100;
    }

    @Override
    public void stop() {
        this.obliterator.remove();
        cooldownTicker = 0;
    }

    public static void spawnGroundTelegraph(ClientWorld level, Vector3d center, double initialRadius, double speed, int count, ParticleType<?> particle) {
        double angleStep = (2 * Math.PI) / count;

        for (int i = 0; i < count; i++) {
            double angle = i * angleStep;

            double dx = Math.cos(angle);
            double dz = Math.sin(angle);

            double px = center.x + dx * initialRadius;
            double py = center.y + 0.05; // slightly above ground
            double pz = center.z + dz * initialRadius;

            // Velocity moves outwards
            level.addParticle((IParticleData) particle, px, py, pz, dx * speed, 0, dz * speed);
        }
    }

    public void tickCooldown() {
        cooldownTicker++;
    }

}

