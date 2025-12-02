package net.geminiimmortal.mobius.effects.custom;

import net.geminiimmortal.mobius.effects.ModEffects;
import net.geminiimmortal.mobius.entity.custom.GrimcrowCaptainBossEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.Objects;

public class ChargedEffect extends Effect {
    public ChargedEffect(EffectType type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (this == ModEffects.CHARGED_EFFECT.get()) {
            if (entity instanceof GrimcrowCaptainBossEntity) {
                GrimcrowCaptainBossEntity boss = (GrimcrowCaptainBossEntity) entity;
                double bossAttackStat = Objects.requireNonNull(boss.getAttribute(Attributes.ATTACK_DAMAGE)).getBaseValue();
                double bossSpeedStat = Objects.requireNonNull(boss.getAttribute(Attributes.MOVEMENT_SPEED)).getBaseValue();
                double bossAttackStatMod = bossAttackStat * (0.01D * amplifier);
                double bossSpeedStatMod = bossSpeedStat * (0.005 * amplifier);
                this.addAttributeModifier(Attributes.ATTACK_DAMAGE, boss.getStringUUID(), bossAttackStatMod, AttributeModifier.Operation.ADDITION);
                this.addAttributeModifier(Attributes.MOVEMENT_SPEED, boss.getStringUUID(), bossSpeedStatMod, AttributeModifier.Operation.ADDITION);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}

