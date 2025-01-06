package net.geminiimmortal.mobius.world.worldgen.feature.placement;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModFeaturePlacement {
    public static final DeferredRegister<Placement<?>> DECORATORS = DeferredRegister.create(ForgeRegistries.DECORATORS, MobiusMod.MOD_ID);

    public static final RegistryObject<DenserTreesPlacement> DENSER_TREES_PLACEMENT_REGISTRY_OBJECT =
            registerDeco("denser_trees_placement", () -> new DenserTreesPlacement(FeatureSpreadConfig.CODEC));


    private static <T extends Placement<?>> RegistryObject<T> registerDeco(final String name, final Supplier<T> sup) {
        return DECORATORS.register(name, sup);
    }
}
