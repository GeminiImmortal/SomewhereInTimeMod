package net.geminiimmortal.mobius.effects.custom;

import net.geminiimmortal.mobius.effects.ModEffects;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;

public class LordDecreeEffect extends Effect {
    public LordDecreeEffect(EffectType type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (this == ModEffects.LORD_DECREE_EFFECT.get()) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                player.addEffect(new EffectInstance(Effects.WITHER, 100, 2));
                player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 4));
                player.addEffect(new EffectInstance(Effects.CONFUSION, 100, 3));
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}

