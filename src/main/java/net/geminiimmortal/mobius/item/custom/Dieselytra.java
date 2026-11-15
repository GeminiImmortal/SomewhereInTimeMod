package net.geminiimmortal.mobius.item.custom;


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
        if (!entity.level.isClientSide() && (flightTicks + 1) % 20 == 0) {
            stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(net.minecraft.inventory.EquipmentSlotType.CHEST));
        }
        return true;
    }
}