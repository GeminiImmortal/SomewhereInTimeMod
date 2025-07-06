package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.effects.ModEffects;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.GovernorCloneEntity;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.geminiimmortal.mobius.entity.goals.util.TeleportUtil;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.EnumSet;

public class GovernorSummonCloneGoal extends Goal {


    private final GovernorEntity governor;

    public GovernorSummonCloneGoal(GovernorEntity governor) {
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.governor = governor;
    }

    @Override
    public boolean canUse() {
        return governor.getTarget() != null && governor.getGrinning() && governor.getGCD() == 10;
    }

    @Override
    public void start() {
        performSummon();
    }


    @Override
    public boolean canContinueToUse() {
        return false; // one-shot goal — reset each time it's triggered
    }

    private void performSummon() {
        ServerWorld world = (ServerWorld) governor.level;
        LivingEntity target = governor.getTarget();

        if (target == null) return;

        // Visual/audio feedback
        governor.addEffect(new EffectInstance(Effects.INVISIBILITY, 160, 1));
        target.addEffect(new EffectInstance(ModEffects.EXPOSED_EFFECT.get(), 160, 1));
        governor.playSound(ModSounds.GOVERNOR_POOF.get(), 12.0F, 1.0F);
        governor.playSound(ModSounds.GOVERNOR_ILLUSION.get(), 12.0f, 0.9f);

        world.sendParticles(
                ParticleTypes.CLOUD,
                governor.getX(), governor.getY(), governor.getZ(),
                20, 0.1, 0.1, 0.1, 0.1
        );

        Vector3d safePos = TeleportUtil.findSafeTeleportPosition(governor.level, governor, 16, 20);
        if (safePos != null) {
            governor.moveTo(safePos);
        }

        for (int i = 0; i < 24; i++) {
            int delayTicks = i * 5; // 5 ticks between each summon

            int index = i; // for lambda capture
            world.getServer().tell(new TickDelayedTask(world.getServer().getTickCount() + delayTicks, () -> {
                BlockPos spawnPos = target.blockPosition().offset(
                        governor.getRandom().nextInt(8) - 3,
                        0,
                        governor.getRandom().nextInt(8) - 3
                );

                GovernorCloneEntity clone = ModEntityTypes.GOVERNOR_CLONE.get().create(world);
                if (clone != null) {
                    clone.setFromGovernor(governor);
                    clone.moveTo(spawnPos, 0.0F, 0.0F);
                    clone.setOriginalGovernor(governor, governor.getUUID());
                    world.addFreshEntity(clone);
                }
            }));
        }






    }
}


