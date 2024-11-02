package net.geminiimmortal.mobius.world.worldgen.features;

import net.geminiimmortal.mobius.world.worldgen.biome.ModBiomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ModMountainGeneration {
    public static void generateMountains(final BiomeLoadingEvent event) {
        ResourceLocation valid = ModBiomes.DRACONIC_FORELANDS.getId();

        if(Objects.equals(event.getName(), valid)) {
            List<Supplier<ConfiguredFeature<?, ?>>> base =
                    event.getGeneration().getFeatures(GenerationStage.Decoration.TOP_LAYER_MODIFICATION);

            base.add(() -> ModConfiguredFeatures.DRACONIC_FORELANDS_MOUNTAIN
                    .decorated(Features.Placements.HEIGHTMAP)
                    .decorated(Placement.COUNT_MULTILAYER.configured(
                            new FeatureSpreadConfig(2))));
            System.out.println("Created a mountain...");
        }
    }
}
