package net.geminiimmortal.mobius.fluid;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFluids {
    public static final ResourceLocation BOG_WATER_STILL_TEXTURE = new ResourceLocation("mobius:fluid/bog_water_still");
    public static final ResourceLocation BOG_WATER_FLOWING_TEXTURE = new ResourceLocation("mobius:fluid/flowing_bog_water");

    public static final ResourceLocation ECTOPLASM_STILL_TEXTURE = new ResourceLocation("mobius:fluid/ectoplasm_still");
    public static final ResourceLocation ECTOPLASM_FLOWING_TEXTURE = new ResourceLocation("mobius:fluid/flowing_ectoplasm");

    public static final ResourceLocation WATER_OVERLAY_RL = new ResourceLocation("block/water_overlay");


    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MobiusMod.MOD_ID);

    //Bog water stuff
    public static final RegistryObject<FlowingFluid> BOG_WATER_FLUID =
            FLUIDS.register("bog_water_fluid", () -> new ForgeFlowingFluid.Source(ModFluids.BOG_WATER_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_BOG_WATER =
            FLUIDS.register("flowing_bog_water", () -> new ForgeFlowingFluid.Flowing(ModFluids.BOG_WATER_PROPERTIES));

    public static final ForgeFlowingFluid.Properties BOG_WATER_PROPERTIES = new ForgeFlowingFluid.Properties(
            () -> BOG_WATER_FLUID.get(), () -> FLOWING_BOG_WATER.get(), FluidAttributes.builder(BOG_WATER_STILL_TEXTURE, BOG_WATER_FLOWING_TEXTURE)
            .density(30).luminosity(2).viscosity(15).sound(SoundEvents.WATER_AMBIENT).overlay(WATER_OVERLAY_RL)
            ).slopeFindDistance(2).levelDecreasePerBlock(2)
            .block(() -> ModFluids.BOG_WATER_BLOCK.get()).bucket(() -> ModItems.BOG_WATER_BUCKET.get());

    public static final RegistryObject<FlowingFluidBlock> BOG_WATER_BLOCK =
            ModBlocks.BLOCKS.register("bog_water", () -> new FlowingFluidBlock(() -> ModFluids.BOG_WATER_FLUID.get(),
                    AbstractBlock.Properties.of(ModFluidTypes.BOG_WATER_TYPE).noCollission().strength(100f).noDrops()));



    //Ectoplasm stuff
    public static final RegistryObject<FlowingFluid> ECTOPLASM_FLUID =
            FLUIDS.register("ectoplasm_fluid", () -> new ForgeFlowingFluid.Source(ModFluids.ECTOPLASM_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_ECTOPLASM =
            FLUIDS.register("flowing_ectoplasm", () -> new ForgeFlowingFluid.Flowing(ModFluids.ECTOPLASM_PROPERTIES));

    public static final ForgeFlowingFluid.Properties ECTOPLASM_PROPERTIES = new ForgeFlowingFluid.Properties(
            () -> ECTOPLASM_FLUID.get(), () -> FLOWING_ECTOPLASM.get(), FluidAttributes.builder(ECTOPLASM_STILL_TEXTURE, ECTOPLASM_FLOWING_TEXTURE)
            .density(30).luminosity(2).viscosity(15).sound(SoundEvents.LAVA_POP)
    ).slopeFindDistance(2).levelDecreasePerBlock(2)
            .block(() -> ModFluids.ECTOPLASM_BLOCK.get()).bucket(() -> ModItems.ECTOPLASM_BUCKET.get());

    public static final RegistryObject<FlowingFluidBlock> ECTOPLASM_BLOCK =
            ModBlocks.BLOCKS.register("ectoplasm", () -> new FlowingFluidBlock(() -> ModFluids.ECTOPLASM_FLUID.get(),
                    AbstractBlock.Properties.of(ModFluidTypes.ECTOPLASM_TYPE).noCollission().strength(1000f).noDrops()));

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}

