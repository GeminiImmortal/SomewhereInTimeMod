package net.geminiimmortal.mobius.container.custom;

import net.geminiimmortal.mobius.container.ModContainers;
import net.geminiimmortal.mobius.recipe.AstralConduitRecipe;
import net.geminiimmortal.mobius.recipe.ModRecipeTypes;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.geminiimmortal.mobius.tileentity.AstralConduitTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.Optional;

public class AstralConduitContainer extends Container {
    private final TileEntity tileEntity;
    private final PlayerEntity playerEntity;
    private final IItemHandler playerInventory;
    private IIntArray fields;




    public AstralConduitContainer(int windowId, World world, BlockPos pos,
                                  PlayerInventory playerInventory, PlayerEntity player) {
        super(ModContainers.ASTRAL_CONDUIT_CONTAINER.get(), windowId);
        this.tileEntity = world.getBlockEntity(pos);





        if (tileEntity instanceof AstralConduitTileEntity) {
            this.fields = ((AstralConduitTileEntity) tileEntity).getFields();
            addDataSlots(fields); // Synchronize the fields
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 80, 13));
                addSlot(new SlotItemHandler(h, 1, 106, 22));
                addSlot(new SlotItemHandler(h, 2, 54, 22));
                this.addSlot(new SlotItemHandler(h, 3, 80, 61) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false; // No placing into output
                    }

                    @Override
                    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
                        assert tileEntity.getLevel() != null;
                        tileEntity.getLevel().playSound(null, tileEntity.getBlockPos(), ModSounds.ASTRAL_CONDUIT.get(), SoundCategory.BLOCKS, 20.0f, 1.0f);

                        super.onTake(player, stack);

                        // Remove one item from each input slot
                        for (int i = 0; i < 3; i++) {
                            ItemStack input = h.getStackInSlot(i);
                            if (!input.isEmpty()) {
                                h.extractItem(i, 1, false);
                            }
                        }

                        // Recalculate output
                        if (tileEntity.getLevel() != null && !tileEntity.getLevel().isClientSide) {
                            ((AstralConduitTileEntity) tileEntity).updateCraftingResult(); // Ensure this method exists in your TileEntity
                            broadcastChanges(); // Sync to client
                        }
                        return stack;
                    }
                });

            });
        } else {
            this.fields = fields; // Fallback if tileEntity is null or of the wrong type
        }
        playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        layoutPlayerInventorySlots(8, 86);



        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 80, 13));
                addSlot(new SlotItemHandler(h, 1, 106, 22));
                addSlot(new SlotItemHandler(h, 2, 54, 22));
                this.addSlot(new SlotItemHandler(h, 3, 80, 61) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false; // Output only
                    }

                    @Override
                    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
                        super.onTake(player, stack);
                        assert tileEntity.getLevel() != null;
                        tileEntity.getLevel().playSound(null, tileEntity.getBlockPos(), ModSounds.ASTRAL_CONDUIT.get(), SoundCategory.BLOCKS, 20.0f, 1.0f);
                        // Consume 1 of each input
                        for (int i = 0; i < 3; i++) {
                            ItemStack input = h.getStackInSlot(i);
                            if (!input.isEmpty()) {
                                h.extractItem(i, 1, false);
                            }
                        }

                        // Recalculate crafting result
                        if (player.level instanceof ServerWorld) {
                            ((AstralConduitTileEntity) tileEntity).updateCraftingResult();
                            // Optional: sync container changes
                            broadcastChanges();
                        }
                        return stack;
                    }
                });

            });
        }
    }

    public AstralConduitTileEntity getTileEntity() {
        return (AstralConduitTileEntity) tileEntity;
    }

    private void updateCraftingResult() {
        if (this.tileEntity.getLevel() == null) return;

        assert this.tileEntity.getLevel() != null;
        Optional<AstralConduitRecipe> match = this.tileEntity.getLevel().getRecipeManager()
                .getRecipeFor(ModRecipeTypes.ASTRAL_CONDUIT_RECIPE, new Inventory(
                        this.slots.get(0).getItem(),
                        this.slots.get(1).getItem(),
                        this.slots.get(2).getItem()
                ), this.tileEntity.getLevel());

        if (match.isPresent()) {
            this.slots.get(3).set(match.get().getResultItem().copy());
        } else {
            this.slots.get(3).set(ItemStack.EMPTY);
        }

        broadcastChanges(); // Sync with client
    }




    @Override
    public boolean stillValid(PlayerEntity player) {
        // Check if player is within interaction range and block is not destroyed
        if (this.playerEntity.level == null || this.tileEntity.getTileEntity().isRemoved()) {
            return false;
        }
        return player.distanceToSqr((double) this.tileEntity.getBlockPos().getX() + 0.5,
                (double) this.tileEntity.getBlockPos().getY() + 0.5,
                (double) this.tileEntity.getBlockPos().getZ() + 0.5) <= 64.0;
    }


    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 4;  // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        Slot sourceSlot = this.slots.get(index);  // `inventorySlots` to `slots`
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  // `getHasStack` to `hasItem`
        ItemStack sourceStack = sourceSlot.getItem();  // `getStack` to `getItem`
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the player's inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.isEmpty()) {  // `getCount == 0` to `isEmpty`
            sourceSlot.set(ItemStack.EMPTY);  // `putStack` to `set`
        } else {
            sourceSlot.setChanged();  // `onSlotChanged` to `setChanged`
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }
}
