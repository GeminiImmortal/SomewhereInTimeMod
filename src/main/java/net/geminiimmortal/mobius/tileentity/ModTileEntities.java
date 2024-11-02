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
                    ModBlocks.MARROWOOD_WALL_SIGN.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<SoulForgeTileEntity>> SOUL_FORGE_TILE_ENTITY =
            TILE_ENTITIES.register("soul_forge_tile_entity", () -> TileEntityType.Builder.of(SoulForgeTileEntity::new,
                    ModBlocks.SOUL_FORGE.get())
                    .build(null));


    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}
