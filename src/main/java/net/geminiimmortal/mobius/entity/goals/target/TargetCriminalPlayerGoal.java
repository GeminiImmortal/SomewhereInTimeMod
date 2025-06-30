package net.geminiimmortal.mobius.entity.goals.target;

import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.geminiimmortal.mobius.capability.infamy.IInfamy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public class TargetCriminalPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {

    public TargetCriminalPlayerGoal(MobEntity goalOwner) {
        // Always call super with `checkSight = true` and `nearbyOnly = false` or as needed
        super(goalOwner, PlayerEntity.class, 10, true, false, player -> isCriminal((PlayerEntity) player));
    }

    private static boolean isCriminal(@Nullable PlayerEntity player) {
        if (player == null) return false;
        return player.getCapability(ModCapabilities.INFAMY_CAPABILITY)
                .map(cap -> cap.getInfamyTier().ordinal() >= IInfamy.InfamyTier.CRIMINAL.ordinal())
                .orElse(false);
    }
}

