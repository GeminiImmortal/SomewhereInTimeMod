package net.geminiimmortal.mobius.api.crop;

import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collection;
import java.util.function.Function;

public final class CropTagGeneratorHelper {

    public static void generateBlockTags(
            Collection<RegistryObject<Block>> modBlocks,
            Function<ITag.INamedTag<Block>, BlockTagsProvider.Builder<Block>> tagBuilder) {

        for (RegistryObject<Block> entry : modBlocks) {
            Block block = entry.get();
            if (!(block instanceof ICropCompat)) continue;

            ICropCompat crop = (ICropCompat) block;

            tagBuilder.apply(CropCompatTags.MINECRAFT_CROPS).add(block);
            tagBuilder.apply(CropCompatTags.FORGE_CROPS_BLOCK).add(block);

            if (crop.isBeeGrowable()) {
                tagBuilder.apply(CropCompatTags.BEE_GROWABLES).add(block);
            }

            // Serene Seasons Block Tags
            for (ICropCompat.Season season : crop.getFertileSeasons()) {
                switch (season) {
                    case SPRING: tagBuilder.apply(CropCompatTags.SS_SPRING_CROPS_BLOCK).add(block); break;
                    case SUMMER: tagBuilder.apply(CropCompatTags.SS_SUMMER_CROPS_BLOCK).add(block); break;
                    case AUTUMN: tagBuilder.apply(CropCompatTags.SS_AUTUMN_CROPS_BLOCK).add(block); break;
                    case WINTER: tagBuilder.apply(CropCompatTags.SS_WINTER_CROPS_BLOCK).add(block); break;
                }
            }

            if (crop.isUnbreakableInfertile()) {
                tagBuilder.apply(CropCompatTags.SS_UNBREAKABLE_BLOCK).add(block);
            }
        }
    }

    public static void generateItemTags(
            Collection<RegistryObject<Block>> modBlocks,
            Function<ITag.INamedTag<Item>, ItemTagsProvider.Builder<Item>> tagBuilder) {

        for (RegistryObject<Block> entry : modBlocks) {
            Block block = entry.get();
            if (!(block instanceof ICropCompat)) continue;

            ICropCompat crop = (ICropCompat) block;
            String name = crop.getCropName();
            Item produce = crop.getProduceItem().asItem();
            Item seed = crop.getSeedItem() != null ? crop.getSeedItem().asItem() : produce;

            // Universal Crop & Seed Tags
            tagBuilder.apply(CropCompatTags.FORGE_CROPS).add(produce);
            tagBuilder.apply(CropCompatTags.forgeCropSubTag(name)).add(produce);
            tagBuilder.apply(CropCompatTags.FORGE_SEEDS).add(seed);
            tagBuilder.apply(CropCompatTags.forgeSeedSubTag(name)).add(seed);

            // Serene Seasons Seed Item Tags (enables season tooltips on seeds)
            for (ICropCompat.Season season : crop.getFertileSeasons()) {
                switch (season) {
                    case SPRING: tagBuilder.apply(CropCompatTags.SS_SPRING_CROPS_ITEM).add(seed); break;
                    case SUMMER: tagBuilder.apply(CropCompatTags.SS_SUMMER_CROPS_ITEM).add(seed); break;
                    case AUTUMN: tagBuilder.apply(CropCompatTags.SS_AUTUMN_CROPS_ITEM).add(seed); break;
                    case WINTER: tagBuilder.apply(CropCompatTags.SS_WINTER_CROPS_ITEM).add(seed); break;
                }
            }

            if (crop.isUnbreakableInfertile()) {
                tagBuilder.apply(CropCompatTags.SS_UNBREAKABLE_ITEM).add(seed);
            }

            // Category-Specific Food/Ingredient Tags
            switch (crop.getCropCategory()) {
                case GRAIN:
                    tagBuilder.apply(CropCompatTags.FORGE_GRAIN).add(produce);
                    tagBuilder.apply(CropCompatTags.forgeCategorySubTag("grain", name)).add(produce);
                    break;
                case VEGETABLE:
                case ROOT:
                    tagBuilder.apply(CropCompatTags.FORGE_VEGETABLES).add(produce);
                    tagBuilder.apply(CropCompatTags.forgeCategorySubTag("vegetables", name)).add(produce);
                    tagBuilder.apply(CropCompatTags.FORGE_SALAD_INGREDIENTS).add(produce);
                    tagBuilder.apply(CropCompatTags.FD_CABBAGE_ROLL_INGREDIENTS).add(produce);
                    break;
                case FRUIT:
                    tagBuilder.apply(CropCompatTags.FORGE_FRUITS).add(produce);
                    tagBuilder.apply(CropCompatTags.forgeCategorySubTag("fruits", name)).add(produce);
                    break;
                case BERRY:
                    tagBuilder.apply(CropCompatTags.FORGE_FRUITS).add(produce);
                    tagBuilder.apply(CropCompatTags.FORGE_BERRIES).add(produce);
                    tagBuilder.apply(CropCompatTags.forgeCategorySubTag("berries", name)).add(produce);
                    break;
                case HERB:
                    tagBuilder.apply(CropCompatTags.FORGE_HERBS).add(produce);
                    tagBuilder.apply(CropCompatTags.FORGE_SALAD_INGREDIENTS).add(produce);
                    break;
                case MUSHROOM:
                    tagBuilder.apply(CropCompatTags.FORGE_MUSHROOMS).add(produce);
                    tagBuilder.apply(CropCompatTags.FD_CABBAGE_ROLL_INGREDIENTS).add(produce);
                    break;
            }
        }
    }
}