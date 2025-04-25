package net.geminiimmortal.mobius.world.worldgen.structure.processor;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.world.worldgen.structure.structures.MolvanSettlementA;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModProcessors {
    public static final IStructureProcessorType<WardOrientationFixProcessor> WARD_ORIENTATION_FIX =
            Registry.register(
                    Registry.STRUCTURE_PROCESSOR,
                    new ResourceLocation(MobiusMod.MOD_ID, "ward_orientation_fix"),
                    () -> WardOrientationFixProcessor.CODEC
            );
}




