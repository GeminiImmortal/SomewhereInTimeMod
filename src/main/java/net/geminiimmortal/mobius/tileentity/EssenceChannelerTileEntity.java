package net.geminiimmortal.mobius.tileentity;

import net.geminiimmortal.mobius.recipe.AstralConduitRecipe;
import net.geminiimmortal.mobius.recipe.EssenceChannelerRecipe;
import net.geminiimmortal.mobius.recipe.ModRecipeTypes;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class EssenceChannelerTileEntity extends TileEntity implements ITickableTileEntity {


    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public EssenceChannelerTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public EssenceChannelerTileEntity() {
        this(ModTileEntities.ESSENCE_CHANNELER_TILE_ENTITY.get());
    }

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            return 4;
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 4; // Number of synchronized fields
        }
    };

    public IIntArray getFields() {
        return this.fields;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        return super.save(compound);
    }


    private ItemStackHandler createHandler() {
        return new ItemStackHandler(4) {
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
                return 1; // Allows up to 64 items in slot 1
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

    public void updateCraftingResult() {
        Inventory fakeInv = new Inventory(3);
        fakeInv.setItem(0, itemHandler.getStackInSlot(0));
        fakeInv.setItem(1, itemHandler.getStackInSlot(1));
        fakeInv.setItem(2, itemHandler.getStackInSlot(2));

        Optional<EssenceChannelerRecipe> match = level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.ESSENCE_CHANNELER_RECIPE, fakeInv, level);

        ItemStack currentOutput = itemHandler.getStackInSlot(3);
        if (match.isPresent()) {
            ItemStack result = match.get().getResultItem();
            if (!ItemStack.isSame(currentOutput, result)) {
                itemHandler.setStackInSlot(3, result.copy());
            }
        } else {
            if (!currentOutput.isEmpty()) {
                itemHandler.setStackInSlot(3, ItemStack.EMPTY);
            }
        }
    }


    private boolean canCraft(EssenceChannelerRecipe recipe) {
        ItemStack outputStack = itemHandler.getStackInSlot(3); // Slot 3 is the output slot
        ItemStack recipeOutput = recipe.getResultItem();

        // Check if the output slot is empty or has room for the result
        return (outputStack.isEmpty() ||
                (ItemStack.isSame(outputStack, recipeOutput) &&
                        outputStack.getCount() + recipeOutput.getCount() <= outputStack.getMaxStackSize()));
    }


    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }


    private void craftTheItem(ItemStack output) {
        itemHandler.extractItem(0, 1, false);
        itemHandler.extractItem(1, 1, false);
        itemHandler.extractItem(2, 1, false);
    }

    private boolean hasCrafted = false; // Tracks whether crafting has occurred


    @Override
    public void tick() {

        assert this.level != null;
        if (level == null || level.isClientSide) return;

        updateCraftingResult();
    }




    private boolean hasSufficientInputs(EssenceChannelerRecipe recipe) {
        // Check if each input slot contains at least one of the required item
        return !itemHandler.getStackInSlot(0).isEmpty() && !itemHandler.getStackInSlot(1).isEmpty() && !itemHandler.getStackInSlot(2).isEmpty();
    }

}
