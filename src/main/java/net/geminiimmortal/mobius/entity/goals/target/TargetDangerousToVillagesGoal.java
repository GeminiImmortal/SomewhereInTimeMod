package net.geminiimmortal.mobius.entity.goals.target;

import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.geminiimmortal.mobius.capability.infamy.IInfamy;
import net.geminiimmortal.mobius.faction.FactionType;
import net.geminiimmortal.mobius.faction.IFactionCarrier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public class TargetDangerousToVillagesGoal extends NearestAttackableTargetGoal<LivingEntity> {

    public TargetDangerousToVillagesGoal(MobEntity goalOwner) {
        // Always call super with `checkSight = true` and `nearbyOnly = false` or as needed
        super(goalOwner, LivingEntity.class, 10, true, false, danger -> isDangerous((LivingEntity) danger));
    }

    private static boolean isDangerous(@Nullable LivingEntity danger) {
        if (danger instanceof IFactionCarrier) {
            FactionType targetFaction = (((IFactionCarrier) danger).getFaction());
            return targetFaction == FactionType.DANGEROUS_TO_VILLAGES;
        }
        return false;
    }
}

