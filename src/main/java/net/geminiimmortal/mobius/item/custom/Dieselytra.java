package net.geminiimmortal.mobius.item.custom;


import net.geminiimmortal.mobius.hook.CompatHooks;
import net.geminiimmortal.mobius.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;

import javax.annotation.Nullable;

public class Dieselytra extends ElytraItem implements IForgeItem {
    public Dieselytra(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == ModItems.NICKEL_INGOT.get() || super.isValidRepairItem(toRepair, repair);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return ElytraItem.isFlyEnabled(stack);
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return handleFlightTick(stack, entity, flightTicks);
    }

    @Nullable
    @Override
    public EquipmentSlotType getEquipmentSlot(ItemStack stack){
        return EquipmentSlotType.CHEST;
    }

    public static boolean handleFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        ItemStack activeElytra = CompatHooks.getActiveElytraStack(entity);
        if (!entity.level.isClientSide() && (flightTicks + 1) % 20 == 0) {
            if (CompatHooks.hasCurioElytra(entity)) {
                activeElytra.hurtAndBreak(1, entity, e -> {
                    // Plays break animation on chest if worn there; Curios handles its own break sync
                    if (entity.getItemBySlot(EquipmentSlotType.CHEST) == activeElytra) {
                        e.getItemBySlot(EquipmentSlotType.CHEST).hurtAndBreak(1, entity, livingEntity -> e.broadcastBreakEvent(EquipmentSlotType.CHEST));
                    }
                });
            }
        }
        return true;
    }
}