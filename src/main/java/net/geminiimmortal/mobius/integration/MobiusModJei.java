package net.geminiimmortal.mobius.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.recipe.AstralConduitRecipe;
import net.geminiimmortal.mobius.recipe.ModRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class MobiusModJei implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MobiusMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new AstralConduitRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        registration.addRecipes(rm.getAllRecipesFor(ModRecipeTypes.ASTRAL_CONDUIT_RECIPE).stream()
                        .filter(r -> r instanceof AstralConduitRecipe).collect(Collectors.toList()),
                AstralConduitRecipeCategory.UID);
    }
}
