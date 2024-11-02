package net.geminiimmortal.mobius.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class SoulForgeRecipe implements ISoulForgeRecipe {


    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public SoulForgeRecipe(ResourceLocation id, ItemStack output,
                                    NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        // Checks for correct focus (Glass Pane)
        if(recipeItems.get(0).test(inv.getItem(0))) {
            return recipeItems.get(0).test(inv.getItem(0));
        }

        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        return output;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }


    public ItemStack icon() {
        return new ItemStack(ModBlocks.SOUL_FORGE.get());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.SOUL_FORGE_SERIALIZER.get();
    }

    public static class SoulForgeRecipeType implements IRecipeType<SoulForgeRecipe> {
        @Override
        public String toString() {
            return SoulForgeRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<SoulForgeRecipe> {

        @Override
        public SoulForgeRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "output"));

            JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new SoulForgeRecipe(recipeId, output,
                    inputs);
        }

        @Nullable
        @Override
        public SoulForgeRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buffer));
            }

            ItemStack output = buffer.readItem();
            return new SoulForgeRecipe(recipeId, output,
                    inputs);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, SoulForgeRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buffer);
            }
            buffer.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
