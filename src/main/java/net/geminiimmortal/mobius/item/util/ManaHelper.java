package net.geminiimmortal.mobius.item.util;

import net.geminiimmortal.mobius.integration.CuriosCompat;
import net.geminiimmortal.mobius.item.custom.ManaVial;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class ManaHelper {

    /**
     * Attempts to consume a specific amount of mana from a player's equipped Curios first,
     * then their standard inventory.
     *
     * @param player The player using the artifact
     * @param amount The amount of mana to draw
     * @param simulate If true, only checks if enough mana is available without draining
     * @return true if the full amount was successfully drawn (or is available)
     */
    public static boolean consumeMana(PlayerEntity player, int amount, boolean simulate) {
        if (player.isCreative()) {
            return true;
        }

        List<ItemStack> manaSources = new ArrayList<>();

        // 1. Gather Mana Vials from Curios slots (prioritize belt/curios)
        if (ModList.get().isLoaded("curios")) {
            CuriosCompat.getCurioVials(player, manaSources);
        }

        // 2. Gather Mana Vials from main inventory & offhand
        for (int i = 0; i < player.inventory.getContainerSize(); i++) {
            ItemStack stack = player.inventory.getItem(i);
            if (stack.getItem() instanceof ManaVial) {
                manaSources.add(stack);
            }
        }

        // 3. Calculate total available mana across all sources
        int totalAvailable = 0;
        for (ItemStack vial : manaSources) {
            totalAvailable += ManaVial.getStoredMana(vial); // Replace with your NBT/Capability getter
            if (totalAvailable >= amount && simulate) {
                return true;
            }
        }

        if (totalAvailable < amount) {
            player.displayClientMessage(new TranslationTextComponent("item.mobius.staff.not_enough_mana"), true);
            return false;
        }

        if (simulate) {
            return true;
        }

        // 4. Drain mana sequentially across vials until the requirement is met
        int remainingToDrain = amount;
        for (ItemStack vial : manaSources) {
            int current = ManaVial.getStoredMana(vial);
            int toExtract = Math.min(current, remainingToDrain);
            ManaVial.setStoredMana(vial, current - toExtract); // Replace with your NBT/Capability setter
            remainingToDrain -= toExtract;

            if (remainingToDrain <= 0) {
                break;
            }
        }

        return true;
    }
}
