package net.geminiimmortal.mobius.util;

import net.geminiimmortal.mobius.effects.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CustomDamageEventHandler {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();

        if (entity.hasEffect(ModEffects.EXPOSED_EFFECT.get())) {
            float originalDamage = event.getAmount();
            event.setAmount(originalDamage * 2);
        }
    }
}

