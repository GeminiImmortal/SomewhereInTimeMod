package net.geminiimmortal.mobius.block;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.custom.*;
import net.geminiimmortal.mobius.block.custom.boss_blocks.GovernorBossBlock;
import net.geminiimmortal.mobius.block.custom.boss_blocks.GovernorBossExitBlock;
import net.geminiimmortal.mobius.block.custom.crop.ManaWartCrop;
import net.geminiimmortal.mobius.block.custom.flora.Gloomcap;
import net.geminiimmortal.mobius.block.custom.flora.StandingGloomcap;
import net.geminiimmortal.mobius.block.custom.flora.WildManaWart;
import net.geminiimmortal.mobius.block.custom.trees.GloamthornTree;
import net.geminiimmortal.mobius.block.custom.trees.ManawoodTree;
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

    public static final RegistryObject<Block> AURORA_DIRT = registerBlock("aurora_dirt",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.DIRT)));

    public static final RegistryObject<Block> AURORA_GRASS_BLOCK = registerBlock("aurora_grass_block",
            () -> new AuroraGrassBlock(AbstractBlock.Properties.copy(Blocks.GRASS_BLOCK)));

    public static final RegistryObject<Block> HEMATITE = registerBlock("hematite",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> HEMATITE_SLAB = registerBlock("hematite_slab",
            () -> new SlabBlock(AbstractBlock.Properties.copy(Blocks.STONE_SLAB)));

    public static final RegistryObject<Block> HEMATITE_STAIRS = registerBlock("hematite_stairs",
            () -> new StairsBlock(HEMATITE.get().defaultBlockState(), AbstractBlock.Properties.copy(Blocks.STONE_STAIRS)));

    public static final RegistryObject<Block> HEMATITE_WALL = registerBlock("hematite_wall",
            () -> new WallBlock(AbstractBlock.Properties.copy(Blocks.COBBLESTONE_WALL)));

    public static final RegistryObject<Block> POLISHED_HEMATITE = registerBlock("polished_hematite",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> POLISHED_HEMATITE_SLAB = registerBlock("polished_hematite_slab",
            () -> new SlabBlock(AbstractBlock.Properties.copy(Blocks.STONE_SLAB)));

    public static final RegistryObject<Block> POLISHED_HEMATITE_STAIRS = registerBlock("polished_hematite_stairs",
            () -> new StairsBlock(POLISHED_HEMATITE.get().defaultBlockState(), AbstractBlock.Properties.copy(Blocks.STONE_STAIRS)));

    public static final RegistryObject<Block> BONE_LEAVES = registerBlock("bone_leaves",
            () -> new LeavesBlock(AbstractBlock.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> MARROWOOD_DOOR = registerBlock("marrowood_door",
            () -> new DoorBlock(AbstractBlock.Properties.copy(Blocks.OAK_DOOR)));

    public static final RegistryObject<Block> GOVERNOR_BOSS_BLOCK = registerBlock("governor_boss_block",
            () -> new GovernorBossBlock(AbstractBlock.Properties.copy(Blocks.BEDROCK)));

    public static final RegistryObject<Block> GOVERNOR_BOSS_EXIT_BLOCK = registerBlock("governor_boss_exit_block",
            () -> new GovernorBossExitBlock(AbstractBlock.Properties.copy(Blocks.BEDROCK)));

    public static final RegistryObject<Block> DUNGEON_BLOCK = registerBlock("dungeon_block",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.BEDROCK)));

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
            () -> new SoulPodzol(AbstractBlock.Properties.copy(Blocks.GRASS_BLOCK)));

    public static final RegistryObject<Block> STRIPPED_MARROWOOD_LOG = registerBlock("stripped_marrowood_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.STRIPPED_OAK_LOG)));

    public static final RegistryObject<Block> STRIPPED_MANAWOOD_LOG = registerBlock("stripped_manawood_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.STRIPPED_OAK_LOG)));

    public static final RegistryObject<Block> STRIPPED_GLOAMTHORN_LOG = registerBlock("stripped_gloamthorn_log",
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

    public static final RegistryObject<Block> MANAWOOD_WOOD = registerBlock("manawood_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.OAK_WOOD)));

    public static final RegistryObject<Block> GLOAMTHORN_WOOD = registerBlock("gloamthorn_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.OAK_WOOD)));

    public static final RegistryObject<Block> STRIPPED_MARROWOOD_WOOD = registerBlock("stripped_marrowood_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));

    public static final RegistryObject<Block> STRIPPED_MANAWOOD_WOOD = registerBlock("stripped_manawood_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));

    public static final RegistryObject<Block> STRIPPED_GLOAMTHORN_WOOD = registerBlock("stripped_gloamthorn_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));

    public static final RegistryObject<Block> MARROWOOD_SIGN = registerBlock("marrowood_sign",
            () -> new ModStandingSignBlock(AbstractBlock.Properties.of(Material.WOOD), ModWoodTypes.MARROWOOD));

    public static final RegistryObject<Block> MAGIC_KEYSTONE = registerBlock("magic_keystone",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.OBSIDIAN)));

    public static final RegistryObject<Block> HEMATITE_IRON_ORE = registerBlock("hematite_iron_ore",
            () -> new OreBlock(AbstractBlock.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> MARROWOOD_WALL_SIGN = registerBlock("marrowood_wall_sign",
            () -> new ModWallSignBlock(AbstractBlock.Properties.of(Material.WOOD), ModWoodTypes.MARROWOOD));

    public static final RegistryObject<Block> MANAWOOD_SIGN = registerBlock("manawood_sign",
            () -> new ModStandingSignBlock(AbstractBlock.Properties.of(Material.WOOD), ModWoodTypes.MANAWOOD));

    public static final RegistryObject<Block> GLOAMTHORN_WALL_SIGN = registerBlock("gloamthorn_wall_sign",
            () -> new ModWallSignBlock(AbstractBlock.Properties.of(Material.WOOD), ModWoodTypes.GLOAMTHORN));

    public static final RegistryObject<Block> GLOAMTHORN_SIGN = registerBlock("gloamthorn_sign",
            () -> new ModStandingSignBlock(AbstractBlock.Properties.of(Material.WOOD), ModWoodTypes.GLOAMTHORN));

    public static final RegistryObject<Block> MANAWOOD_WALL_SIGN = registerBlock("manawood_wall_sign",
            () -> new ModWallSignBlock(AbstractBlock.Properties.of(Material.WOOD), ModWoodTypes.MANAWOOD));

    public static final RegistryObject<Block> NICKEL_ORE = registerBlock("nickel_ore",
            () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).strength(30F, 1200F).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> SOUL_FORGE = registerBlock("soul_forge",
            () -> new SoulForgeBlock(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(3).strength(30F, 1200F).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> ASTRAL_CONDUIT = registerBlock("astral_conduit",
            () -> new AstralConduitBlock(AbstractBlock.Properties.copy(Blocks.BEDROCK).noOcclusion()));

    public static final RegistryObject<Block> LIVING_MANAWOOD_LOG = registerBlock("living_manawood_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.OAK_LOG)));

    public static final RegistryObject<Block> MANAWOOD_LOG = registerBlock("manawood_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.OAK_LOG)));

    public static final RegistryObject<Block> LIVING_MANAWOOD_LEAVES = registerBlock("living_manawood_leaves",
            () -> new LeavesBlock(AbstractBlock.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> GLOAMTHORN_LEAVES = registerBlock("gloamthorn_leaves",
            () -> new LeavesBlock(AbstractBlock.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> GLOAMTHORN_SAPLING = registerBlock("gloamthorn_sapling",
            () -> new SaplingBlock(new GloamthornTree(), AbstractBlock.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> MANAWOOD_LEAVES = registerBlock("manawood_leaves",
            () -> new LeavesBlock(AbstractBlock.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> MANAWOOD_PLANKS = registerBlock("manawood_planks",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> MANAWOOD_FENCE = registerBlock("manawood_fence",
            () -> new FenceBlock(AbstractBlock.Properties.copy(Blocks.OAK_FENCE)));

    public static final RegistryObject<Block> MANAWOOD_FENCE_GATE = registerBlock("manawood_fence_gate",
            () -> new FenceGateBlock(AbstractBlock.Properties.copy(Blocks.OAK_FENCE_GATE)));

    public static final RegistryObject<Block> MANAWOOD_SLAB = registerBlock("manawood_slab",
            () -> new SlabBlock(AbstractBlock.Properties.copy(Blocks.OAK_SLAB)));

    public static final RegistryObject<Block> MANAWOOD_STAIRS = registerBlock("manawood_stairs",
            () -> new StairsBlock(MANAWOOD_PLANKS.get().defaultBlockState(), AbstractBlock.Properties.copy(Blocks.OAK_STAIRS)));

    public static final RegistryObject<Block> MANAWOOD_DOOR = registerBlock("manawood_door",
            () -> new DoorBlock(AbstractBlock.Properties.copy(Blocks.OAK_DOOR)));

    public static final RegistryObject<Block> ESSENCE_CHANNELER = registerBlock("essence_channeler",
            () -> new EssenceChannelerBlock(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final RegistryObject<Block> MANAWOOD_SAPLING = registerBlock("manawood_sapling",
            () -> new SaplingBlock(new ManawoodTree(), AbstractBlock.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> MANAWOOD_BUTTON = registerBlock("manawood_button",
            () -> new WoodButtonBlock(AbstractBlock.Properties.copy(Blocks.OAK_BUTTON)));

    public static final RegistryObject<Block> MANAWOOD_PRESSURE_PLATE = registerBlock("manawood_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,AbstractBlock.Properties.copy(Blocks.OAK_PRESSURE_PLATE)));

    public static final RegistryObject<Block> MANAWOOD_TRAPDOOR = registerBlock("manawood_trapdoor",
            () -> new TrapDoorBlock(AbstractBlock.Properties.copy(Blocks.OAK_TRAPDOOR)));

    public static final RegistryObject<Block> BLOCK_OF_NICKEL = registerBlock("block_of_nickel",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> GLOAMTHORN_LOG = registerBlock("gloamthorn_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.copy(Blocks.OAK_LOG)));

    public static final RegistryObject<Block> GLOAMTHORN_PLANKS = registerBlock("gloamthorn_planks",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> GLOAMTHORN_SLAB = registerBlock("gloamthorn_slab",
            () -> new SlabBlock(AbstractBlock.Properties.copy(Blocks.OAK_SLAB)));

    public static final RegistryObject<Block> GLOAMTHORN_STAIRS = registerBlock("gloamthorn_stairs",
            () -> new StairsBlock(GLOAMTHORN_PLANKS.get().defaultBlockState(), AbstractBlock.Properties.copy(Blocks.OAK_STAIRS)));

    public static final RegistryObject<Block> GLOAMTHORN_FENCE = registerBlock("gloamthorn_fence",
            () -> new FenceBlock(AbstractBlock.Properties.copy(Blocks.OAK_FENCE)));

    public static final RegistryObject<Block> GLOAMTHORN_FENCE_GATE = registerBlock("gloamthorn_fence_gate",
            () -> new FenceGateBlock(AbstractBlock.Properties.copy(Blocks.OAK_FENCE_GATE)));

    public static final RegistryObject<Block> GLOAMTHORN_BUTTON = registerBlock("gloamthorn_button",
            () -> new WoodButtonBlock(AbstractBlock.Properties.copy(Blocks.OAK_BUTTON)));

    public static final RegistryObject<Block> GLOAMTHORN_PRESSURE_PLATE = registerBlock("gloamthorn_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,AbstractBlock.Properties.copy(Blocks.OAK_PRESSURE_PLATE)));

    public static final RegistryObject<Block> GLOAMTHORN_DOOR = registerBlock("gloamthorn_door",
            () -> new DoorBlock(AbstractBlock.Properties.copy(Blocks.OAK_DOOR)));

    public static final RegistryObject<Block> GLOAMTHORN_TRAPDOOR = registerBlock("gloamthorn_trapdoor",
            () -> new TrapDoorBlock(AbstractBlock.Properties.copy(Blocks.OAK_TRAPDOOR)));

    public static final RegistryObject<Block> STANDING_GLOOMCAP = registerBlock("standing_gloomcap",
            () -> new StandingGloomcap(AbstractBlock.Properties.copy(Blocks.BROWN_MUSHROOM).lightLevel(state -> 15).instabreak().noOcclusion().randomTicks()));

    public static final RegistryObject<Block> GIANT_GLOOMCAP_CAP = registerBlock("giant_gloomcap_cap",
            () -> new Block(AbstractBlock.Properties.of(Material.PLANT).lightLevel(state -> 15).strength(0.5f).noOcclusion()));

    public static final RegistryObject<Block> GIANT_GLOOMCAP_STEM = registerBlock("giant_gloomcap_stem",
            () -> new Block(AbstractBlock.Properties.of(Material.WOOD).strength(1.0f)));

    public static final RegistryObject<Block> MANA_WART = BLOCKS.register("mana_wart",
            () -> new ManaWartCrop(AbstractBlock.Properties.copy(Blocks.WHEAT).lightLevel(state -> 12).instabreak().noOcclusion()));

    public static final RegistryObject<Block> WILD_MANA_WART = registerBlock("wild_mana_wart",
            () -> new WildManaWart(AbstractBlock.Properties.copy(Blocks.BROWN_MUSHROOM).lightLevel(state -> 12).instabreak().noOcclusion()));

    public static final RegistryObject<Block> GLOOMCAP = registerBlock("gloomcap",
            () -> new Gloomcap(AbstractBlock.Properties.copy(Blocks.BROWN_MUSHROOM).lightLevel(state -> 12).instabreak().noOcclusion()));

    public static final RegistryObject<Block> BLOODSTONE = registerBlock("bloodstone",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> BLOODSTONE_GEM_ORE = registerBlock("bloodstone_gem_ore",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.STONE).lightLevel(state -> 7)));

    public static final RegistryObject<Block> CELESTIAL_PILLAR = registerBlock("celestial_pillar",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.STONE).noOcclusion().strength(2000f, 2000f).noDrops()));

    public static final RegistryObject<Block> CELESTIAL_TILE = registerBlock("celestial_tile",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.STONE).strength(2000f, 2000f).noDrops()));

    public static final RegistryObject<Block> CELESTIAL_TILE_SLAB = registerBlock("celestial_tile_slab",
            () -> new SlabBlock(AbstractBlock.Properties.copy(Blocks.STONE_SLAB).strength(2000f, 2000f).noDrops()));

    public static final RegistryObject<Block> CELESTIAL_STAIRS = registerBlock("celestial_stairs",
            () -> new StairsBlock(CELESTIAL_TILE.get().defaultBlockState(), AbstractBlock.Properties.copy(Blocks.STONE_STAIRS).strength(2000f, 2000f).noDrops()));

    public static final RegistryObject<Block> CELESTIAL_WALL = registerBlock("celestial_wall",
            () -> new WallBlock(AbstractBlock.Properties.copy(Blocks.COBBLESTONE_WALL).strength(2000f, 2000f).noDrops()));

    public static final RegistryObject<Block> OBSECFUTORIA = registerBlock("obsecfutoria",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> HUECO_SAND = registerBlock("hueco_sand",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.SAND)));

    public static final RegistryObject<Block> LATENT_MANA_COLLECTOR = registerBlock("latent_mana_collector",
            () -> new LatentManaCollector(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(3).strength(30F, 1200F).requiresCorrectToolForDrops().noOcclusion()));

    public static final RegistryObject<Block> CRYSTAL_KEEP_BLOCK = registerBlock("crystal_keep_block",
            () -> new Block(AbstractBlock.Properties.copy(Blocks.BEDROCK).lightLevel(state -> 12).sound(SoundType.GLASS)));

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        return toReturn;
    }



    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
