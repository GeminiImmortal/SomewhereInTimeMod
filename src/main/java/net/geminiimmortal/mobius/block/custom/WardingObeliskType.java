package net.geminiimmortal.mobius.block.custom;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum WardingObeliskType implements IStringSerializable {
    NONE,
    GOVERNOR_TOWER,
    WARDING_TOWER;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
