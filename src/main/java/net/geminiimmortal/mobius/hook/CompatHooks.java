package net.geminiimmortal.mobius.hook;

import net.geminiimmortal.mobius.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

public class CompatHooks {
    public static Function<LivingEntity, ItemStack> getCurioElytraStack = entity -> ItemStack.EMPTY;

    public static boolean hasCurioElytra(LivingEntity entity) {
        return !getCurioElytraStack.apply(entity).isEmpty();
    }

    public static ItemStack getActiveElytraStack(LivingEntity entity) {
            ItemStack curioStack = getCurioElytraStack.apply(entity);
            if (!curioStack.isEmpty()) {
                return curioStack;
            }

            ItemStack chestStack = entity.getItemBySlot(EquipmentSlotType.CHEST);
            if (chestStack.getItem() == ModItems.DIESELYTRA.get() && ElytraItem.isFlyEnabled(chestStack)) {
                return chestStack;
            }

            return ItemStack.EMPTY;
    }
}
