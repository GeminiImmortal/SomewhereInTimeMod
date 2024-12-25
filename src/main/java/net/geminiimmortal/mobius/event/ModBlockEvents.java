package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.MolvanEntity;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID)
public class ModBlockEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        World world = (World) event.getWorld();
        PlayerEntity player = event.getPlayer();
        BlockPos pos = event.getPos();
        BlockState blockState = event.getState();

        // Check if the broken block is a chest
        if (blockState.getBlock() instanceof ChestBlock) {
            // Find mobs in a radius around the chest
            List<CreatureEntity> mobs = world.getEntitiesOfClass(
                    MolvanEntity.class,
                    new AxisAlignedBB(pos).inflate(10.0), // 10-block radius
                    mob -> mob instanceof MolvanEntity // Replace with your mob's class
            );

            for (CreatureEntity mob : mobs) {
                if (mob.getSensing().canSee(player)) {
                    // Make the mob angry or set a target
                    mob.setTarget(player);
                    mob.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.MOLVAN_ANGRY.get(), SoundCategory.HOSTILE, 10.0f, 0.8f);
                }
            }
        }
    }
}

