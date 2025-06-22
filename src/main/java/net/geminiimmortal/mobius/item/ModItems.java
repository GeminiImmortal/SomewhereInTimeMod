package net.geminiimmortal.mobius.item;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.fluid.ModFluids;
import net.geminiimmortal.mobius.item.custom.*;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MobiusMod.MOD_ID);

    //Soil
    public static final RegistryObject<Item> AURORA_GRASS_BLOCK = ITEMS.register("aurora_grass_block", () -> new BlockItem(ModBlocks.AURORA_GRASS_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> AURORA_DIRT = ITEMS.register("aurora_dirt", () -> new BlockItem(ModBlocks.AURORA_DIRT.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SOUL_PODZOL = ITEMS.register("soul_podzol", () -> new BlockItem(ModBlocks.SOUL_PODZOL.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> HUECO_SAND = ITEMS.register("hueco_sand", () -> new BlockItem(ModBlocks.HUECO_SAND.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Leaves
    public static final RegistryObject<Item> BONE_LEAVES = ITEMS.register("bone_leaves", () -> new BlockItem(ModBlocks.BONE_LEAVES.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> MANAWOOD_LEAVES = ITEMS.register("manawood_leaves", () -> new BlockItem(ModBlocks.MANAWOOD_LEAVES.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> LIVING_MANAWOOD_LEAVES = ITEMS.register("living_manawood_leaves", () -> new BlockItem(ModBlocks.LIVING_MANAWOOD_LEAVES.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> GLOAMTHORN_LEAVES = ITEMS.register("gloamthorn_leaves", () -> new BlockItem(ModBlocks.GLOAMTHORN_LEAVES.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    //Logs
    public static final RegistryObject<Item> MARROWOOD_LOG = ITEMS.register("marrowood_log", () -> new BlockItem(ModBlocks.MARROWOOD_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MANAWOOD_LOG = ITEMS.register("manawood_log", () -> new BlockItem(ModBlocks.MANAWOOD_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> LIVING_MANAWOOD_LOG = ITEMS.register("living_manawood_log", () -> new BlockItem(ModBlocks.LIVING_MANAWOOD_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> GLOAMTHORN_LOG = ITEMS.register("gloamthorn_log", () -> new BlockItem(ModBlocks.GLOAMTHORN_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Stripped Logs
    public static final RegistryObject<Item> STRIPPED_MARROWOOD_LOG = ITEMS.register("stripped_marrowood_log", () -> new BlockItem(ModBlocks.STRIPPED_MARROWOOD_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STRIPPED_MANAWOOD_LOG = ITEMS.register("stripped_manawood_log", () -> new BlockItem(ModBlocks.STRIPPED_MANAWOOD_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STRIPPED_GLOAMTHORN_LOG = ITEMS.register("stripped_gloamthorn_log", () -> new BlockItem(ModBlocks.STRIPPED_GLOAMTHORN_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Bark Blocks
    public static final RegistryObject<Item> MARROWOOD_WOOD = ITEMS.register("marrowood_wood", () -> new BlockItem(ModBlocks.MARROWOOD_WOOD.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MANAWOOD_WOOD = ITEMS.register("manawood_wood", () -> new BlockItem(ModBlocks.MANAWOOD_WOOD.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> GLOAMTHORN_WOOD = ITEMS.register("gloamthorn_wood", () -> new BlockItem(ModBlocks.GLOAMTHORN_WOOD.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Stripped Bark Blocks
    public static final RegistryObject<Item> STRIPPED_MARROWOOD_WOOD = ITEMS.register("stripped_marrowood_wood", () -> new BlockItem(ModBlocks.STRIPPED_MARROWOOD_WOOD.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STRIPPED_MANAWOOD_WOOD = ITEMS.register("stripped_manawood_wood", () -> new BlockItem(ModBlocks.STRIPPED_MANAWOOD_WOOD.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STRIPPED_GLOAMTHORN_WOOD = ITEMS.register("stripped_gloamthorn_wood", () -> new BlockItem(ModBlocks.STRIPPED_GLOAMTHORN_WOOD.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Planks
    public static final RegistryObject<Item> MARROWOOD_PLANKS = ITEMS.register("marrowood_planks", () -> new BlockItem(ModBlocks.MARROWOOD_PLANKS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MANAWOOD_PLANKS = ITEMS.register("manawood_planks", () -> new BlockItem(ModBlocks.MANAWOOD_PLANKS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> GLOAMTHORN_PLANKS = ITEMS.register("gloamthorn_planks", () -> new BlockItem(ModBlocks.GLOAMTHORN_PLANKS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Wooden Stairs
    public static final RegistryObject<Item> MARROWOOD_STAIRS = ITEMS.register("marrowood_stairs", () -> new BlockItem(ModBlocks.MARROWOOD_STAIRS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MANAWOOD_STAIRS = ITEMS.register("manawood_stairs", () -> new BlockItem(ModBlocks.MANAWOOD_STAIRS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> GLOAMTHORN_STAIRS = ITEMS.register("gloamthorn_stairs", () -> new BlockItem(ModBlocks.GLOAMTHORN_STAIRS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Wooden Slabs
    public static final RegistryObject<Item> MARROWOOD_SLAB = ITEMS.register("marrowood_slab", () -> new BlockItem(ModBlocks.MARROWOOD_SLAB.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MANAWOOD_SLAB = ITEMS.register("manawood_slab", () -> new BlockItem(ModBlocks.MANAWOOD_SLAB.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> GLOAMTHORN_SLAB = ITEMS.register("gloamthorn_slab", () -> new BlockItem(ModBlocks.GLOAMTHORN_SLAB.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Wooden Fences
    public static final RegistryObject<Item> MARROWOOD_FENCE = ITEMS.register("marrowood_fence", () -> new BlockItem(ModBlocks.MARROWOOD_FENCE.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> MANAWOOD_FENCE = ITEMS.register("manawood_fence", () -> new BlockItem(ModBlocks.MANAWOOD_FENCE.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> GLOAMTHORN_FENCE = ITEMS.register("gloamthorn_fence", () -> new BlockItem(ModBlocks.GLOAMTHORN_FENCE.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    //Wooden Fence Gates
    public static final RegistryObject<Item> MARROWOOD_FENCE_GATE = ITEMS.register("marrowood_fence_gate", () -> new BlockItem(ModBlocks.MARROWOOD_FENCE_GATE.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> MANAWOOD_FENCE_GATE = ITEMS.register("manawood_fence_gate", () -> new BlockItem(ModBlocks.MANAWOOD_FENCE_GATE.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> GLOAMTHORN_FENCE_GATE = ITEMS.register("gloamthorn_fence_gate", () -> new BlockItem(ModBlocks.GLOAMTHORN_FENCE_GATE.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));

    //Wooden Doors
    public static final RegistryObject<Item> MARROWOOD_DOOR = ITEMS.register("marrowood_door", () -> new BlockItem(ModBlocks.MARROWOOD_DOOR.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> MANAWOOD_DOOR = ITEMS.register("manawood_door", () -> new BlockItem(ModBlocks.MANAWOOD_DOOR.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> GLOAMTHORN_DOOR = ITEMS.register("gloamthorn_door", () -> new BlockItem(ModBlocks.GLOAMTHORN_DOOR.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));

    //Wooden Trapdoors
    public static final RegistryObject<Item> MARROWOOD_TRAPDOOR = ITEMS.register("marrowood_trapdoor", () -> new BlockItem(ModBlocks.MARROWOOD_TRAPDOOR.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> MANAWOOD_TRAPDOOR = ITEMS.register("manawood_trapdoor", () -> new BlockItem(ModBlocks.MANAWOOD_TRAPDOOR.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> GLOAMTHORN_TRAPDOOR = ITEMS.register("gloamthorn_trapdoor", () -> new BlockItem(ModBlocks.GLOAMTHORN_TRAPDOOR.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));

    //Wooden Pressure Plates
    public static final RegistryObject<Item> MARROWOOD_PRESSURE_PLATE = ITEMS.register("marrowood_pressure_plate", () -> new BlockItem(ModBlocks.MARROWOOD_PRESSURE_PLATE.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> MANAWOOD_PRESSURE_PLATE = ITEMS.register("manawood_pressure_plate", () -> new BlockItem(ModBlocks.MANAWOOD_PRESSURE_PLATE.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> GLOAMTHORN_PRESSURE_PLATE = ITEMS.register("gloamthorn_pressure_plate", () -> new BlockItem(ModBlocks.GLOAMTHORN_PRESSURE_PLATE.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));

    //Wooden Buttons
    public static final RegistryObject<Item> MARROWOOD_BUTTON = ITEMS.register("marrowood_button", () -> new BlockItem(ModBlocks.MARROWOOD_BUTTON.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> MANAWOOD_BUTTON = ITEMS.register("manawood_button", () -> new BlockItem(ModBlocks.MANAWOOD_BUTTON.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
    public static final RegistryObject<Item> GLOAMTHORN_BUTTON = ITEMS.register("gloamthorn_button", () -> new BlockItem(ModBlocks.GLOAMTHORN_BUTTON.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));

    //Signs
    public static final RegistryObject<Item> MARROWOOD_SIGN = ITEMS.register("marrowood_sign", () -> new SignItem(new Item.Properties().tab(ItemGroup.TAB_DECORATIONS).stacksTo(16),ModBlocks.MARROWOOD_SIGN.get(), ModBlocks.MARROWOOD_WALL_SIGN.get()));
    public static final RegistryObject<Item> MANAWOOD_SIGN = ITEMS.register("manawood_sign", () -> new SignItem(new Item.Properties().tab(ItemGroup.TAB_DECORATIONS).stacksTo(16),ModBlocks.MANAWOOD_SIGN.get(), ModBlocks.MANAWOOD_WALL_SIGN.get()));
    public static final RegistryObject<Item> GLOAMTHORN_SIGN = ITEMS.register("gloamthorn_sign", () -> new SignItem(new Item.Properties().tab(ItemGroup.TAB_DECORATIONS).stacksTo(16),ModBlocks.GLOAMTHORN_SIGN.get(), ModBlocks.GLOAMTHORN_WALL_SIGN.get()));

    //Saplings
    public static final RegistryObject<Item> MARROWOOD_SAPLING = ITEMS.register("marrowood_sapling", () -> new BlockItem(ModBlocks.MARROWOOD_SAPLING.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> MANAWOOD_SAPLING = ITEMS.register("manawood_sapling", () -> new BlockItem(ModBlocks.MANAWOOD_SAPLING.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> GLOAMTHORN_SAPLING = ITEMS.register("gloamthorn_sapling", () -> new BlockItem(ModBlocks.GLOAMTHORN_SAPLING.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    //Boats
    public static final RegistryObject<Item> MARROWOOD_BOAT = ITEMS.register("marrowood_boat", () -> new CustomBoatItem(new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION), "marrowood"));
    public static final RegistryObject<Item> MANAWOOD_BOAT = ITEMS.register("manawood_boat", () -> new ManawoodBoatItem(new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION), "manawood"));
    public static final RegistryObject<Item> GLOAMTHORN_BOAT = ITEMS.register("gloamthorn_boat", () -> new GloamthornBoatItem(new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION), "gloamthorn"));

    //Flora
    public static final RegistryObject<Item> STANDING_GLOOMCAP = ITEMS.register("standing_gloomcap", () -> new BlockItem(ModBlocks.STANDING_GLOOMCAP.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> GIANT_GLOOMCAP_CAP = ITEMS.register("giant_gloomcap_cap", () -> new BlockItem(ModBlocks.GIANT_GLOOMCAP_CAP.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> GIANT_GLOOMCAP_STEM = ITEMS.register("giant_gloomcap_stem", () -> new BlockItem(ModBlocks.GIANT_GLOOMCAP_STEM.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    //Hematite
    public static final RegistryObject<Item> HEMATITE = ITEMS.register("hematite", () -> new BlockItem(ModBlocks.HEMATITE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> HEMATITE_SLAB = ITEMS.register("hematite_slab", () -> new BlockItem(ModBlocks.HEMATITE_SLAB.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> HEMATITE_STAIRS = ITEMS.register("hematite_stairs", () -> new BlockItem(ModBlocks.HEMATITE_STAIRS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> HEMATITE_WALL = ITEMS.register("hematite_wall", () -> new BlockItem(ModBlocks.HEMATITE_WALL.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Ores
    public static final RegistryObject<Item> NICKEL_ORE = ITEMS.register("nickel_ore", () -> new BlockItem(ModBlocks.NICKEL_ORE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> HEMATITE_IRON_ORE = ITEMS.register("hematite_iron_ore", () -> new BlockItem(ModBlocks.HEMATITE_IRON_ORE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Polished Hematite
    public static final RegistryObject<Item> POLISHED_HEMATITE = ITEMS.register("polished_hematite", () -> new BlockItem(ModBlocks.POLISHED_HEMATITE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> POLISHED_HEMATITE_SLAB = ITEMS.register("polished_hematite_slab", () -> new BlockItem(ModBlocks.POLISHED_HEMATITE_SLAB.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> POLISHED_HEMATITE_STAIRS = ITEMS.register("polished_hematite_stairs", () -> new BlockItem(ModBlocks.POLISHED_HEMATITE_STAIRS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Workstations for Villagers
    public static final RegistryObject<Item> HUNTING_TABLE = ITEMS.register("hunting_table", () -> new BlockItem(ModBlocks.HUNTING_TABLE.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> SOUL_FORGE = ITEMS.register("soul_forge", () -> new BlockItem(ModBlocks.SOUL_FORGE.get(), new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> ESSENCE_CHANNELER = ITEMS.register("essence_channeler", () -> new BlockItem(ModBlocks.ESSENCE_CHANNELER.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> SMUGGLER_STRONGBOX = ITEMS.register("smuggler_strongbox", () -> new BlockItem(ModBlocks.SMUGGLER_STRONGBOX.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    //Crops and Food
    public static final RegistryObject<Item> MANA_WART = ITEMS.register("mana_wart",
            () -> new BlockItem(ModBlocks.MANA_WART.get(), new Item.Properties()
                    .food(new Food.Builder().nutrition(1).saturationMod(0.1f).build())
                    .tab(ItemGroup.TAB_FOOD)));
    public static final RegistryObject<Item> MANAGLOOM_PIE = ITEMS.register("managloom_pie", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_FOOD).food(new Food.Builder().nutrition(8).saturationMod(0.5f).effect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 200), 1f).build())));
    public static final RegistryObject<Item> GLOOMCAP = ITEMS.register("gloomcap",
            () -> new BlockItem(ModBlocks.GLOOMCAP.get() ,new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> RAW_FAE_VENISON = ITEMS.register("raw_fae_venison", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_FOOD).food(new Food.Builder().nutrition(3).saturationMod(0.1f).meat().build())));
    public static final RegistryObject<Item> COOKED_FAE_VENISON = ITEMS.register("cooked_fae_venison", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_FOOD).food(new Food.Builder().nutrition(10).saturationMod(0.8f).effect(new EffectInstance(Effects.GLOWING, 100), 1f).meat().build())));

    //Workstations Misc.
    public static final RegistryObject<Item> ASTRAL_CONDUIT = ITEMS.register("astral_conduit", () -> new BlockItem(ModBlocks.ASTRAL_CONDUIT.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> LATENT_MANA_COLLECTOR = ITEMS.register("latent_mana_collector", () -> new BlockItem(ModBlocks.LATENT_MANA_COLLECTOR.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    //Uncategorized Building Blocks
    public static final RegistryObject<Item> WASP_PAPER_BLOCK = ITEMS.register("wasp_paper_block", () -> new BlockItem(ModBlocks.WASP_PAPER_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MAGIC_KEYSTONE = ITEMS.register("magic_keystone", () -> new BlockItem(ModBlocks.MAGIC_KEYSTONE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> BLOCK_OF_NICKEL = ITEMS.register("block_of_nickel", () -> new BlockItem(ModBlocks.BLOCK_OF_NICKEL.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> DUNGEON_BLOCK = ITEMS.register("dungeon_block", () -> new BlockItem(ModBlocks.DUNGEON_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> CRYSTAL_KEEP_BLOCK = ITEMS.register("crystal_keep_block", () -> new BlockItem(ModBlocks.CRYSTAL_KEEP_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Bloodstone
    public static final RegistryObject<Item> BLOODSTONE = ITEMS.register("bloodstone", () -> new BlockItem(ModBlocks.BLOODSTONE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> BLOODSTONE_GEM_ORE = ITEMS.register("bloodstone_gem_ore", () -> new BlockItem(ModBlocks.BLOODSTONE_GEM_ORE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Shattered Plains Blocks
    public static final RegistryObject<Item> OBSECFUTORIA = ITEMS.register("obsecfutoria", () -> new BlockItem(ModBlocks.OBSECFUTORIA.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Celestial Blocks
    public static final RegistryObject<Item> CELESTIAL_PILLAR = ITEMS.register("celestial_pillar", () -> new BlockItem(ModBlocks.CELESTIAL_PILLAR.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> CELESTIAL_TILE = ITEMS.register("celestial_tile", () -> new BlockItem(ModBlocks.CELESTIAL_TILE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> CELESTIAL_TILE_SLAB = ITEMS.register("celestial_tile_slab", () -> new BlockItem(ModBlocks.CELESTIAL_TILE_SLAB.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> CELESTIAL_STAIRS = ITEMS.register("celestial_stairs", () -> new BlockItem(ModBlocks.CELESTIAL_STAIRS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> CELESTIAL_WALL = ITEMS.register("celestial_wall", () -> new BlockItem(ModBlocks.CELESTIAL_WALL.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    //Staffs
    public static final RegistryObject<Item> STAFF_PROTECTION_OBSIDIAN_ROD_FAE_LEATHER_BINDING = ITEMS.register("staff_protection_obsidian_rod_fae_leather_binding", () -> new ModularStaff(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_COMBAT), StaffType.PROTECTION_OBSIDIAN_FAE_LEATHER));
    public static final RegistryObject<Item> STAFF_PROTECTION_OBSIDIAN_ROD_MOLVAN_STEEL_BINDING = ITEMS.register("staff_protection_obsidian_rod_molvan_steel_binding", () -> new ModularStaff(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_COMBAT), StaffType.PROTECTION_OBSIDIAN_MOLVAN_STEEL));
    public static final RegistryObject<Item> STAFF_LIGHTNING_OBSIDIAN_ROD_MOLVAN_STEEL_BINDING = ITEMS.register("staff_lightning_obsidian_rod_molvan_steel_binding", () -> new ModularStaff(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_COMBAT), StaffType.LIGHTNING_OBSIDIAN_MOLVAN_STEEL));
    public static final RegistryObject<Item> STAFF_LIGHTNING_OBSIDIAN_ROD_FAE_LEATHER_BINDING = ITEMS.register("staff_lightning_obsidian_rod_fae_leather_binding", () -> new ModularStaff(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_COMBAT), StaffType.LIGHTNING_OBSIDIAN_FAE_LEATHER));
    public static final RegistryObject<Item> STAFF_FIRE_OBSIDIAN_ROD_FAE_LEATHER_BINDING = ITEMS.register("staff_fire_obsidian_rod_fae_leather_binding", () -> new ModularStaff(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_COMBAT), StaffType.FIRE_OBSIDIAN_FAE_LEATHER));
    public static final RegistryObject<Item> GRAVITAS = ITEMS.register("gravitas", () -> new GravitasStaff(new Item.Properties().tab(ItemGroup.TAB_COMBAT).stacksTo(1).rarity(Rarity.EPIC), StaffType.GRAVITAS));
    public static final RegistryObject<Item> HURRICANE_STAFF = ITEMS.register("hurricane_staff", () -> new HurricaneStaff(new Item.Properties().tab(ItemGroup.TAB_COMBAT).stacksTo(1).rarity(Rarity.EPIC), StaffType.HURRICANE_STAFF));
    public static final RegistryObject<Item> REJECTION_STAFF = ITEMS.register("rejection_staff", () -> new RejectionStaff(new Item.Properties().tab(ItemGroup.TAB_COMBAT).stacksTo(1).rarity(Rarity.EPIC), StaffType.REJECTION_STAFF));
    public static final RegistryObject<Item> RING_OF_REPULSION = ITEMS.register("ring_of_warding", () -> new RingOfWarding(new Item.Properties().tab(ItemGroup.TAB_COMBAT).stacksTo(1).rarity(Rarity.RARE)));

    //Weapons and Tools
    public static final RegistryObject<Item> VORPAL_SWORD = ITEMS.register("vorpal_sword", () -> new VorpalSword(ItemTier.NETHERITE, 3, -2.8f, new Item.Properties().rarity(Rarity.RARE).tab(ItemGroup.TAB_COMBAT).stacksTo(1).durability(2032)));
    public static final RegistryObject<Item> ASTRAL_SWORD = ITEMS.register("astral_sword", () -> new SwordItem(ItemTier.NETHERITE, 5, -2.4f, new Item.Properties().tab(ItemGroup.TAB_COMBAT).stacksTo(1).durability(4096)));
    public static final RegistryObject<Item> ASTRAL_SHOVEL = ITEMS.register("astral_shovel", () -> new ShovelItem(ItemTier.NETHERITE, 3, -3.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS).stacksTo(1).durability(4096).fireResistant()));
    public static final RegistryObject<Item> ASTRAL_PICKAXE = ITEMS.register("astral_pickaxe", () -> new PickaxeItem(ItemTier.NETHERITE, 3, -2.8f, new Item.Properties().tab(ItemGroup.TAB_TOOLS).stacksTo(1).durability(4096).fireResistant()));
    public static final RegistryObject<Item> ASTRAL_AXE = ITEMS.register("astral_axe", () -> new AxeItem(ItemTier.NETHERITE, 5.5f, -3.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS).stacksTo(1).durability(4096).fireResistant()));
    public static final RegistryObject<Item> ASTRAL_HOE = ITEMS.register("astral_hoe", () -> new HoeItem(ItemTier.NETHERITE, -4, -0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS).stacksTo(1).durability(4096).fireResistant()));

    //Armor
    public static final RegistryObject<Item> NICKEL_BOOTS = ITEMS.register("nickel_boots",
            () -> new ArmorItem(ModArmorMaterial.NICKEL, EquipmentSlotType.FEET,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<Item> NICKEL_CHESTPLATE = ITEMS.register("nickel_chestplate",
            () -> new ArmorItem(ModArmorMaterial.NICKEL, EquipmentSlotType.CHEST,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<Item> NICKEL_LEGGINGS = ITEMS.register("nickel_leggings",
            () -> new ArmorItem(ModArmorMaterial.NICKEL, EquipmentSlotType.LEGS,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<Item> NICKEL_HELMET = ITEMS.register("nickel_helmet",
            () -> new ArmorItem(ModArmorMaterial.NICKEL, EquipmentSlotType.HEAD,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<Item> ASTRAL_BOOTS = ITEMS.register("astral_boots",
            () -> new AstralArmor(AstralArmorMaterial.ASTRAL, EquipmentSlotType.FEET,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<Item> ASTRAL_CHESTPLATE = ITEMS.register("astral_chestplate",
            () -> new AstralArmor(AstralArmorMaterial.ASTRAL, EquipmentSlotType.CHEST,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<Item> ASTRAL_LEGGINGS = ITEMS.register("astral_leggings",
            () -> new AstralArmor(AstralArmorMaterial.ASTRAL, EquipmentSlotType.LEGS,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<Item> ASTRAL_HELMET = ITEMS.register("astral_helmet",
            () -> new AstralArmor(AstralArmorMaterial.ASTRAL, EquipmentSlotType.HEAD,
                    new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

    //Spawn Eggs
    public static final RegistryObject<ModSpawnEgg> CLUB_GOLEM_SPAWN_EGG = ITEMS.register("club_golem_spawn_egg", () -> new ModSpawnEgg(ModEntityTypes.CLUB_GOLEM, 0x2c615b, 0x2c5960, new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<ModSpawnEgg> DIAMOND_GOLEM_SPAWN_EGG = ITEMS.register("diamond_golem_spawn_egg", () -> new ModSpawnEgg(ModEntityTypes.DIAMOND_GOLEM, 0x2c615b, 0x2ca5b1, new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<ModSpawnEgg> HEART_GOLEM_SPAWN_EGG = ITEMS.register("heart_golem_spawn_egg", () -> new ModSpawnEgg(ModEntityTypes.HEART_GOLEM, 0x2c615b, 0x2cc590, new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<ModSpawnEgg> SPADE_GOLEM_SPAWN_EGG = ITEMS.register("spade_golem_spawn_egg", () -> new ModSpawnEgg(ModEntityTypes.SPADE_GOLEM, 0x2c615b, 0x7ba090, new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<ModSpawnEgg> FAEDEER_SPAWN_EGG = ITEMS.register("faedeer_spawn_egg", () -> new ModSpawnEgg(ModEntityTypes.FAEDEER, 0x2c7a99, 0x2ca399, new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<ModSpawnEgg> MOLVAN_SPAWN_EGG = ITEMS.register("molvan_spawn_egg", () -> new ModSpawnEgg(ModEntityTypes.MOLVAN, 0xa2a090, 0xc5a090, new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<ModSpawnEgg> BONE_WOLF_SPAWN_EGG = ITEMS.register("bone_wolf_spawn_egg", () -> new ModSpawnEgg(ModEntityTypes.BONE_WOLF, 0x7A7A7A, 0x8B0000, new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<ModSpawnEgg> INFERNAL_BRIAR_SPAWN_EGG = ITEMS.register("infernal_briar_spawn_egg", () -> new ModSpawnEgg(ModEntityTypes.INFERNAL_BRIAR, 0x8B0000, 0x0f2310, new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<ModSpawnEgg> ANGLERFISH_SPAWN_EGG = ITEMS.register("anglerfish_spawn_egg", () -> new ModSpawnEgg(ModEntityTypes.ANGLERFISH, 0x042315, 0x09ff91, new Item.Properties().tab(ItemGroup.TAB_MISC)));

    //Misc Gizmos
    public static final RegistryObject<Item> GAIA_STAR = ITEMS.register("gaia_star", GaiaStar::new);
    public static final RegistryObject<Item> FAE_LEATHER = ITEMS.register("fae_leather", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> RAW_MANA = ITEMS.register("raw_mana", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> BALL_OF_CONDENSED_MANA = ITEMS.register("ball_of_condensed_mana", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> CUBE_OF_STABILIZED_VOID_ENERGY = ITEMS.register("cube_of_stabilized_void_energy", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MAGICALLY_REINFORCED_NETHERITE_INGOT = ITEMS.register("magically_reinforced_netherite_ingot", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MOLVANSTEEL_INGOT = ITEMS.register("molvansteel_ingot", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> BLOODROP_GEMSTONE = ITEMS.register("bloodrop_gemstone", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> NICKEL_INGOT = ITEMS.register("nickel_ingot", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> ASTRAL_INGOT = ITEMS.register("astral_ingot", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> VIAL_OF_LIQUID_SCREAMS = ITEMS.register("vial_of_liquid_screams", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MANA_VIAL = ITEMS.register("mana_vial", () -> new ManaVial(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).durability(FlaskType.MANA_VIAL.getManaCapacity()).rarity(FlaskType.MANA_VIAL.getRarity()), FlaskType.MANA_VIAL, FlaskType.MANA_VIAL.getRarity()));
    public static final RegistryObject<Item> MANA_FLASK = ITEMS.register("mana_flask", () -> new ManaVial(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).durability(FlaskType.MANA_FLASK.getManaCapacity()).rarity(FlaskType.MANA_FLASK.getRarity()), FlaskType.MANA_FLASK, FlaskType.MANA_FLASK.getRarity()));
    public static final RegistryObject<Item> MANA_ALEMBIC = ITEMS.register("mana_alembic", () -> new ManaVial(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).durability(FlaskType.MANA_ALEMBIC.getManaCapacity()).rarity(FlaskType.MANA_ALEMBIC.getRarity()), FlaskType.MANA_ALEMBIC, FlaskType.MANA_ALEMBIC.getRarity()));
    public static final RegistryObject<Item> GAIA_FLASK = ITEMS.register("gaia_flask", () -> new ManaVial(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).durability(FlaskType.GAIA_FLASK.getManaCapacity()).rarity(FlaskType.GAIA_FLASK.getRarity()), FlaskType.GAIA_FLASK, FlaskType.GAIA_FLASK.getRarity()));
    public static final RegistryObject<Item> OBSIDIAN_ROD = ITEMS.register("obsidian_rod", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> MANA_TAP = ITEMS.register("mana_tap", () -> new ManaTap(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).durability(128), ModBlocks.LIVING_MANAWOOD_LOG.get(), ModBlocks.MANAWOOD_LOG.get(), ModItems.RAW_MANA.get()));
    public static final RegistryObject<Item> OBLIVION_STONE = ITEMS.register("oblivion_stone", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> MYSTERIOUS_JOURNAL = ITEMS.register("mysterious_journal", () -> new MysteriousJournal(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> JOURNAL_PAGE = ITEMS.register("journal_page", () -> new JournalPage(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.UNCOMMON)));

    //Fluid Bucket Items
    public static final RegistryObject<Item> ECTOPLASM_BUCKET = ITEMS.register("ectoplasm_bucket", () -> new BucketItem(() -> ModFluids.ECTOPLASM_FLUID.get(), new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> BOG_WATER_BUCKET = ITEMS.register("bog_water_bucket", () -> new BucketItem(() -> ModFluids.BOG_WATER_FLUID.get(), new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_MISC)));

    //Music Discs
    public static final RegistryObject<Item> MUSIC_DISC_MARCH_OF_THE_ILLAGERS = ITEMS.register("music_disc_march_of_the_illagers", () -> new MusicDiscItem(1 , ModSounds.MUSIC_DISC_MARCH_OF_THE_ILLAGERS, new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MUSIC_DISC_BULLYRAG = ITEMS.register("music_disc_bullyrag", () -> new MusicDiscItem(2 , ModSounds.MUSIC_DISC_BULLYRAG, new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MUSIC_DISC_THE_LADY_RED = ITEMS.register("music_disc_the_lady_red", () -> new MusicDiscItem(3 , ModSounds.MUSIC_DISC_THE_LADY_RED, new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.RARE)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
