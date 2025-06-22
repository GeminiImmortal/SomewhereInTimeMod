package net.geminiimmortal.mobius.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;

import java.util.ArrayList;
import java.util.List;

public class JournalHelper {
    public static void addPage(ItemStack journal, int pageNumber) {
        CompoundNBT tag = journal.getOrCreateTag();
        ListNBT pages = tag.getList("Pages", 3); // 3 = int
        if (!containsPage(pages, pageNumber)) {
            pages.add(IntNBT.valueOf(pageNumber));
            tag.put("Pages", pages);
        }
    }

    public static List<Integer> getUnlockedPages(ItemStack journal) {
        List<Integer> result = new ArrayList<>();
        CompoundNBT tag = journal.getOrCreateTag();
        ListNBT pages = tag.getList("Pages", 3); // 3 = int
        for (INBT nbt : pages) {
            result.add(((IntNBT) nbt).getAsInt());
        }
        return result;
    }


    public static boolean hasPage(ItemStack journal, int pageNumber) {
        ListNBT pages = journal.getOrCreateTag().getList("Pages", 3);
        return containsPage(pages, pageNumber);
    }

    private static boolean containsPage(ListNBT list, int val) {
        for (INBT nbt : list) {
            if (((IntNBT)nbt).getAsInt() == val) return true;
        }
        return false;
    }
}

