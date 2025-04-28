package net.geminiimmortal.mobius.entity.custom.spell.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class StunUtil {
    public static void stun(LivingEntity entity, int durationTicks) {
        if (!entity.level.isClientSide) {
            entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, durationTicks, 10, false, false, true));
            entity.addEffect(new EffectInstance(Effects.WEAKNESS, durationTicks, 1, false, false, true));
            // Optional: you can even add a custom "Stunned" effect if you want!
        }
    }
}
