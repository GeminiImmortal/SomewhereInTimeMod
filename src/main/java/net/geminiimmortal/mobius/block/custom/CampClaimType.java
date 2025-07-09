package net.geminiimmortal.mobius.block.custom;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum CampClaimType implements IStringSerializable {
    NONE,
    LUMBER_MILL,
    FARM,
    QUARRY;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
