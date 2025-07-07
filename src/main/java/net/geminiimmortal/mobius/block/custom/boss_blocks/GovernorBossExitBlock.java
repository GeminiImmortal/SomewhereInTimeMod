package net.geminiimmortal.mobius.block.custom.boss_blocks;

import net.geminiimmortal.mobius.world.data.MobiusDataManager;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;

import static java.lang.reflect.AccessibleObject.setAccessible;

public class GovernorBossExitBlock extends Block {
    public GovernorBossExitBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide && hand == Hand.MAIN_HAND) {
            int offsetX = 0;
            int offsetY = -10;
            int offsetZ = 0;

            double targetX = pos.getX() + offsetX;
            double targetY = pos.getY() + offsetY;
            double targetZ = pos.getZ() + offsetZ;

            player.teleportTo(targetX, targetY, targetZ);
            player.addEffect(new EffectInstance(Effects.SLOW_FALLING, 60));

            world.playSound(null, targetX, targetY, targetZ, SoundEvents.ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F);

            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            MobiusDataManager data = MobiusDataManager.get((ServerWorld) world, ModDimensions.MOBIUS_WORLD);
            data.setGovernorTowerDefeated(true);

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }
}
