package net.geminiimmortal.mobius.item.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import static net.geminiimmortal.mobius.item.custom.Dieselytra.handleFlightTick;

public class DieselytraFlightTickHelper {
    public static boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return handleFlightTick(stack, entity, flightTicks);
    }
}
