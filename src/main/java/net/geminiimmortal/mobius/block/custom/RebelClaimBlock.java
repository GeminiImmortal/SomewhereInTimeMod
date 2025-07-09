package net.geminiimmortal.mobius.block.custom;

import net.geminiimmortal.mobius.tileentity.ModTileEntities;
import net.geminiimmortal.mobius.tileentity.RebelClaimTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RebelClaimBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 14.0, 48.0, 14.0);
    public static final EnumProperty<CampClaimType> TYPE = EnumProperty.create("type", CampClaimType.class);


    public RebelClaimBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TYPE, CampClaimType.NONE));
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
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // Set the block’s facing direction based on the player’s placement
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());

    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        return ActionResultType.FAIL;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) { // Check if the block is being replaced by the same type
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof RebelClaimTileEntity) {
                // Remove the tile entity
                world.removeBlockEntity(pos);
            }
            if (world.isClientSide()) {
                world.playSound(null, pos, SoundEvents.BEACON_DEACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            super.onRemove(state, world, pos, newState, isMoving); // Call the super method to finalize
        }
    }



    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.REBEL_CLAIM.get().create();
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