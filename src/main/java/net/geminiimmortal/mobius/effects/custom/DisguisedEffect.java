package net.geminiimmortal.mobius.effects.custom;

import net.geminiimmortal.mobius.entity.goals.target.IDisguisedPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import javax.annotation.Nullable;

public class DisguisedEffect extends Effect implements IDisguisedPlayer {
    public DisguisedEffect(EffectType type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity) {
            isCriminal((PlayerEntity) entity);
        }
        if (entity instanceof PlayerEntity && isCriminal((PlayerEntity) entity)) {
            entity.removeEffect(this);
        }
    }

    public static boolean isCriminal(@Nullable PlayerEntity player) {
        if (player != null) {
            return player.swinging;
        }
        return false;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}

