package net.geminiimmortal.mobius.entity.goals.util;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

public class HybridFlightController extends MovementController {
    private final MobEntity mob;
    private Vector3d lastPos = Vector3d.ZERO;
    private int stuckTicks = 0;

    public HybridFlightController(MobEntity mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public void tick() {
        if (this.operation != Action.MOVE_TO) return;

        Vector3d currentPos = Vector3d.atCenterOf(mob.blockPosition());
        double dx = this.wantedX - currentPos.x;
        double dy = this.wantedY - currentPos.y;
        double dz = this.wantedZ - currentPos.z;
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance < mob.getBoundingBox().getSize()) {
            this.operation = Action.WAIT;
            mob.setDeltaMovement(mob.getDeltaMovement().scale(0.5));
            return;
        }

        // Detect if the mob is stuck (barely moving)
        if (currentPos.distanceTo(lastPos) < 0.05D) {
            stuckTicks++;
            if (stuckTicks > 20) {
                // Try to find a nearby open area
                Vector3d escape = findEscapeVector(mob);
                mob.getNavigation().moveTo(escape.x, escape.y, escape.z, 1.0D);
                stuckTicks = 0;
            }
        } else {
            stuckTicks = 0;
        }

        lastPos = currentPos;

        // Smooth vex-style motion
        double speed = this.speedModifier * mob.getAttribute(Attributes.FLYING_SPEED).getValue();
        mob.setDeltaMovement(mob.getDeltaMovement().add(
                dx / distance * 0.05D * speed,
                dy / distance * 0.05D * speed,
                dz / distance * 0.05D * speed
        ));
    }

    private Vector3d findEscapeVector(MobEntity mob) {
        World world = mob.level;
        Random rand = mob.getRandom();
        for (int i = 0; i < 8; i++) {
            double x = mob.getX() + (rand.nextDouble() - 0.5D) * 6.0D;
            double y = mob.getY() + (rand.nextDouble() - 0.5D) * 4.0D;
            double z = mob.getZ() + (rand.nextDouble() - 0.5D) * 6.0D;
            BlockPos pos = new BlockPos(x, y, z);
            if (world.getBlockState(pos).isAir()) {
                return new Vector3d(x, y, z); }
        }
        return mob.getDeltaMovement().add(0, 2, 0);
    }
}

