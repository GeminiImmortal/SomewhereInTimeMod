package net.geminiimmortal.mobius.entity.goals.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

public class TeleportUtil {

    /**
     * Attempts to find a safe location on the same Y level as the entity.
     *
     * @param world The world the entity is in
     * @param entity The entity to teleport
     * @param radius The horizontal radius to search within
     * @param attempts Number of attempts to find a valid location
     * @return A valid Vector3d position or null if none found
     */
    public static Vector3d findSafeTeleportPosition(World world, Entity entity, int radius, int attempts) {
        Random rand = new Random();
        double y = entity.getY(); // Keep the same Y level
        BlockPos.Mutable checkPos = new BlockPos.Mutable();

        for (int i = 0; i < attempts; i++) {
            double offsetX = (rand.nextDouble() * 2 - 1) * radius;
            double offsetZ = (rand.nextDouble() * 2 - 1) * radius;

            double x = entity.getX() + offsetX;
            double z = entity.getZ() + offsetZ;

            checkPos.setX((int) x);
            checkPos.setY((int) y - 1);
            checkPos.setZ((int) z);
            // Make sure there's solid ground and enough space
            if (world.getBlockState(checkPos).isSolidRender(world, checkPos)) {
                BlockPos possiblePosCheck1 = checkPos.above(1);
                BlockPos possiblePosCheck2 = checkPos.above(2);
                BlockPos possiblePosCheck3 = checkPos.above(3);
                if (!world.getBlockState(possiblePosCheck1).isSolidRender(world, possiblePosCheck1) && !world.getBlockState(possiblePosCheck2).isSolidRender(world, possiblePosCheck2) && !world.getBlockState(possiblePosCheck3).isSolidRender(world, possiblePosCheck3)) {
                    return new Vector3d(x + 0.5, y, z + 0.5); // center entity on block
                }
            }
        }

        return null; // No safe location found
    }
}

