package net.geminiimmortal.mobius.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandlerModifiable;
import javax.annotation.Nonnull;

public class ManaRefinerySidedItemHandler implements IItemHandlerModifiable {
    private final IItemHandlerModifiable inner;
    private final Direction side;

    public ManaRefinerySidedItemHandler(IItemHandlerModifiable inner, Direction side) {
        this.inner = inner;
        this.side = side;
    }

    @Override
    public int getSlots() {
        return inner.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return inner.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        // Allow top and side hoppers to insert into INPUT_SLOT only
        if (side != Direction.DOWN && slot == 1) {
            return inner.insertItem(slot, stack, simulate);
        }
        if (side != Direction.DOWN && side != Direction.UP && slot == 0) {
            return inner.insertItem(slot, stack, simulate);
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        // Allow bottom hoppers to pull from OUTPUT_SLOT only
        if (side == Direction.DOWN && slot == 2) {
            return inner.extractItem(slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return inner.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return side != Direction.DOWN && slot == 1 && inner.isItemValid(slot, stack);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        inner.setStackInSlot(slot, stack);
    }
}
