package net.geminiimmortal.mobius.world.worldgen.feature;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.Lazy;

public enum BloodstoneGemOreType {

    BLOODSTONE_GEM_ORE_TYPE(Lazy.of(ModBlocks.BLOODSTONE_GEM_ORE), 8, 30, 110);

    private final Lazy<Block> block;
    private final int maxVeinSize;
    private final int minHeight;
    private final int maxHeight;

    BloodstoneGemOreType(Lazy<Block> block, int maxVeinSize, int minHeight, int maxHeight) {
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

    public static BloodstoneGemOreType get(Block block) {
        for (BloodstoneGemOreType ore : values()) {
            if(block == ore.block) {
                return ore;
            }
        }
        return null;
    }
}
