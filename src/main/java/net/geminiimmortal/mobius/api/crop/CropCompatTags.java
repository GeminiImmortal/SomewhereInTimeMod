package net.geminiimmortal.mobius.api.crop;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public final class CropCompatTags {

    // Vanilla & Forge
    public static final INamedTag<Block> MINECRAFT_CROPS = BlockTags.CROPS;
    public static final INamedTag<Block> BEE_GROWABLES = BlockTags.BEE_GROWABLES;
    public static final INamedTag<Block> FORGE_CROPS_BLOCK = blockTag("forge", "crops");

    public static final INamedTag<Item> FORGE_CROPS = itemTag("forge", "crops");
    public static final INamedTag<Item> FORGE_SEEDS = itemTag("forge", "seeds");
    public static final INamedTag<Item> FORGE_GRAIN = itemTag("forge", "grain");
    public static final INamedTag<Item> FORGE_VEGETABLES = itemTag("forge", "vegetables");
    public static final INamedTag<Item> FORGE_FRUITS = itemTag("forge", "fruits");
    public static final INamedTag<Item> FORGE_BERRIES = itemTag("forge", "berries");
    public static final INamedTag<Item> FORGE_HERBS = itemTag("forge", "herbs");
    public static final INamedTag<Item> FORGE_MUSHROOMS = itemTag("forge", "mushrooms");
    public static final INamedTag<Item> FORGE_SALAD_INGREDIENTS = itemTag("forge", "salad_ingredients");
    public static final INamedTag<Item> FD_CABBAGE_ROLL_INGREDIENTS = itemTag("farmersdelight", "cabbage_roll_ingredients");

    // --- Serene Seasons Block Tags ---
    public static final INamedTag<Block> SS_SPRING_CROPS_BLOCK = blockTag("sereneseasons", "spring_crops");
    public static final INamedTag<Block> SS_SUMMER_CROPS_BLOCK = blockTag("sereneseasons", "summer_crops");
    public static final INamedTag<Block> SS_AUTUMN_CROPS_BLOCK = blockTag("sereneseasons", "autumn_crops");
    public static final INamedTag<Block> SS_WINTER_CROPS_BLOCK = blockTag("sereneseasons", "winter_crops");
    public static final INamedTag<Block> SS_UNBREAKABLE_BLOCK = blockTag("sereneseasons", "unbreakable_infertile_crops");

    // --- Serene Seasons Item Tags (for seed tooltips) ---
    public static final INamedTag<Item> SS_SPRING_CROPS_ITEM = itemTag("sereneseasons", "spring_crops");
    public static final INamedTag<Item> SS_SUMMER_CROPS_ITEM = itemTag("sereneseasons", "summer_crops");
    public static final INamedTag<Item> SS_AUTUMN_CROPS_ITEM = itemTag("sereneseasons", "autumn_crops");
    public static final INamedTag<Item> SS_WINTER_CROPS_ITEM = itemTag("sereneseasons", "winter_crops");
    public static final INamedTag<Item> SS_UNBREAKABLE_ITEM = itemTag("sereneseasons", "unbreakable_infertile_crops");

    public static INamedTag<Item> forgeCropSubTag(String name) {
        return itemTag("forge", "crops/" + name);
    }

    public static INamedTag<Item> forgeSeedSubTag(String name) {
        return itemTag("forge", "seeds/" + name);
    }

    public static INamedTag<Item> forgeCategorySubTag(String categoryPlural, String name) {
        return itemTag("forge", categoryPlural + "/" + name);
    }

    public static INamedTag<Block> blockTag(String namespace, String path) {
        return BlockTags.createOptional(new ResourceLocation(namespace, path));
    }

    public static INamedTag<Item> itemTag(String namespace, String path) {
        return ItemTags.createOptional(new ResourceLocation(namespace, path));
    }
}