package net.geminiimmortal.mobius.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;

import java.util.Random;

public class BasicTrade implements VillagerTrades.ITrade {
    private final ItemStack input, output;
    private final int maxUses, xp;
    private final float priceMultiplier;

    public BasicTrade(ItemStack input, ItemStack output, int maxUses, int xp, float priceMultiplier) {
        this.input = input;
        this.output = output;
        this.maxUses = maxUses;
        this.xp = xp;
        this.priceMultiplier = priceMultiplier;
    }

    @Override
    public MerchantOffer getOffer(Entity entity, Random rand) {
        return new MerchantOffer(input.copy(), output.copy(), maxUses, xp, priceMultiplier);
    }
}

