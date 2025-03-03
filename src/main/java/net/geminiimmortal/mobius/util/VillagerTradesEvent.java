package net.geminiimmortal.mobius.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.item.ModItems;
import net.geminiimmortal.mobius.villager.ModVillagers;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;


@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID)
public class VillagerTradesEvent {
    @SubscribeEvent
    public static void addCustomTrades(net.minecraftforge.event.village.VillagerTradesEvent event) {
        if (event.getType() == ModVillagers.MONSTER_HUNTER.get()) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();

            // Level 1
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(Items.GUNPOWDER, 8),
                    10, 8, 0.02f));
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.FLINT_AND_STEEL, 1),
                    new ItemStack(Items.EMERALD, 1),
                    6, 12, 0.05f));

            // Level 2
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(ModItems.FAE_LEATHER.get(), 2),
                    6, 12, 0.035f));
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.BONE, 24),
                    new ItemStack(Items.EMERALD, 1),
                    6, 12, 0.050f));

            // Level 3
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.IRON_INGOT, 8),
                    new ItemStack(Items.EMERALD, 4),
                    6, 12, 0.075f));

            // Level 4
            trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 4),
                    new ItemStack(ModItems.WASP_PAPER_BLOCK.get(), 6),
                    6, 12, 0.095f));

            // Level 5
            trades.get(5).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 40),
                    new ItemStack(ModItems.GAIA_STAR.get(), 1),
                    6, 12, 0.1f));
        }

            if (event.getType() == ModVillagers.MAGISMITH.get()) {
                Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();

                trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(ModItems.FAE_LEATHER.get(), 2),
                        new ItemStack(Items.EMERALD, 6),
                        10, 8, 0.06f));
                trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.COAL_BLOCK, 1),
                        new ItemStack(Items.EMERALD, 8),
                        6, 12, 0.05f));
                trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(ModItems.NICKEL_ORE.get(), 4),
                        new ItemStack(Items.EMERALD, 1),
                        10, 8, 0.065f));
                trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 24),
                        new ItemStack(ModItems.RAW_MANA.get(), 4),
                        6, 12, 0.069f));
                trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.IRON_INGOT, 4),
                        new ItemStack(Items.EMERALD, 16),
                        6, 12, 0.08f));
                trades.get(5).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 10),
                        new ItemStack(ModItems.NICKEL_INGOT.get(), 1),
                        6, 12, 0.1f));
            }

        if (event.getType() == ModVillagers.SAGE.get()) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(ModItems.FAE_LEATHER.get(), 8),
                    new ItemStack(ModItems.RAW_MANA.get(), 4),
                    10, 8, 0.06f));
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(ModItems.RAW_MANA.get(), 48),
                    new ItemStack(ModItems.STAFF_PROTECTION_OBSIDIAN_ROD_FAE_LEATHER_BINDING.get(), 1),
                    6, 12, 0.095f));
        }
    }
}
