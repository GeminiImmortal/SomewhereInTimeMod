package net.geminiimmortal.mobius.tileentity;

import net.geminiimmortal.mobius.item.custom.ManaVial;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LatentManaCollectorTileEntity extends TileEntity implements ITickableTileEntity {
    private static final int MAX_MANA = 10000;
    private static final int GENERATION_RATE = 0; // Mana per tick in sunlight
    private static final int TRANSFER_RATE = 5; // Mana per tick into flask
    private int manaStored = 0;

    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public LatentManaCollectorTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public LatentManaCollectorTileEntity() {
        this(ModTileEntities.LATENT_MANA_COLLECTOR.get());
    }

    private int getGenerationRate(TileEntity tileEntity, World world) {
        assert level != null;
        if (!level.isClientSide()) {
        if (this.getTileEntity().getLevel().dimension().equals(World.OVERWORLD)) return 1;
        if (this.getTileEntity().getLevel().dimension().equals(ModDimensions.MOBIUS_WORLD)) return 3;
}
        return 0;
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

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            return 0;
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 1; // Number of synchronized fields
        }
    };

    public IIntArray getFields() {
        return this.fields;
    }

    @Override
    public void tick() {
        if (!level.isClientSide) {
            BlockPos pos = getBlockPos();
            if (level.canSeeSky(pos.above()) && level.isDay()) {
                manaStored = Math.min(manaStored + this.getGenerationRate(this, this.level), MAX_MANA);
                setChanged();
            }

            // Charge mana flask
            ItemStack flask = this.getInventory().getStackInSlot(0);
            if (!flask.isEmpty() && flask.getItem() instanceof ManaVial) {
                ManaVial manaFlask = (ManaVial) flask.getItem();
                int transferrableMana = Math.min(TRANSFER_RATE, manaStored);
                if (manaFlask.addMana(flask, transferrableMana)) {
                    manaStored -= transferrableMana;
                    setChanged();
                }
            }
        }
    }

    public int getManaStored() {
        return manaStored;
    }

    public void extractMana(int amount) {
        manaStored = Math.max(manaStored - amount, 0);
        setChanged();
    }

    public ItemStackHandler getInventory() {
        return this.itemHandler;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putInt("ManaStored", manaStored);
        compound.put("Inventory", this.getInventory().serializeNBT());
        return compound;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        manaStored = compound.getInt("ManaStored");
        this.getInventory().deserializeNBT(compound.getCompound("Inventory"));
    }
}

