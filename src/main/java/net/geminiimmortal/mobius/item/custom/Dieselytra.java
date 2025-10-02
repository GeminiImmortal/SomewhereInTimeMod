package net.geminiimmortal.mobius.item.custom;


import net.geminiimmortal.mobius.item.ModItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;

public class Dieselytra extends ElytraItem implements IForgeItem {
    public Dieselytra(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == ModItems.NICKEL_INGOT.get() || super.isValidRepairItem(toRepair, repair);
    }


    @Override
    public boolean canElytraFly(ItemStack stack, net.minecraft.entity.LivingEntity entity) {
        return ElytraItem.isFlyEnabled(stack);
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, net.minecraft.entity.LivingEntity entity, int flightTicks) {
        if (!entity.level.isClientSide && (flightTicks + 1) % 20 == 0) {
            stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(net.minecraft.inventory.EquipmentSlotType.CHEST));
        }
        return true;
    }

    @Override
    public EquipmentSlotType getEquipmentSlot(ItemStack stack){
        return EquipmentSlotType.CHEST;
    }
}

