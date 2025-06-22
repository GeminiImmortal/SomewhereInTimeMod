package net.geminiimmortal.mobius.item.custom;

import net.geminiimmortal.mobius.util.JournalHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class JournalPage extends Item {
    public JournalPage(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        // If player has journal in inventory
        for (ItemStack stack : player.inventory.items) {
            if (stack.getItem() instanceof MysteriousJournal) {
                int page = getPageNumber(player.getItemInHand(hand));
                JournalHelper.addPage(stack, page);
                player.getItemInHand(hand).shrink(1); // consume page
                player.sendMessage(new StringTextComponent("Page " + page + " added to journal!"), player.getUUID());
                break;
            }
        }
        return super.use(world, player, hand);
    }


    public static int getPageNumber(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        return nbt.getInt("PageNumber");
    }

    public static void setPageNumber(ItemStack stack, int pageNumber) {
        stack.getOrCreateTag().putInt("PageNumber", pageNumber);
    }
}

