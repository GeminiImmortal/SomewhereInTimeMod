package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.BeamEntity;
import net.geminiimmortal.mobius.entity.custom.SorcererEntity;
import net.geminiimmortal.mobius.entity.custom.SorcererObliteratorEntity;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class SummonObliteratorAttackGoal extends Goal {
    private final SorcererEntity sorcerer;
    private SorcererObliteratorEntity obliterator;
    private final double summonHeight = 15.0;  // Height above the player to summon the obliterator
    private final double beamSpeed = 1.0;  // Speed of particle beam
    private final int summonCooldown = 100;  // Cooldown before summoning again
    private final int attackDuration = 100;  // Duration of the attack in ticks (20 ticks = 1 second)
    private int cooldownTimer = 0;
    private int attackTimer = 0;

    private final int beamSpawnInterval = 10;  // Every 10 ticks (0.5 seconds)
    private int beamSpawnTimer = 0;


    public SummonObliteratorAttackGoal(SorcererEntity sorcerer) {
        this.sorcerer = sorcerer;
        this.obliterator = obliterator;
    }

    @Override
    public boolean canUse() {
        return cooldownTimer <= 0 && sorcerer.getTarget() != null && sorcerer.getTarget().isAlive();
    }


    @Override
    public void start() {
        sorcerer.setCasting(true);

        LivingEntity target = sorcerer.getTarget();
        if (target != null) {
            this.obliterator = new SorcererObliteratorEntity(ModEntityTypes.OBLITERATOR.get(), sorcerer.level);

            double yOffset = target.getY() + summonHeight;
            obliterator.setPos(target.getX(), yOffset, target.getZ());
            sorcerer.level.addFreshEntity(obliterator);
            sorcerer.level.playSound(null, target.blockPosition(), ModSounds.LIGHTNING_SPELL_FX.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
        }

        attackTimer = attackDuration;
        beamSpawnTimer = 0; // start firing right away
    }



    @Override
    public void tick() {
        LivingEntity target = sorcerer.getTarget();

        if (attackTimer > 0) {
            if (target != null && obliterator != null) {
                beamSpawnTimer--;
                if (beamSpawnTimer <= 0) {
                    spawnPersistentParticleBeam(obliterator, target);
                    beamSpawnTimer = beamSpawnInterval;
                }
                spawnParticleTrail(obliterator, target);
            }
            attackTimer--;
        } else {
            if (obliterator != null && obliterator.isAlive()) {
                obliterator.remove();
            }

            // Now that attack is over, start the cooldown
            cooldownTimer = summonCooldown;
        }

        if (attackTimer <= 0 && cooldownTimer > 0) {
            cooldownTimer--;
        }
    }



    private void spawnPersistentParticleBeam(SorcererObliteratorEntity obliterator, LivingEntity target) {
        double dx = target.getX() - obliterator.getX();
        double dy = target.getY() - obliterator.getY();
        double dz = target.getZ() - obliterator.getZ();
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        dx /= dist;
        dy /= dist;
        dz /= dist;

        BeamEntity beam = new BeamEntity(sorcerer.level, obliterator.getX(), obliterator.getY(), obliterator.getZ());
        beam.setDirection(dx, dy, dz);
        beam.setTarget((PlayerEntity) target);
        beam.setParticleEffect(ParticleTypes.END_ROD); // more magical beam look
        beam.setDuration(beamSpawnInterval + 5); // persist a bit past next interval
        sorcerer.level.addFreshEntity(beam);
        sorcerer.level.playSound(null, target, ModSounds.ARCANE_BOLT_FX.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
    }

    private void spawnParticleTrail(Entity from, Entity to) {
        World world = sorcerer.level;
        int points = 20;
        double dx = (to.getX() - from.getX()) / points;
        double dy = (to.getY() + 1 - from.getY()) / points;
        double dz = (to.getZ() - from.getZ()) / points;

        for (int i = 0; i < points; i++) {
            double px = from.getX() + dx * i;
            double py = from.getY() + dy * i;
            double pz = from.getZ() + dz * i;
            world.addParticle(ParticleTypes.ENCHANT, px, py, pz, 0, 0, 0);
        }
    }



    @Override
    public boolean canContinueToUse() {
        return attackTimer > 0 && sorcerer.getTarget() != null;
    }


    @Override
    public void stop() {
        if (obliterator != null && obliterator.isAlive()) {
            obliterator.remove();
        }

        // Let goal reset naturally
    }

}
