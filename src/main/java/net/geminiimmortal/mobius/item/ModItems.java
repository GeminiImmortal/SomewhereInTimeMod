package net.geminiimmortal.mobius.item;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.block.ModWoodTypes;
import net.geminiimmortal.mobius.item.custom.CandyRing;
import net.geminiimmortal.mobius.item.custom.CustomBoatItem;
import net.geminiimmortal.mobius.item.custom.ManawoodBoatItem;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MobiusMod.MOD_ID);

    //Leaves
    public static final RegistryObject<Item> BONE_LEAVES = ITEMS.register("bone_leaves", () -> new BlockItem(ModBlocks.BONE_LEAVES.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MANAWOOD_LEAVES = ITEMS.register("manawood_leaves", () -> new BlockItem(ModBlocks.MANAWOOD_LEAVES.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> LIVING_MANAWOOD_LEAVES = ITEMS.register("living_manawood_leaves", () -> new BlockItem(ModBlocks.LIVING_MANAWOOD_LEAVES.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Logs
    public static final RegistryObject<Item> MARROWOOD_LOG = ITEMS.register("marrowood_log", () -> new BlockItem(ModBlocks.MARROWOOD_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MANAWOOD_LOG = ITEMS.register("manawood_log", () -> new BlockItem(ModBlocks.MANAWOOD_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> LIVING_MANAWOOD_LOG = ITEMS.register("living_manawood_log", () -> new BlockItem(ModBlocks.LIVING_MANAWOOD_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Stripped Logs
    public static final RegistryObject<Item> STRIPPED_MARROWOOD_LOG = ITEMS.register("stripped_marrowood_log", () -> new BlockItem(ModBlocks.STRIPPED_MARROWOOD_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Bark Blocks
    public static final RegistryObject<Item> MARROWOOD_WOOD = ITEMS.register("marrowood_wood", () -> new BlockItem(ModBlocks.MARROWOOD_WOOD.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Stripped Bark Blocks
    public static final RegistryObject<Item> STRIPPED_MARROWOOD_WOOD = ITEMS.register("stripped_marrowood_wood", () -> new BlockItem(ModBlocks.STRIPPED_MARROWOOD_WOOD.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Planks
    public static final RegistryObject<Item> MARROWOOD_PLANKS = ITEMS.register("marrowood_planks", () -> new BlockItem(ModBlocks.MARROWOOD_PLANKS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MANAWOOD_PLANKS = ITEMS.register("manawood_planks", () -> new BlockItem(ModBlocks.MANAWOOD_PLANKS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Wooden Stairs
    public static final RegistryObject<Item> MARROWOOD_STAIRS = ITEMS.register("marrowood_stairs", () -> new BlockItem(ModBlocks.MARROWOOD_STAIRS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MANAWOOD_STAIRS = ITEMS.register("manawood_stairs", () -> new BlockItem(ModBlocks.MANAWOOD_STAIRS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Wooden Slabs
    public static final RegistryObject<Item> MARROWOOD_SLAB = ITEMS.register("marrowood_slab", () -> new BlockItem(ModBlocks.MARROWOOD_SLAB.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MANAWOOD_SLAB = ITEMS.register("manawood_slab", () -> new BlockItem(ModBlocks.MANAWOOD_SLAB.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Wooden Fences
    public static final RegistryObject<Item> MARROWOOD_FENCE = ITEMS.register("marrowood_fence", () -> new BlockItem(ModBlocks.MARROWOOD_FENCE.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> MANAWOOD_FENCE = ITEMS.register("manawood_fence", () -> new BlockItem(ModBlocks.MANAWOOD_FENCE.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    //Wooden Fence Gates
    public static final RegistryObject<Item> MARROWOOD_FENCE_GATE = ITEMS.register("marrowood_fence_gate", () -> new BlockItem(ModBlocks.MARROWOOD_FENCE_GATE.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> MANAWOOD_FENCE_GATE = ITEMS.register("manawood_fence_gate", () -> new BlockItem(ModBlocks.MANAWOOD_FENCE_GATE.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));

    //Wooden Doors
    public static final RegistryObject<Item> MARROWOOD_DOOR = ITEMS.register("marrowood_door", () -> new BlockItem(ModBlocks.MARROWOOD_DOOR.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> MANAWOOD_DOOR = ITEMS.register("manawood_door", () -> new BlockItem(ModBlocks.MANAWOOD_DOOR.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));

    //Wooden Trapdoors
    public static final RegistryObject<Item> MARROWOOD_TRAPDOOR = ITEMS.register("marrowood_trapdoor", () -> new BlockItem(ModBlocks.MARROWOOD_TRAPDOOR.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> MANAWOOD_TRAPDOOR = ITEMS.register("manawood_trapdoor", () -> new BlockItem(ModBlocks.MANAWOOD_TRAPDOOR.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));

    //Wooden Pressure Plates
    public static final RegistryObject<Item> MARROWOOD_PRESSURE_PLATE = ITEMS.register("marrowood_pressure_plate", () -> new BlockItem(ModBlocks.MARROWOOD_PRESSURE_PLATE.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> MANAWOOD_PRESSURE_PLATE = ITEMS.register("manawood_pressure_plate", () -> new BlockItem(ModBlocks.MANAWOOD_PRESSURE_PLATE.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));

    //Wooden Buttons
    public static final RegistryObject<Item> MARROWOOD_BUTTON = ITEMS.register("marrowood_button", () -> new BlockItem(ModBlocks.MARROWOOD_BUTTON.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> MANAWOOD_BUTTON = ITEMS.register("manawood_button", () -> new BlockItem(ModBlocks.MANAWOOD_BUTTON.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));

    //Signs
    public static final RegistryObject<Item> MARROWOOD_SIGN = ITEMS.register("marrowood_sign", () -> new SignItem(new Item.Properties().tab(ItemGroup.TAB_DECORATIONS).stacksTo(16),ModBlocks.MARROWOOD_SIGN.get(), ModBlocks.MARROWOOD_WALL_SIGN.get()));

    //Saplings
    public static final RegistryObject<Item> MARROWOOD_SAPLING = ITEMS.register("marrowood_sapling", () -> new BlockItem(ModBlocks.MARROWOOD_SAPLING.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> MANAWOOD_SAPLING = ITEMS.register("manawood_sapling", () -> new BlockItem(ModBlocks.MANAWOOD_SAPLING.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    //Boats
    public static final RegistryObject<Item> MARROWOOD_BOAT = ITEMS.register("marrowood_boat", () -> new CustomBoatItem(new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION), "marrowood"));
    public static final RegistryObject<Item> MANAWOOD_BOAT = ITEMS.register("manawood_boat", () -> new ManawoodBoatItem(new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION), "manawood"));

    //Hematite
    public static final RegistryObject<Item> HEMATITE = ITEMS.register("hematite", () -> new BlockItem(ModBlocks.HEMATITE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> HEMATITE_SLAB = ITEMS.register("hematite_slab", () -> new BlockItem(ModBlocks.HEMATITE_SLAB.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> HEMATITE_STAIRS = ITEMS.register("hematite_stairs", () -> new BlockItem(ModBlocks.HEMATITE_STAIRS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Polished Hematite
    public static final RegistryObject<Item> POLISHED_HEMATITE = ITEMS.register("polished_hematite", () -> new BlockItem(ModBlocks.POLISHED_HEMATITE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> POLISHED_HEMATITE_SLAB = ITEMS.register("polished_hematite_slab", () -> new BlockItem(ModBlocks.POLISHED_HEMATITE_SLAB.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> POLISHED_HEMATITE_STAIRS = ITEMS.register("polished_hematite_stairs", () -> new BlockItem(ModBlocks.POLISHED_HEMATITE_STAIRS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Weapons and Tools
    public static final RegistryObject<Item> VORPAL_SWORD = ITEMS.register("vorpal_sword", () -> new SwordItem(ItemTier.NETHERITE, 5, -2.8f, new Item.Properties().rarity(Rarity.EPIC).tab(ItemGroup.TAB_COMBAT).stacksTo(1).durability(4096).fireResistant()));

    //Misc Gizmos
    public static final RegistryObject<Item> CANDY_RING = ITEMS.register("candy_ring", CandyRing::new);
    public static final RegistryObject<Item> FAE_LEATHER = ITEMS.register("fae_leather", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> RAW_MANA = ITEMS.register("raw_mana", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));

    //Uncategorized Building Blocks
    public static final RegistryObject<Item> SOUL_PODZOL = ITEMS.register("soul_podzol", () -> new BlockItem(ModBlocks.SOUL_PODZOL.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> WASP_PAPER_BLOCK = ITEMS.register("wasp_paper_block", () -> new BlockItem(ModBlocks.WASP_PAPER_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> CANDYCANE_BRICKS = ITEMS.register("candycane_bricks", () -> new BlockItem(ModBlocks.CANDYCANE_BRICKS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Workstations for Villagers
    public static final RegistryObject<Item> HUNTING_TABLE = ITEMS.register("hunting_table", () -> new BlockItem(ModBlocks.HUNTING_TABLE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SOUL_FORGE = ITEMS.register("soul_forge", () -> new BlockItem(ModBlocks.SOUL_FORGE.get(), new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> ESSENCE_CHANNELER = ITEMS.register("essence_channeler", () -> new BlockItem(ModBlocks.ESSENCE_CHANNELER.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Music Discs
    public static final RegistryObject<Item> MUSIC_DISC_MARCH_OF_THE_ILLAGERS = ITEMS.register("music_disc_march_of_the_illagers", () -> new MusicDiscItem(1 , ModSounds.MUSIC_DISC_MARCH_OF_THE_ILLAGERS, new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MUSIC_DISC_BULLYRAG = ITEMS.register("music_disc_bullyrag", () -> new MusicDiscItem(2 , ModSounds.MUSIC_DISC_BULLYRAG, new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MUSIC_DISC_THE_LADY_RED = ITEMS.register("music_disc_the_lady_red", () -> new MusicDiscItem(1 , ModSounds.MUSIC_DISC_THE_LADY_RED, new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.RARE)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
