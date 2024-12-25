package net.geminiimmortal.mobius.world.worldgen.features;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.Lazy;

public enum HematiteIronOreType {

    HEMATITE_IRON_ORE_TYPE(Lazy.of(ModBlocks.HEMATITE_IRON_ORE), 16, 15, 50);

    private final Lazy<Block> block;
    private final int maxVeinSize;
    private final int minHeight;
    private final int maxHeight;

    HematiteIronOreType(Lazy<Block> block, int maxVeinSize, int minHeight, int maxHeight) {
        this.block = block;
        this.maxVeinSize = maxVeinSize;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    public Lazy<Block> getBlock() {
        return block;
    }

    public int getMaxVeinSize() {
        return maxVeinSize;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public static HematiteIronOreType get(Block block) {
        for (HematiteIronOreType ore : values()) {
            if(block == ore.block) {
                return ore;
            }
        }
        return null;
    }
}
