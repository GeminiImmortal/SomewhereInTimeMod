package net.geminiimmortal.mobius.entity.goals;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Random;

public class TeleportAwayGoal extends Goal {
    private final MobEntity boss;
    private final double minDistance; // Minimum distance to trigger teleportation
    private final int roomX1, roomY1, roomZ1; // Room corner 1
    private final int roomX2, roomY2, roomZ2; // Room corner 2
    private final Random random = new Random();

    public TeleportAwayGoal(MobEntity boss, double minDistance, int roomX1, int roomY1, int roomZ1, int roomX2, int roomY2, int roomZ2) {
        this.boss = boss;
        this.minDistance = minDistance;
        this.roomX1 = roomX1;
        this.roomY1 = roomY1;
        this.roomZ1 = roomZ1;
        this.roomX2 = roomX2;
        this.roomY2 = roomY2;
        this.roomZ2 = roomZ2;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE)); // Prevent other movement goals while teleporting
    }

    @Override
    public boolean canUse() {
        // Check if any nearby player is within the minimum distance
        for (PlayerEntity player : this.boss.level.getEntitiesOfClass(PlayerEntity.class, this.boss.getBoundingBox().inflate(minDistance))) {
            if (!player.isSpectator() && !player.isCreative()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        teleportRandomly();
    }

    private void teleportRandomly() {
        World world = this.boss.level;

        for (int attempt = 0; attempt < 10; attempt++) { // Try up to 10 times to find a valid position
            int x = roomX1 + random.nextInt(roomX2 - roomX1 + 1);
            int y = roomY1 + random.nextInt(roomY2 - roomY1 + 1);
            int z = roomZ1 + random.nextInt(roomZ2 - roomZ1 + 1);

            BlockPos newPos = new BlockPos(x, y, z);

            // Check if the new position is safe
            if (world.isEmptyBlock(newPos) && world.isEmptyBlock(newPos.above())) {
                this.boss.setPos(x + 0.5, y, z + 0.5);
                world.levelEvent(2003, newPos, 0); // Play teleport particles (optional)
                break;
            }
        }
    }

    private void strikeLightning(double x, double y, double z) {
        // Create and spawn a lightning bolt entity
        LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(this.boss.level);
        if (lightning != null) {
            lightning.moveTo(x, y, z, 0.0f, 0.0f); // Set position
            this.boss.level.addFreshEntity(lightning);
        }
    }
}

