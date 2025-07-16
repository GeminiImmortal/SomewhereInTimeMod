package net.geminiimmortal.mobius.block.custom;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.geminiimmortal.mobius.entity.custom.AbstractImperialEntity;
import net.geminiimmortal.mobius.tileentity.WardingObeliskTileEntity;
import net.geminiimmortal.mobius.tileentity.ModTileEntities;
import net.geminiimmortal.mobius.util.CelebrationFireworksHelper;
import net.geminiimmortal.mobius.util.InfamyHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class WardingObeliskBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 14.0, 100.0, 14.0);
    public static final EnumProperty<WardingObeliskType> TYPE = EnumProperty.create("type", WardingObeliskType.class);

    public WardingObeliskBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TYPE, WardingObeliskType.NONE));
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        int tickCount = 0;

        if (worldIn.isClientSide()) {
            tickCount++;
            int particleCount = 50;
            double radius = 1.1;
            double height = 6.0;

            double time = tickCount + Minecraft.getInstance().getFrameTime();

            for (int i = 0; i < particleCount; i++) {
                double angle = (i / (double) particleCount) * 2 * Math.PI + (time * 0.1);
                double xOffset = Math.cos(angle) * radius;
                double zOffset = Math.sin(angle) * radius;
                double yOffset = (i / (double) particleCount) * height;

                worldIn.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
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
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // Set the block’s facing direction based on the player’s placement
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());

    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos,
                                PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide) {
            if (Objects.equals(state.getValue(TYPE), WardingObeliskType.GOVERNOR_TOWER)) {
                worldIn.removeBlockEntity(pos);
                worldIn.removeBlock(pos, false);
                return ActionResultType.SUCCESS;
            }
            ServerWorld serverWorld = (ServerWorld) worldIn;

            // Define the radius
            double radius = 24.0D;

            // Check for AbstractImperialEntity within radius
            List<AbstractImperialEntity> nearbyImperials = serverWorld.getEntitiesOfClass(
                    AbstractImperialEntity.class,
                    new AxisAlignedBB(
                            pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
                            pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
                    )
            );
            PlayerEntity nearestPlayer = worldIn.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 32D, false);

            // If none found, remove the block
            if (nearbyImperials.isEmpty()) {
                if (player.getCapability(ModCapabilities.INFAMY_CAPABILITY).isPresent() && ((ServerPlayerEntity) nearestPlayer != null)) {
                    InfamyHelper.get(nearestPlayer).addInfamy(500);
                }
                CelebrationFireworksHelper.spawnCelebrationFireworks((ServerWorld) worldIn, pos);
                TileEntity tileEntity = worldIn.getBlockEntity(pos);
                if (tileEntity instanceof WardingObeliskTileEntity) {
                    TileEntity te = worldIn.getBlockEntity(pos);
                    if (te instanceof WardingObeliskTileEntity) {
             //           ((WardingObeliskTileEntity) te).handleObeliskDestroyed();
                    }
                }
                worldIn.removeBlockEntity(pos);
                worldIn.removeBlock(pos, false);
                worldIn.setBlock(pos, ModBlocks.REBEL_CLAIM.get().defaultBlockState().setValue(RebelClaimBlock.TYPE, CampClaimType.WARDING_TOWER), 3);
            } else if (nearestPlayer != null) {
                nearestPlayer.sendMessage(new StringTextComponent("This structure cannot be captured until all nearby enemies are eliminated!").withStyle(TextFormatting.YELLOW), nearestPlayer.getUUID());
                return ActionResultType.FAIL;
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {

        if (!state.is(newState.getBlock())) { // Check if the block is being replaced by the same type
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof WardingObeliskTileEntity) {
                TileEntity te = world.getBlockEntity(pos);
                /*if (te instanceof WardingObeliskTileEntity) {
                    ((WardingObeliskTileEntity) te).handleObeliskDestroyed();
                }*/
            }
            if (world.isClientSide()) {
                world.playSound(null, pos, SoundEvents.BEACON_DEACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            world.removeBlockEntity(pos);
            super.onRemove(state, world, pos, newState, isMoving); // Call the super method to finalize
        }
    }



    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.WARDING_OBELISK.get().create();
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