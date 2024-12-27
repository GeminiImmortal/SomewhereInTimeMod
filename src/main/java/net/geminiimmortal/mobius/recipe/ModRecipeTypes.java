package net.geminiimmortal.mobius.recipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.geminiimmortal.mobius.MobiusMod;

public class ModRecipeTypes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MobiusMod.MOD_ID);

    public static final RegistryObject<SoulForgeRecipe.Serializer> SOUL_FORGE_SERIALIZER
            = RECIPE_SERIALIZER.register("soul_forge", SoulForgeRecipe.Serializer::new);

    public static final RegistryObject<AstralConduitRecipe.Serializer> ASTRAL_CONDUIT_SERIALIZER
            = RECIPE_SERIALIZER.register("astral_conduit", AstralConduitRecipe.Serializer::new);

    public static IRecipeType<SoulForgeRecipe> SOUL_FORGE_RECIPE
            = new SoulForgeRecipe.SoulForgeRecipeType();

    public static IRecipeType<AstralConduitRecipe> ASTRAL_CONDUIT_RECIPE
            = new AstralConduitRecipe.AstralConduitRecipeType();

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, SoulForgeRecipe.TYPE_ID, SOUL_FORGE_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, AstralConduitRecipe.TYPE_ID, ASTRAL_CONDUIT_RECIPE);
    }
}
