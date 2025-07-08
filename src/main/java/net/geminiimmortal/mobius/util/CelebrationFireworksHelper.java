package net.geminiimmortal.mobius.util;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class CelebrationFireworksHelper {
    public static void spawnCelebrationFireworks(ServerWorld world, BlockPos pos) {
        Random random = world.random;

        // Repeat a few times for multiple fireworks
        for (int i = 0; i < 5; i++) {
            // Random offset around the position
            double offsetX = pos.getX() + 0.5 + random.nextGaussian();
            double offsetY = pos.getY() + 1 + random.nextDouble();
            double offsetZ = pos.getZ() + 0.5 + random.nextGaussian();

            // Create firework NBT tag
            CompoundNBT fireworkTag = new CompoundNBT();
            ListNBT explosions = new ListNBT();

            CompoundNBT explosion = new CompoundNBT();
            explosion.putByte("Type", (byte) random.nextInt(5)); // 0–4: Small Ball, Large Ball, Star, Creeper, Burst
            explosion.putBoolean("Flicker", true);
            explosion.putBoolean("Trail", true);
            explosion.putIntArray("Colors", new int[] { DyeColor.RED.getFireworkColor(), DyeColor.YELLOW.getFireworkColor() });

            explosions.add(explosion);

            CompoundNBT fireworks = new CompoundNBT();
            fireworks.putByte("Flight", (byte) (1 + random.nextInt(2))); // Flight duration
            fireworks.put("Explosions", explosions);

            fireworkTag.put("Fireworks", fireworks);

            // Create the itemstack with NBT
            ItemStack fireworkStack = new ItemStack(Items.FIREWORK_ROCKET);
            fireworkStack.setTag(fireworkTag);

            // Create and spawn the firework entity
            FireworkRocketEntity firework = new FireworkRocketEntity(world, offsetX, offsetY, offsetZ, fireworkStack);
            world.addFreshEntity(firework);
        }
    }

}
