package net.geminiimmortal.mobius.block.custom.boss_blocks;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.util.TitleUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class GovernorBossBlock extends Block {
    public GovernorBossBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide && hand == Hand.MAIN_HAND) {
            int offsetX = 0;
            int offsetY = 5;
            int offsetZ = -20;

            double targetX = pos.getX() + offsetX;
            double targetY = pos.getY() + offsetY;
            double targetZ = pos.getZ() + offsetZ;

            double bossX = pos.getX();
            double bossY = pos.getY() + offsetY;
            double bossZ = pos.getZ();

            BlockPos validPos = findTeleportPosition(new BlockPos(bossX, bossY, bossZ), world);

            player.teleportTo(validPos.getX() + 0.5, validPos.getY(), validPos.getZ() + 0.5);

            EntityType<?> bossEntityType = ModEntityTypes.GOVERNOR.get();
            Entity bossEntity = bossEntityType.create(world);
            if (bossEntity != null) {
                bossEntity.moveTo(bossX, bossY, bossZ, 0.0F, 0.0F);
                world.addFreshEntity(bossEntity);
                world.playSound(null, targetX, targetY, targetZ, SoundEvents.WITHER_SPAWN, SoundCategory.HOSTILE, 1.0F, 1.0F);
            }
            TitleUtils.sendTitle((ServerPlayerEntity) player, "Duty Commenced!", null, 10, 40, 40, TextFormatting.GOLD);
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    public BlockPos findTeleportPosition(BlockPos bossSpawn, World world) {
        int offset = 25;
        List<BlockPos> candidatePositions = Arrays.asList(
                new BlockPos(bossSpawn.getX() + offset, bossSpawn.getY(), bossSpawn.getZ()),
                new BlockPos(bossSpawn.getX(),bossSpawn.getY(),bossSpawn.getZ() + offset),
                new BlockPos(bossSpawn.getX() - offset, bossSpawn.getY(), bossSpawn.getZ()),
                new BlockPos(bossSpawn.getX(), bossSpawn.getY(),bossSpawn.getZ() - offset)
        );

        for (BlockPos pos : candidatePositions) {
            if (isSafePosition(pos, bossSpawn, world)) {
                return pos;
            }
        }

        return bossSpawn;
    }

    private boolean isSafePosition(BlockPos pos, BlockPos bossPos, World world) {
        if (pos.equals(bossPos)) return false;

        BlockState blockBelow = world.getBlockState(pos.below());
        BlockState blockAbove = world.getBlockState(pos.above());

        return (!blockBelow.isAir() && blockAbove.isAir());
    }
}
