package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.SorcererObliteratorEntity;
import net.geminiimmortal.mobius.network.BeamCirclePacket;
import net.geminiimmortal.mobius.network.BeamEndPacket;
import net.geminiimmortal.mobius.network.BeamRenderPacket;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;

public class ArcaneBeamAttackGoal extends Goal {
    private final MobEntity boss;
    private LivingEntity target;
    private int attackTick;
    private BlockPos beamTarget;
    private boolean hasFired;
    SorcererObliteratorEntity obliterator;

    public ArcaneBeamAttackGoal(MobEntity boss, SorcererObliteratorEntity obliterator) {
        this.boss = boss;
        this.obliterator = obliterator;
    }

    @Override
    public boolean canUse() {
        this.target = boss.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public void start() {
        attackTick = 0;
        hasFired = false;
        beamTarget = target.blockPosition();
        boss.getNavigation().stop();

        // Tell client to render the arcane circle
        /*ModNetwork.NETWORK_CHANNEL.send(
                PacketDistributor.TRACKING_CHUNK.with(() -> boss.level.getChunkAt(target.blockPosition())),
                new BeamCirclePacket(beamTarget.above(24))
        );*/
        this.obliterator = new SorcererObliteratorEntity(ModEntityTypes.OBLITERATOR.get(), boss.level);
        obliterator.setPos(beamTarget.getX(), beamTarget.getY() + 16, beamTarget.getZ());
        boss.level.addFreshEntity(obliterator);

        boss.level.playSound(null, beamTarget, ModSounds.OBLITERATOR.get(), SoundCategory.HOSTILE, 2f, 0.5f);
    }

    @Override
    public void tick() {
        attackTick++;

        if (!this.boss.isAlive()) {
            stop();
        }

        if (attackTick == 80 && !hasFired && target != null) {
            hasFired = true;

            ModNetwork.NETWORK_CHANNEL.send(
                    PacketDistributor.TRACKING_CHUNK.with(() -> boss.level.getChunkAt(target.blockPosition())),
                    new BeamRenderPacket(new Vector3d(beamTarget.getX(), beamTarget.getY() + 24, beamTarget.getZ()), new Vector3d(beamTarget.getX(), beamTarget.getY(), beamTarget.getZ()), ParticleTypes.SOUL_FIRE_FLAME, 25f) // Adjust density and particle type
            );
            boss.level.playSound(null, beamTarget, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.HOSTILE, 3f, 0.7f);

            AxisAlignedBB aoe = new AxisAlignedBB(beamTarget).inflate(4);
            for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, aoe)) {
                if (entity != boss && !entity.isInvulnerable()) {
                    entity.hurt(DamageSource.indirectMagic(boss, boss), 40.0f);
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
        // Optional: tell client to fade beam or stop rendering
        /*ModNetwork.NETWORK_CHANNEL.send(
                PacketDistributor.TRACKING_CHUNK.with(() -> boss.level.getChunkAt(target.blockPosition())),
                new BeamEndPacket(target.blockPosition())
        );*/
        obliterator.remove();

    }
}

