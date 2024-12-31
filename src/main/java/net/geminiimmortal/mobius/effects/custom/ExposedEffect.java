package net.geminiimmortal.mobius.effects.custom;

import net.geminiimmortal.mobius.effects.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;

public class ExposedEffect extends Effect {
    public ExposedEffect(EffectType type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (this == ModEffects.EXPOSED_EFFECT.get()) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;

            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}

