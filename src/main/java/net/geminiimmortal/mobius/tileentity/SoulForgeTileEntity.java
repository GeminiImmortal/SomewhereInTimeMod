package net.geminiimmortal.mobius.tileentity;

import net.geminiimmortal.mobius.recipe.ModRecipeTypes;
import net.geminiimmortal.mobius.recipe.SoulForgeRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.geminiimmortal.mobius.item.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class SoulForgeTileEntity extends TileEntity implements ITickableTileEntity {
    private int progress = 0;
    private static final int maxProgress = 100;

    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public SoulForgeTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public SoulForgeTileEntity() {
        this(ModTileEntities.SOUL_FORGE_TILE_ENTITY.get());
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.load(state, nbt);
        this.progress = nbt.getInt("Progress");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        compound.putInt("Progress", this.progress);
        return super.save(compound);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();

            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }


            @Override
            public int getSlotLimit(int slot) {
                return 64; // Allows up to 64 items in slot 1
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (!isItemValid(slot, stack)) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }



    public void craft() {
        Inventory inv = new Inventory(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        assert level != null;
        Optional<SoulForgeRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.SOUL_FORGE_RECIPE, inv, level);

        recipe.ifPresent(iRecipe -> {

            ItemStack output = iRecipe.getResultItem();

            craftTheItem(output);
            System.out.println("Crafted: " + output);

            setChanged();
        });

    }

    private void craftTheItem(ItemStack output) {
        itemHandler.extractItem(0, 1, false);
        itemHandler.insertItem(1, output, false);
    }

    public int getProgress() {
        return progress;
    }

    public static int getMaxProgress() {
        return maxProgress;
    }

    @Override
    public void tick() {
        assert this.level != null;
        if (this.level.isClientSide)
            return;


        craft();
    }
}
