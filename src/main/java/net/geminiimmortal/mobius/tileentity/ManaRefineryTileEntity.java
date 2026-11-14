package net.geminiimmortal.mobius.tileentity;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.network.ClientPlaySoundPacket;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.geminiimmortal.mobius.recipe.ManaRefineryRecipe;
import net.geminiimmortal.mobius.recipe.ModRecipeTypes;
import net.geminiimmortal.mobius.recipe.SoulForgeRecipe;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class ManaRefineryTileEntity extends TileEntity implements ITickableTileEntity {
    private int progress = 0;
    private static final int maxProgress = 100;
    public static final int WORK_TIME = 2 * 120;

    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public ManaRefineryTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public ManaRefineryTileEntity() {
        this(ModTileEntities.MANA_REFINERY_TILE_ENTITY.get());
    }

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            if (index == 1) return progress;
            if (index == 2) return WORK_TIME;
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 1) progress = value;
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public IIntArray getFields() {
        return this.fields;
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
        return new ItemStackHandler(3) {
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
                return 64;
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
        Optional<ManaRefineryRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.MANA_REFINERY_RECIPE, inv, level);

        recipe.ifPresent(iRecipe -> {

            if (progress < WORK_TIME) {
                ++progress;
                if (this.level != null && !this.level.isClientSide()) {
                    ServerWorld world = (ServerWorld) this.getLevel();
                    assert world != null;
                    world.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), 2, 0.005, 0.1, 0.005, 0.1);
                }
            }

            if (progress >= WORK_TIME && itemHandler.getStackInSlot(2).getCount() < 64) {
                ItemStack output = iRecipe.getResultItem();
                if (this.getLevel() != null && !this.level.isClientSide()) {
                    this.getLevel().playSound(null, this.getBlockPos(), ModSounds.SPARKS.get(), SoundCategory.BLOCKS, 1.0f, 0.8f);
                }
                craftTheItem(output);

                progress = 0;
                setChanged();
            }
        });

    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }


    private void craftTheItem(ItemStack output) {
        itemHandler.extractItem(0, 1, false);
        itemHandler.extractItem(1, 1, false);
        itemHandler.insertItem(2, output, false);
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
