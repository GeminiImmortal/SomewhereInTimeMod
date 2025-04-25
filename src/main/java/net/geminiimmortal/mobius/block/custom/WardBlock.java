package net.geminiimmortal.mobius.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class WardBlock extends Block {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_AABB = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
    protected static final VoxelShape Z_AABB = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

    public WardBlock() {
        super(Properties.of(Material.PORTAL)
                .noOcclusion()
                .strength(-1.0F, 3600000.0F)
                .noDrops()
                .lightLevel(state -> 6)
        );
        registerDefaultState(stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state == null || world == null || pos == null) {
            return; // Avoid rendering particles if any of these are null
        }

        for (int i = 0; i < 2; i++) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            world.addParticle(ParticleTypes.SOUL, x, y, z, 0, 0.02, 0);
        }
    }




    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction.Axis axis = state.getValue(AXIS);
        if (axis == null) {
            return VoxelShapes.block(); // Default to full block shape if AXIS is null
        }

        switch (axis) {
            case Z:
                return Z_AABB;
            case X:
            default:
                return X_AABB;
        }
    }


    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        Entity entity = context.getEntity();

        Direction.Axis axis = state.getValue(WardBlock.AXIS);
        if (entity != null) {
            if (axis == Direction.Axis.Z) {
                assert entity != null;
                if (entity.getZ() < pos.getZ() + 0.5) {
                    return VoxelShapes.empty();
                }
            } else if (axis == Direction.Axis.X) {
                assert entity != null;
                if (entity.getX() < pos.getX() + 0.5) {
                    return VoxelShapes.empty();
                }
            }
        }


        return VoxelShapes.block(); // block everyone else
    }


}


