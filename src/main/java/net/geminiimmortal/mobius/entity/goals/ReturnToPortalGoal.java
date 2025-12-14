package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.FaestagEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;
import java.util.List;

public class ReturnToPortalGoal extends Goal {
    FaestagEntity faestag;
    BlockPos target;

    public ReturnToPortalGoal(FaestagEntity faestag) {
        this.faestag = faestag;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        List<PlayerEntity> players = faestag.level.getEntitiesOfClass(PlayerEntity.class,
                this.faestag.getBoundingBox().inflate(24D),
                e -> (e instanceof PlayerEntity && !e.isCreative() && !e.isSpectator() && !e.isInvisible())
        );
        return !players.isEmpty() && !faestag.getSummoned();
    }

    @Override
    public void start() {
        faestag.setAlerted(true);
        this.target = faestag.getTargetPortalPosition();
    }

    @Override
    public void tick() {
        faestag.setAlerted(true);

        if (this.target != null) {
            faestag.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.5);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.faestag.getAlerted() && this.target != null;
    }
}
