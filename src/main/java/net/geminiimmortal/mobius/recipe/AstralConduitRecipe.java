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

public class AstralConduitRecipe implements IAstralConduitRecipe {


    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public AstralConduitRecipe(ResourceLocation id, ItemStack output,
                               NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return recipeItems.get(0).test(inv.getItem(0)) && recipeItems.get(1).test(inv.getItem(1)) && recipeItems.get(2).test(inv.getItem(2));
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
        return new ItemStack(ModBlocks.ASTRAL_CONDUIT.get());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.ASTRAL_CONDUIT_SERIALIZER.get();
    }

    public static class AstralConduitRecipeType implements IRecipeType<AstralConduitRecipe> {
        @Override
        public String toString() {
            return AstralConduitRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<AstralConduitRecipe> {

        @Override
        public AstralConduitRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "output"));

            JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(3, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new AstralConduitRecipe(recipeId, output,
                    inputs);
        }

        @Nullable
        @Override
        public AstralConduitRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(3, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buffer));
            }

            ItemStack output = buffer.readItem();
            return new AstralConduitRecipe(recipeId, output,
                    inputs);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, AstralConduitRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buffer);
            }
            buffer.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
