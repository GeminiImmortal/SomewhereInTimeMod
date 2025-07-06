package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.GovernorCloneEntity;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.geminiimmortal.mobius.entity.goals.util.TeleportUtil;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class GovernorSummonCloneGoal extends Goal {


    private static final String[] TAUNTS = {
            "Over here, dumbo!", "Don't blink! You might miss me! HA!",
            "Hit me already, fool!", "Stop trying to hit me and hit me!"
    };

    private final GovernorEntity governor;
    private final Random random = new Random();

    public GovernorSummonCloneGoal(GovernorEntity governor) {
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.governor = governor;
    }

    @Override
    public boolean canUse() {
        return governor.getTarget() != null && governor.getGrinning() && governor.getGCD() == 38;
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

        for (int i = 0; i < 16; i++) {
            BlockPos spawnPos = governor.getTarget().blockPosition().offset(
                    governor.getRandom().nextInt(15) - 3,
                    0,
                    governor.getRandom().nextInt(15) - 3
            );

            GovernorCloneEntity clone = ModEntityTypes.GOVERNOR_CLONE.get().create(world);
            if (clone != null) {
                clone.setFromGovernor(governor);
                clone.moveTo(spawnPos, 0.0F, 0.0F);
                clone.setOriginalGovernor(governor, governor.getUUID());
                world.addFreshEntity(clone);
            }
        }

        // Visual/audio feedback
//        governor.addEffect(new EffectInstance(Effects.INVISIBILITY, 100, 1));
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

        // Taunt nearby players
        if (!governor.level.isClientSide) {
            List<ServerPlayerEntity> players = world.getEntitiesOfClass(ServerPlayerEntity.class, governor.getBoundingBox().inflate(50));
            for (ServerPlayerEntity player : players) {
                player.sendMessage(
                        new StringTextComponent(TAUNTS[random.nextInt(TAUNTS.length)]).withStyle(TextFormatting.DARK_PURPLE),
                        governor.getUUID()
                );
            }
        }
    }
}


