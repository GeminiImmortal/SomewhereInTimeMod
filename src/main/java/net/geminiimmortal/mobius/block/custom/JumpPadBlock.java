package net.geminiimmortal.mobius.block.custom;

import net.geminiimmortal.mobius.tileentity.JumpPadTileEntity;
import net.geminiimmortal.mobius.tileentity.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class JumpPadBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
    public static final EnumProperty<JumpPadType> TYPE = EnumProperty.create("type", JumpPadType.class);
    public static final BooleanProperty LINKED = BooleanProperty.create("linked");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");


    public JumpPadBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TYPE, JumpPadType.NONE).setValue(LINKED, true).setValue(ACTIVE, true));
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        int tickCount = 0;

        if (worldIn.isClientSide() && stateIn.getBlockState().getValue(LINKED) && stateIn.getBlockState().getValue(ACTIVE)) {
            tickCount++;
            int particleCount = 4;
            double radius = 0.5;
            double height = 0.5;

            double time = tickCount + Minecraft.getInstance().getFrameTime();

            for (int i = 0; i < particleCount; i++) {
                double angle = (i / (double) particleCount) * 2 * Math.PI + (time * 0.1);
                double xOffset = Math.cos(angle) * radius;
                double zOffset = Math.sin(angle) * radius;
                double yOffset = (i / (double) particleCount) * height;

                worldIn.addParticle(ParticleTypes.SWEEP_ATTACK,
                        pos.getX() + xOffset,
                        pos.getY() + yOffset,
                        pos.getZ() + zOffset,
                        0.0, 0.0, 0.0);
            }
        }
        super.animateTick(stateIn, worldIn, pos, rand);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(TYPE);
        builder.add(LINKED);
        builder.add(ACTIVE);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // Set the block’s facing direction based on the player’s placement
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());

    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {

        if (!state.is(newState.getBlock())) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof JumpPadTileEntity) {
                TileEntity te = world.getBlockEntity(pos);
            }
            if (world.isClientSide()) {
                world.playSound(null, pos, SoundEvents.BEACON_DEACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            world.removeBlockEntity(pos);
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }



    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.JUMP_PAD.get().create();
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}