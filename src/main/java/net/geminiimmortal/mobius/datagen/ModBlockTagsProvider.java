package net.geminiimmortal.mobius.datagen;

import net.geminiimmortal.mobius.api.crop.CropTagGeneratorHelper;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        CropTagGeneratorHelper.generateBlockTags(ModBlocks.BLOCKS.getEntries(), this::tag);
    }

    @Override
    public String getName() {
        return "Mobius Block Tags";
    }
}
