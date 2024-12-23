package net.geminiimmortal.mobius.util;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CustomDamageEventHandler {
    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getMsgId().equals("knives_out")) {
            // Custom behavior, like applying effects
        //    event.getEntityLiving().addEffect(new EffectInstance(Effects.WITHER, 40, 1));
        }
    }
}

