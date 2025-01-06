package net.geminiimmortal.mobius.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class ModFluidTypes {
    public static final Material BOG_WATER_TYPE = new Material.Builder(MaterialColor.WATER).noCollider().nonSolid().liquid().replaceable().build();
    public static final Material ECTOPLASM_TYPE = new Material.Builder(MaterialColor.WATER).noCollider().nonSolid().liquid().replaceable().build();
}
