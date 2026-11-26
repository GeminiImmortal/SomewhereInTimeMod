package net.geminiimmortal.mobius.api.crop;

import net.minecraft.util.IItemProvider;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;

public interface ICropCompat {

    enum CropCategory {
        GRAIN,      // forge:grain, forge:crops/wheat, etc.
        VEGETABLE,  // forge:vegetables
        FRUIT,      // forge:fruits
        BERRY,      // forge:berries, forge:fruits
        HERB,       // forge:herbs, forge:crops
        ROOT,       // forge:vegetables, forge:rootvegetables
        MUSHROOM    // forge:mushrooms
    }

    enum Season {
        SPRING,
        SUMMER,
        AUTUMN,
        WINTER
    }

    /**
     * The lowercase registry path name used for sub-tags (e.g., "tomato" -> forge:crops/tomato).
     */
    String getCropName();

    /**
     * Broad food/ingredient category for recipe compatibility (Farmer's Delight, Pam's, Create).
     */
    CropCategory getCropCategory();

    /**
     * The harvested produce item.
     */
    IItemProvider getProduceItem();

    /**
     * The seed item planted to grow this crop (return null if the produce itself is planted).
     */
    @Nullable
    default IItemProvider getSeedItem() {
        return null;
    }

    /**
     * Whether bees can pollinate and accelerate this crop (#minecraft:bee_growables).
     */
    default boolean isBeeGrowable() {
        return true;
    }

    /**
     * Whether Farmer's Delight / Create / Botany Pots can compost or mulch this crop.
     */
    default boolean isCompostable() {
        return true;
    }

    /**
     * Seasons in which this crop is fertile for Serene Seasons.
     * Return an empty set to leave it untagged (grows year-round).
     */
    default Set<Season> getFertileSeasons() {
        return EnumSet.of(Season.SPRING, Season.SUMMER, Season.AUTUMN);
    }

    /**
     * If true, registers the crop block and seed in sereneseasons:unbreakable_infertile_crops
     * so perennials/bushes go dormant instead of breaking out of season.
     */
    default boolean isUnbreakableInfertile() {
        return false;
    }
}
