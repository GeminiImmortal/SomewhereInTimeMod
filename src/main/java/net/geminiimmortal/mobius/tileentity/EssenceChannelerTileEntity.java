package net.geminiimmortal.mobius.tileentity;

import net.geminiimmortal.mobius.recipe.AstralConduitRecipe;
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

    private void craft(AstralConduitRecipe recipe) {
        // Consume one item from each input slot
        itemHandler.getStackInSlot(0).shrink(1);
        itemHandler.getStackInSlot(1).shrink(1);
        itemHandler.getStackInSlot(2).shrink(1);

        // Place the recipe's output in the output slot
        ItemStack outputStack = itemHandler.getStackInSlot(3);
        ItemStack recipeOutput = recipe.getResultItem();

        if (outputStack.isEmpty()) {
            itemHandler.insertItem(3, recipeOutput.copy(), false);
        }
    }


    private boolean canCraft(AstralConduitRecipe recipe) {
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
        if (!this.level.isClientSide) { // Server-side only
            RecipeManager recipeManager = this.level.getRecipeManager();
            Optional<AstralConduitRecipe> recipe = recipeManager.getRecipeFor(
                    ModRecipeTypes.ASTRAL_CONDUIT_RECIPE,
                    new Inventory(itemHandler.getStackInSlot(0), itemHandler.getStackInSlot(1), itemHandler.getStackInSlot(2)),
                    this.level
            );

            if (recipe.isPresent() && !hasCrafted) {
                ItemStack recipeOutput = recipe.get().getResultItem();

                // Check if the output slot is empty or matches the recipe output
                ItemStack outputSlotStack = itemHandler.getStackInSlot(3);
                if (outputSlotStack.isEmpty() || (ItemStack.isSame(outputSlotStack, recipeOutput) &&
                        outputSlotStack.getCount() + recipeOutput.getCount() <= outputSlotStack.getMaxStackSize())) {

                    if (this.itemHandler.getStackInSlot(3).isEmpty()){
                        itemHandler.getStackInSlot(0).shrink(1);
                        itemHandler.getStackInSlot(1).shrink(1);
                        itemHandler.getStackInSlot(2).shrink(1);

                        // Clear the output slot
                        itemHandler.setStackInSlot(3, ItemStack.EMPTY);
                    }

                    // Set the output slot and mark as crafted
                    itemHandler.setStackInSlot(3, recipeOutput.copy());
                    hasCrafted = true;
                    this.level.playSound(null,this.getBlockPos(), ModSounds.ESSENCE_CHANNELER.get(), SoundCategory.BLOCKS, 5.0f, 1.0f);
                }
            }

            // Reset the crafting flag if the output slot is empty
            if (itemHandler.getStackInSlot(3).isEmpty()) {
                hasCrafted = false;
            }
        }
    }




    private boolean hasSufficientInputs(AstralConduitRecipe recipe) {
        // Check if each input slot contains at least one of the required item
        return !itemHandler.getStackInSlot(0).isEmpty() && !itemHandler.getStackInSlot(1).isEmpty() && !itemHandler.getStackInSlot(2).isEmpty();
    }

}
