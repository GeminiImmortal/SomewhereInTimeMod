package net.geminiimmortal.mobius.block;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.custom.MobiusPortalBlock;
import net.geminiimmortal.mobius.block.custom.ModStandingSignBlock;
import net.geminiimmortal.mobius.block.custom.ModWallSignBlock;
import net.geminiimmortal.mobius.block.custom.SoulForgeBlock;
import net.geminiimmortal.mobius.block.custom.trees.MarrowoodTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MobiusMod.MOD_ID);

    public static final RegistryObject<Block> MOBIUS_PORTAL = BLOCKS.register("mobius_portal", MobiusPortalBlock::new);

    public static final RegistryObject<Block> CANDYCANE_BRICKS = registerBlock("candycane_bricks",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.STONE)
                    .harvestLevel(2).harvestTool(ToolType.PICKAXE).strength(8f)));

    public static final RegistryObject<Block> BONE_LEAVES = registerBlock("bone_leaves",
            () -> new LeavesBlock(AbstractBlock.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> MARROWOOD_DOOR = registerBlock("marrowood_door",
            () -> new DoorBlock(AbstractBlock.Properties.copy(Blocks.OAK_DOOR)));

    public static final RegistryObject<Block> MARROWOOD_FENCE = registerBlock("marrowood_fence",
            () -> new FenceBlock(AbstractBlock.Properties.copy(Blocks.OAK_FENCE)));

    public static final RegistryObject<Block> MARROWOOD_FENCE_GATE = registerBlock("marrowood_fence_gate",
            () -> new FenceGateBlock(AbstractBlock.Properties.copy(Blocks.OAK_FENCE_GATE)));

    public static final RegistryObject<Block> MARROWOOD_LOG = registerBlock("marrowood_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.OAK_LOG)));

    public static final RegistryObject<Block> MARROWOOD_PLANKS = registerBlock("marrowood_planks",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> MARROWOOD_SLAB = registerBlock("marrowood_slab",
            () -> new SlabBlock(AbstractBlock.Properties.copy(Blocks.OAK_SLAB)));

    public static final RegistryObject<Block> MARROWOOD_STAIRS = registerBlock("marrowood_stairs",
            () -> new StairsBlock(MARROWOOD_PLANKS.get().defaultBlockState(), AbstractBlock.Properties.copy(Blocks.OAK_STAIRS)));

    public static final RegistryObject<Block> MARROWOOD_TRAPDOOR = registerBlock("marrowood_trapdoor",
            () -> new TrapDoorBlock(AbstractBlock.Properties.copy(Blocks.OAK_TRAPDOOR)));

    public static final RegistryObject<Block> SOUL_PODZOL = registerBlock("soul_podzol",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.DIRT)));

    public static final RegistryObject<Block> STRIPPED_MARROWOOD_LOG = registerBlock("stripped_marrowood_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.STRIPPED_OAK_LOG)));

    public static final RegistryObject<Block> WASP_PAPER_BLOCK = registerBlock("wasp_paper_block",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> HUNTING_TABLE = registerBlock("hunting_table",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.FLETCHING_TABLE)));

    public static final RegistryObject<Block> MARROWOOD_SAPLING = registerBlock("marrowood_sapling",
            () -> new SaplingBlock(new MarrowoodTree(), AbstractBlock.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> MARROWOOD_BUTTON = registerBlock("marrowood_button",
            () -> new WoodButtonBlock(AbstractBlock.Properties.copy(Blocks.OAK_BUTTON)));

    public static final RegistryObject<Block> MARROWOOD_PRESSURE_PLATE = registerBlock("marrowood_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,AbstractBlock.Properties.copy(Blocks.OAK_PRESSURE_PLATE)));

    public static final RegistryObject<Block> MARROWOOD_WOOD = registerBlock("marrowood_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.OAK_WOOD)));

    public static final RegistryObject<Block> STRIPPED_MARROWOOD_WOOD = registerBlock("stripped_marrowood_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));

    public static final RegistryObject<Block> MARROWOOD_SIGN = registerBlock("marrowood_sign",
            () -> new ModStandingSignBlock(AbstractBlock.Properties.of(Material.WOOD), ModWoodTypes.MARROWOOD));

    public static final RegistryObject<Block> MARROWOOD_WALL_SIGN = registerBlock("marrowood_wall_sign",
            () -> new ModWallSignBlock(AbstractBlock.Properties.of(Material.WOOD), ModWoodTypes.MARROWOOD));

    public static final RegistryObject<Block> SOUL_FORGE = registerBlock("soul_forge",
            () -> new SoulForgeBlock(AbstractBlock.Properties.of(Material.STONE)));

    public static final RegistryObject<Block> LIVING_MANAWOOD_LOG = registerBlock("living_manawood_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.OAK_LOG)));

    public static final RegistryObject<Block> MANAWOOD_LOG = registerBlock("manawood_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.OAK_LOG)));

    public static final RegistryObject<Block> LIVING_MANAWOOD_LEAVES = registerBlock("living_manawood_leaves",
            () -> new LeavesBlock(AbstractBlock.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> MANAWOOD_LEAVES = registerBlock("manawood_leaves",
            () -> new LeavesBlock(AbstractBlock.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> MANAWOOD_PLANKS = registerBlock("manawood_planks",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> HEMATITE = registerBlock("hematite",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.STONE)));

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        return toReturn;
    }



    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
