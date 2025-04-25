package net.geminiimmortal.mobius.world.worldgen.structure.processor;

import com.mojang.serialization.Codec;
import net.geminiimmortal.mobius.block.custom.WardBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.*;

public class WardOrientationFixProcessor extends StructureProcessor {
    public static final Codec<WardOrientationFixProcessor> CODEC = Codec.unit(new WardOrientationFixProcessor());

    @Override
    public Template.BlockInfo processBlock(IWorldReader level, BlockPos pos,
                                           BlockPos relativePos, Template.BlockInfo original, Template.BlockInfo current, PlacementSettings settings) {

        if (current.state.getBlock() instanceof WardBlock) {
            // Always set it to Z axis (or whichever you want)
            return new Template.BlockInfo(
                    current.pos,
                    current.state.setValue(WardBlock.AXIS, Direction.Axis.Z),
                    current.nbt
            );
        }

        return current;
    }



    @Override
    protected IStructureProcessorType<?> getType(){
        return ModProcessors.WARD_ORIENTATION_FIX; // Register this in your mod
    }
}

