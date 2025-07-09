package net.geminiimmortal.mobius.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.block.ModBlocks;

public class ModTileEntities {

    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MobiusMod.MOD_ID);


    public static final RegistryObject<TileEntityType<ModSignsTileEntity>> SIGN_TILE_ENTITIES =
            TILE_ENTITIES.register("sign", () -> TileEntityType.Builder.of(ModSignsTileEntity::new,
                    ModBlocks.MARROWOOD_SIGN.get(),
                    ModBlocks.MARROWOOD_WALL_SIGN.get(),
                    ModBlocks.MANAWOOD_SIGN.get(),
                    ModBlocks.MANAWOOD_WALL_SIGN.get(),
                    ModBlocks.GLOAMTHORN_SIGN.get(),
                    ModBlocks.GLOAMTHORN_WALL_SIGN.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<SoulForgeTileEntity>> SOUL_FORGE_TILE_ENTITY =
            TILE_ENTITIES.register("soul_forge_tile_entity", () -> TileEntityType.Builder.of(SoulForgeTileEntity::new,
                    ModBlocks.SOUL_FORGE.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<AstralConduitTileEntity>> ASTRAL_CONDUIT_TILE_ENTITY =
            TILE_ENTITIES.register("astral_conduit_tile_entity", () -> TileEntityType.Builder.of(AstralConduitTileEntity::new,
                            ModBlocks.ASTRAL_CONDUIT.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<EssenceChannelerTileEntity>> ESSENCE_CHANNELER_TILE_ENTITY =
            TILE_ENTITIES.register("essence_channeler_tile_entity", () -> TileEntityType.Builder.of(EssenceChannelerTileEntity::new,
                            ModBlocks.ESSENCE_CHANNELER.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<LatentManaCollectorTileEntity>> LATENT_MANA_COLLECTOR =
            TILE_ENTITIES.register("latent_mana_collector", () -> TileEntityType.Builder.of(LatentManaCollectorTileEntity::new,
                            ModBlocks.LATENT_MANA_COLLECTOR.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<WardingObeliskTileEntity>> WARDING_OBELISK =
            TILE_ENTITIES.register("warding_obelisk", () -> TileEntityType.Builder.of(WardingObeliskTileEntity::new, ModBlocks.WARDING_OBELISK.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<CapturePointTileEntity>> CAPTURE_POINT =
            TILE_ENTITIES.register("capture_point", () -> TileEntityType.Builder.of(CapturePointTileEntity::new, ModBlocks.CAPTURE_POINT.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<RebelClaimTileEntity>> REBEL_CLAIM =
            TILE_ENTITIES.register("rebel_claim", () -> TileEntityType.Builder.of(RebelClaimTileEntity::new, ModBlocks.REBEL_CLAIM.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<GlowingBlockTileEntity>> GLOWING_BLOCK = TILE_ENTITIES.register("glowing_block",
            () -> TileEntityType.Builder.of(GlowingBlockTileEntity::new, ModBlocks.STANDING_GLOOMCAP.get()).build(null));



    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}
