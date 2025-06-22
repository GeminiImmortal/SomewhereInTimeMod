package net.geminiimmortal.mobius.container.custom;

import net.geminiimmortal.mobius.container.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

public class JournalContainer extends Container {
    public static final int CONTAINER_ID = 0; // use if you're syncing data

    public JournalContainer(int windowId, PlayerInventory playerInventory) {
        super(ModContainers.JOURNAL_CONTAINER.get(), windowId);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return true; // Always true because this is a virtual container
    }
}

