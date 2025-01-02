package net.geminiimmortal.mobius.block;

import net.minecraft.block.WoodType;
import net.minecraft.util.ResourceLocation;
import net.geminiimmortal.mobius.MobiusMod;

public class ModWoodTypes {
    public static final WoodType MARROWOOD =
            WoodType.create(new ResourceLocation(MobiusMod.MOD_ID, "marrowood").toString());

    public static final WoodType MANAWOOD =
            WoodType.create(new ResourceLocation(MobiusMod.MOD_ID, "manawood").toString());

    public static final WoodType GLOAMTHORN =
            WoodType.create(new ResourceLocation(MobiusMod.MOD_ID, "gloamthorn").toString());
}
