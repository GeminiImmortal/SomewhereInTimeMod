package net.geminiimmortal.mobius.util;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {

    public static class Blocks {

        public static final Tags.IOptionalNamedTag<Block> PORTAL_FRAME_BLOCKS =
                createTag("portal_frame_blocks");

        public static final Tags.IOptionalNamedTag<Block> ALLOW_TREES =
                createTag("tree_growth");

        public static final Tags.IOptionalNamedTag<Block> MARROWOOD_LOGS =
                createTag("marrowood_logs");

        public static final Tags.IOptionalNamedTag<Block> MANAWOOD_LOGS =
                createTag("manawood_logs");

        public static final Tags.IOptionalNamedTag<Block> GLOAMTHORN_LOGS =
                createTag("gloamthorn_logs");

        private static Tags.IOptionalNamedTag<Block> createTag(String name) {
            return BlockTags.createOptional(new ResourceLocation(MobiusMod.MOD_ID, name));
        }

        private static Tags.IOptionalNamedTag<Block> createForgeTag(String name) {
            return BlockTags.createOptional(new ResourceLocation("forge", name));
        }


    }

    public static class Items {

        public static final Tags.IOptionalNamedTag<Item> MARROWOOD_LOGS =
                createTag("marrowood_logs");

        public static final Tags.IOptionalNamedTag<Item> MANAWOOD_LOGS =
                createTag("manawood_logs");

        public static final Tags.IOptionalNamedTag<Item> GLOAMTHORN_LOGS =
                createTag("gloamthorn_logs");

        private static Tags.IOptionalNamedTag<Item> createTag(String name) {
            return ItemTags.createOptional(new ResourceLocation(MobiusMod.MOD_ID, name));
        }

        private static Tags.IOptionalNamedTag<Item> createForgeTag(String name) {
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }
    }

    public static final class Fluids {

        public static final ITag<Fluid> ECTOPLASM_FLUID = FluidTags.createOptional(new ResourceLocation(MobiusMod.MOD_ID, "ectoplasm_fluid"));


        private static Tags.IOptionalNamedTag<Fluid> createTag(String name) {
            return FluidTags.createOptional(new ResourceLocation(MobiusMod.MOD_ID, name));
        }
    }
}

