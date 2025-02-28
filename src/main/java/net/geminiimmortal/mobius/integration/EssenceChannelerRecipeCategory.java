package net.geminiimmortal.mobius.integration;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.recipe.EssenceChannelerRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EssenceChannelerRecipeCategory implements IRecipeCategory<EssenceChannelerRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(MobiusMod.MOD_ID, "essence_channeler");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(MobiusMod.MOD_ID, "textures/gui/essence_channeler_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public EssenceChannelerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.ESSENCE_CHANNELER.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends EssenceChannelerRecipe> getRecipeClass() {
        return EssenceChannelerRecipe.class;
    }

    @Override
    public String getTitle() {
        return ModBlocks.ESSENCE_CHANNELER.get().getName().getString();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(EssenceChannelerRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, EssenceChannelerRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 47, 13);
        recipeLayout.getItemStacks().init(1, true, 40, 34);
        recipeLayout.getItemStacks().init(2, true, 47, 56);

        recipeLayout.getItemStacks().init(3, false, 123, 34);
        recipeLayout.getItemStacks().set(ingredients);
    }


}
