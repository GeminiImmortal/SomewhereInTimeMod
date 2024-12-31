package net.geminiimmortal.mobius.block.custom.boss_blocks;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.sound.ClientMusicHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class GovernorBossBlock extends Block {
    public GovernorBossBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide && hand == Hand.MAIN_HAND) {
            Minecraft minecraft = Minecraft.getInstance();
            int offsetX = 0;
            int offsetY = 7;
            int offsetZ = 0;

            double targetX = pos.getX() + offsetX;
            double targetY = pos.getY() + offsetY;
            double targetZ = pos.getZ() + offsetZ;

            player.teleportTo(targetX, targetY, targetZ);

            EntityType<?> bossEntityType = ModEntityTypes.GOVERNOR.get();
            Entity bossEntity = bossEntityType.create(world);
            if (bossEntity != null) {
                bossEntity.moveTo(targetX, targetY, targetZ, 0.0F, 0.0F); // Set position and rotation
                world.addFreshEntity(bossEntity); // Add the boss to the world

                // Notify the player or world
                world.playSound(null, targetX, targetY, targetZ, SoundEvents.WITHER_SPAWN, SoundCategory.HOSTILE, 1.0F, 1.0F);
                player.sendMessage(new StringTextComponent("The Governor challenges you to a duel!"), player.getUUID());
            }

            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }
}
