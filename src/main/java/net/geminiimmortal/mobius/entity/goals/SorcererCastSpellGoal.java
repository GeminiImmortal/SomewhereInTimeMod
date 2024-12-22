package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.*;
import net.geminiimmortal.mobius.network.ParticlePacket;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.EnumSet;

public class SorcererCastSpellGoal extends Goal {
    private final SorcererEntity sorcerer;
    private int chargeTime; // Time spent charging the spell
    private int cooldown;   // Cooldown before the sorcerer can cast again
    private final int maxChargeTime = 20; // Ticks (3 seconds at 20 TPS)
    private final int cooldownTime = 60; // Ticks (5 seconds cooldown)
    private final double castRange;

    public SorcererCastSpellGoal(SorcererEntity sorcerer, double castRange) {
        this.sorcerer = sorcerer;
        this.castRange = castRange;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK)); // Prevent movement while casting
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            return false; // Skip if on cooldown
        }
        if (this.sorcerer.getTarget() != null) {
            if (this.sorcerer.distanceTo(this.sorcerer.getTarget()) > this.castRange) {
                return false;
            }
        }

        // Check if there's a valid target within range
        LivingEntity target = sorcerer.getTarget();
        return target != null && target.isAlive() && sorcerer.distanceToSqr(target) <= castRange * castRange;
    }

    @Override
    public boolean canContinueToUse() {
        return chargeTime < maxChargeTime; // Continue until fully charged
    }

    @Override
    public void start() {
        chargeTime = 0;
        sorcerer.setCasting(true); // Optional: Add a flag or animation
    }

    @Override
    public void tick() {
        super.tick();
        chargeTime++;

        // Rotate to face the target during charging
        LivingEntity target = sorcerer.getTarget();
        if (cooldown > 0) {
            cooldown--;
        }
        if (target != null) {
            sorcerer.getLookControl().setLookAt(target, 30.0F, 30.0F);
        }

        if (chargeTime == maxChargeTime) {
            executeSpell();
        }
    }

    private void executeSpell() {
        LivingEntity target = sorcerer.getTarget();
        if (target != null && target.isAlive()) {
            // Summon the spell entity
            SpellEntity spell = new SpellEntity(ModEntityTypes.SPELL.get(), sorcerer.level);
            sorcerer.level.addFreshEntity(spell);
            spell.setPos(target.getX(), target.getY() + 6, target.getZ());
            sorcerer.level.playSound(null, sorcerer.blockPosition(), SoundEvents.ILLUSIONER_CAST_SPELL, SoundCategory.HOSTILE, 1.0F, 1.0F);
            sorcerer.level.playSound(null, sorcerer.blockPosition(), ModSounds.KNIFE_RAIN.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);

            double startX = target.getX();
            double startY = target.getY() + 6; // Adjust height to eye level
            double startZ = target.getZ();

            double endX = target.getX();
            double endY = target.getY();
            double endZ = target.getZ();

            // Calculate the number of particles and interpolate their position
            int particleCount = 300; // Example count
            for (int i = 0; i < particleCount; i++) {
                double t = (double) i / (particleCount - 1);
                double particleX = startX + (endX - startX) * t;
                double particleY = startY + (endY - startY) * t;
                double particleZ = startZ + (endZ - startZ) * t;
                ParticlePacket packet = new ParticlePacket(particleX, particleY, particleZ, "knife_rain"); // or your chosen particle type
                MobiusMod.NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), packet);


                // Add visual or sound effects for casting

            }
        }
    }
}

