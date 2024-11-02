package net.geminiimmortal.mobius.tileentity;

import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class ModSignsTileEntity extends SignTileEntity {
    public ModSignsTileEntity() {
        super();
    }

    @Override
    public TileEntityType<?> getType() {
        return ModTileEntities.SIGN_TILE_ENTITIES.get();
    }
}
