package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.BoneWolfEntity;
import net.geminiimmortal.mobius.entity.custom.FaedeerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.List;

public class FaedeerRunGoal extends Goal {
    private final FaedeerEntity deer;
    private final double fleeSpeed;
    private final double spookRadius;
    private PlayerEntity player;
    private BoneWolfEntity boneWolf;
    private LivingEntity runningFrom;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected boolean isRunning;

    public FaedeerRunGoal(FaedeerEntity deer, double spookRadius, double fleeSpeed) {
        this.deer = deer;
        this.spookRadius = spookRadius;
        this.fleeSpeed = fleeSpeed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        List<LivingEntity> threats = this.deer.level.getEntitiesOfClass(LivingEntity.class,
                this.deer.getBoundingBox().inflate(spookRadius),
                e -> (e instanceof PlayerEntity && !((PlayerEntity) e).isCreative() && !((PlayerEntity) e).isSpectator())
                        || e instanceof BoneWolfEntity);

        if (!threats.isEmpty()) {
            this.runningFrom = threats.get(0);
            return true;
        }

        return false;
    }


    @Override
    public void start() {
        Vector3d fleeDirection = this.deer.position().subtract(this.runningFrom.position()).normalize().scale(10);
        BlockPos fleeTo = new BlockPos(this.deer.getX() + fleeDirection.x, this.deer.getY(), this.deer.getZ() + fleeDirection.z);
        this.deer.getNavigation().moveTo(fleeTo.getX(), fleeTo.getY(), fleeTo.getZ(), 1.5D);
        this.deer.setFleeing(true);
    }


    @Override
    public void stop() {
        this.player = null;
        this.boneWolf = null;
        this.deer.setFleeing(false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.deer.distanceTo(this.runningFrom) < spookRadius * 8) {
            moveAway();
        }

        stop();
    }


    @Override
    public boolean canContinueToUse() {
        return this.deer.distanceTo(this.runningFrom) < (spookRadius * 8);
    }

    private void moveAway() {
        double dx = this.deer.getX() - this.runningFrom.getX();
        double dz = this.deer.getZ() - this.runningFrom.getZ();
        double distance = MathHelper.sqrt(dx * dx + dz * dz);

        if (distance > 0) {
                // Calculate the yaw based on the direction to the player, and clamp it
            float targetYaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI)) - 90.0F;
            this.deer.setYBodyRot(MathHelper.clamp(targetYaw, -180.0F, 180.0F));
            this.deer.setYHeadRot(MathHelper.clamp(targetYaw, -180.0F, 180.0F));
        }
        this.deer.getMoveControl().setWantedPosition(this.deer.getX() + dx * 6, this.deer.getY(), this.deer.getZ() + dz * 6, this.fleeSpeed);
    }

}
