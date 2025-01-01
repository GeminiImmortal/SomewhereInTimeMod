package net.geminiimmortal.mobius.integration;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.recipe.AstralConduitRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AstralConduitRecipeCategory implements IRecipeCategory<AstralConduitRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(MobiusMod.MOD_ID, "astral_conduit");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(MobiusMod.MOD_ID, "textures/gui/astral_conduit_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public AstralConduitRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.ASTRAL_CONDUIT.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends AstralConduitRecipe> getRecipeClass() {
        return AstralConduitRecipe.class;
    }

    @Override
    public String getTitle() {
        return ModBlocks.ASTRAL_CONDUIT.get().getName().getString();
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
    public void setIngredients(AstralConduitRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AstralConduitRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 79, 10);
        recipeLayout.getItemStacks().init(1, true, 105, 19);
        recipeLayout.getItemStacks().init(2, true, 53, 19);

        recipeLayout.getItemStacks().init(3, false, 79, 58);
        recipeLayout.getItemStacks().set(ingredients);
    }


}
