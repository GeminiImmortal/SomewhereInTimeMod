package net.geminiimmortal.mobius.block.custom.crop;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class AuroraBerryCrop extends BushBlock {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, getMaxAge());

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D)
    };

    public AuroraBerryCrop(Properties builder) {
        super(builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public void randomTick(BlockState p_225542_1_, ServerWorld p_225542_2_, BlockPos p_225542_3_, Random p_225542_4_) {
        int i = p_225542_1_.getValue(AGE);
        if (i < 3 && p_225542_2_.getRawBrightness(p_225542_3_.above(), 0) >= 9 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(p_225542_2_, p_225542_3_, p_225542_1_,p_225542_4_.nextInt(5) == 0)) {
            p_225542_2_.setBlock(p_225542_3_, p_225542_1_.setValue(AGE, Integer.valueOf(i + 1)), 2);
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(p_225542_2_, p_225542_3_, p_225542_1_);
        }

    }

    @Override
    public ItemStack getCloneItemStack(IBlockReader p_185473_1_, BlockPos p_185473_2_, BlockState p_185473_3_) {
        return new ItemStack(ModItems.AURORA_BERRY.get());
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getValue(AGE) < 2;
    }

    private static int getMaxAge(){
        return 2;
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        int i = blockState.getValue(AGE);
        boolean flag = i == 1;
        if (!flag && player.getItemInHand(hand).getItem() == Items.BONE_MEAL) {
            return ActionResultType.PASS;
        } else if (i == 2) {
            int j = 1 + world.random.nextInt(2);
            popResource(world, pos, new ItemStack(ModItems.AURORA_BERRY.get(), j + (flag ? 1 : 0)));
            world.playSound((PlayerEntity)null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            world.setBlock(pos, blockState.setValue(AGE, Integer.valueOf(1)), 2);
            return ActionResultType.sidedSuccess(world.isClientSide);
        } else {
            return super.use(blockState, world, pos, player, hand, result);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AuroraBerryCrop.SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(AGE);
    }

    @Override
    protected boolean mayPlaceOn(BlockState p_200014_1_, IBlockReader p_200014_2_, BlockPos p_200014_3_) {
        return p_200014_1_.is(ModBlocks.AURORA_GRASS_BLOCK.get()) || p_200014_1_.is(ModBlocks.AURORA_DIRT.get());
    }
}
