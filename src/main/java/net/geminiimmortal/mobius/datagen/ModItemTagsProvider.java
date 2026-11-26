package net.geminiimmortal.mobius.datagen;

import net.geminiimmortal.mobius.api.crop.CropTagGeneratorHelper;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(
            DataGenerator generator,
            BlockTagsProvider blockTagsProvider,
            String modId,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        CropTagGeneratorHelper.generateItemTags(ModBlocks.BLOCKS.getEntries(), this::tag);
    }

    @Override
    public String getName() {
        return "Mobius Item Tags";
    }
}
