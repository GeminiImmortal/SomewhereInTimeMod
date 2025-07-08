package net.geminiimmortal.mobius.world.worldgen.structure.processor;

import com.mojang.serialization.Codec;
import net.minecraft.block.JigsawBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.template.*;

public class AnchorSnapProcessor extends StructureProcessor {

    public static final AnchorSnapProcessor INSTANCE = new AnchorSnapProcessor();
    public static final Codec<AnchorSnapProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public IStructureProcessorType<?> getType() {
        return ModProcessors.ANCHOR_SNAP_PROCESSOR;
    }

    @Override
    public Template.BlockInfo process(IWorldReader world,
                                      BlockPos pos,
                                      BlockPos originalPos,
                                      Template.BlockInfo original,
                                      Template.BlockInfo current,
                                      PlacementSettings settings,
                                      Template templateManager) {

        if (current.state.getBlock() instanceof JigsawBlock) {
            CompoundNBT nbt = current.nbt;
            if (nbt != null && nbt.contains("name")) {
                String name = nbt.getString("name");

                if (name.startsWith("mobius:warding_anchor_") || name.contains("camp")) {
                    // Shift this position to the closest open, grounded spot.
                    BlockPos anchorPos = findNearbyGroundedPosition((World) world, current.pos, 64);

                    // Return a new info object at the updated position:
                    return new Template.BlockInfo(anchorPos, current.state, current.nbt);
                }
            }
        }

        return current;
    }

    private BlockPos findNearbyGroundedPosition(World world, BlockPos center, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    BlockPos pos = center.offset(dx, dy, dz);
                    int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
                    BlockPos ground = new BlockPos(pos.getX(), pos.getY(), pos.getZ());

                    if (world.canSeeSky(ground) &&
                            world.getBlockState(ground.below()).isSolidRender(world, ground.below())) {
                        return ground;
                    }
                }
            }

        }
        return center;
    }
}
