package net.geminiimmortal.mobius.block.custom;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.FaestagEntity;
import net.geminiimmortal.mobius.entity.goals.util.TeleportUtil;
import net.geminiimmortal.mobius.tileentity.ModTileEntities;
import net.geminiimmortal.mobius.tileentity.MysticTearTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class MysticTearBlock extends Block {
    public MysticTearBlock(Properties properties) {
        super(properties);
        this.properties.noOcclusion();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.MYSTIC_TEAR.get().create();
    }

    /*@Override
    public void onPlace(BlockState state, World world, BlockPos pos,
                        BlockState oldState, boolean moving) {
        if (world.isClientSide) return;

        TileEntity te = world.getBlockEntity(pos);
        if (!(te instanceof MysticTearTileEntity)) return;

        FaestagEntity stag = ModEntityTypes.FAESTAG.get().create((ServerWorld)world);

        if (stag != null) {
            BlockPos initialPos = new BlockPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            stag.setPos(initialPos.getX(), initialPos.getY(), initialPos.getZ());
            TeleportUtil.findSafeTeleportPosition(world, stag, 24, 40);
            world.addFreshEntity(stag);

            System.out.println("Stag placed at: " + pos);

            ((MysticTearTileEntity)te).setFaestag(stag);
        }
    }*/

    /*@Override
    public void onRemove(BlockState state, World world, BlockPos pos,
                         BlockState newState, boolean moving) {

        if (state.getBlock() == newState.getBlock()) return;
        if (world.isClientSide) return;

        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof MysticTearTileEntity) {
            FaestagEntity faestag = ((MysticTearTileEntity)te).getFaestag();
            if (faestag != null && faestag.isAlive()) {
                faestag.remove();
            }
        }

        super.onRemove(state, world, pos, newState, moving);
    }*/


}
