package net.geminiimmortal.mobius.effects.custom;

import net.geminiimmortal.mobius.effects.ModEffects;
import net.geminiimmortal.mobius.entity.custom.GrimcrowCaptainBossEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;

import java.util.Objects;

public class AstralGraceEffect extends Effect {
    public AstralGraceEffect(EffectType type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (this == ModEffects.ASTRAL_GRACE.get()) {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                this.addAttributeModifier(
                        Attributes.MOVEMENT_SPEED,
                        player.getStringUUID(),
                        0.20D,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                );
                this.addAttributeModifier(
                        Attributes.MAX_HEALTH,
                        player.getStringUUID(),
                        20.0D,
                        AttributeModifier.Operation.ADDITION
                );
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}

