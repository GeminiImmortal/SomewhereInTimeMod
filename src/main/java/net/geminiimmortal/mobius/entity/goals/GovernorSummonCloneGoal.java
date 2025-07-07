package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.GovernorCloneEntity;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.geminiimmortal.mobius.entity.goals.util.TeleportUtil;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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
        return governor.getGCD() == 99;
    }

    @Override
    public void start() {
        performSummon();
        this.governor.setCorrectHit(false);
        this.governor.setGrinning(true);
    }


    @Override
    public boolean canContinueToUse() {
        return false; // one-shot goal — reset each time it's triggered
    }

    private void performSummon() {
        ServerWorld world = (ServerWorld) governor.level;
        LivingEntity target = governor.level.getNearestPlayer(governor, 50);

        if (target == null) return;

        governor.preCloneSummonEvent();
        for (int i = 0; i < 11; i++) {
            governor.setCorrectHit(false);

            GovernorCloneEntity clone = ModEntityTypes.GOVERNOR_CLONE.get().create(world);

            BlockPos spawnPos = target.blockPosition().offset(
                    governor.getRandom().nextInt(18) - 3,
                    0,
                    governor.getRandom().nextInt(18) - 3
            );





            if (clone != null) {
                clone.setFromGovernor(governor);
                clone.moveTo(spawnPos, 0.0f, 0.0f);
                    clone.setOriginalGovernor(governor, governor.getUUID());
                    world.addFreshEntity(clone);
                }
            if (clone != null) {
                Vector3d safeSpawnPos = TeleportUtil.findSafeTeleportPosition(this.governor.level, clone, 9, 50);
                if (safeSpawnPos != null) {
                    clone.moveTo(safeSpawnPos);
                    double dx = target.getX() - clone.getX();
                    double dz = target.getZ() - clone.getZ();
                    double dy = target.getEyeY() - clone.getEyeY();

                    float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx))) - 90F;

                    clone.yRot = yaw;
                    clone.yHeadRot = yaw;
                    clone.yBodyRot = yaw;
                }
            }
        }
    }
}


