package net.geminiimmortal.mobius.block.custom;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum JumpPadType implements IStringSerializable {
    NONE,
    THUNDERBIRD;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
