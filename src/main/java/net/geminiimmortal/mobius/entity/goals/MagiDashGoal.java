package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.SorcererEntity;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class MagiDashGoal extends Goal {
    SorcererEntity boss;
    private int cooldown = 0;
    private final int cooldownTimer = 80;

    public MagiDashGoal(SorcererEntity boss) {
        this.boss = boss;
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
        }
        return boss.getTarget() != null && boss.getHealth() <= boss.getMaxHealth() * 0.33 && cooldown <= 0;
    }

    @Override
    public void start() {
        LivingEntity target = boss.getTarget();
        if (target != null) {
            boss.setNoGravity(false);

            if (boss.getTarget() != null) {
                double dx = boss.getTarget().getX() - boss.getX();
                double dz = boss.getTarget().getZ() - boss.getZ();

                float currentYaw = boss.getRotationVector().y;
                float targetYaw = (float) (Math.atan2(dz, dx) * (180 / Math.PI)) - 90.0F;
                //    float newYaw = MathHelper.lerp(0.1F, currentYaw, targetYaw); // Gradually rotate
                boss.setYya(targetYaw);
                boss.yBodyRot = targetYaw;
                boss.yHeadRot = targetYaw;
            }

            boss.setDashing(true);
            Vector3d dashDirection = new Vector3d(target.getX() - boss.getX(), 0, target.getZ() - boss.getZ()).normalize();
            boss.setDeltaMovement(dashDirection.x * 3, 0.3, dashDirection.z * 3);
            boss.level.playSound(null, boss.blockPosition(), ModSounds.DASH_IMPACT.get(), SoundCategory.HOSTILE, 10.0F, 1.0F);
        }
    }

    @Override
    public void stop() {
        createShockwave();
        cooldown = cooldownTimer;
        boss.setDashing(false);
    }

    private void createShockwave() {
        World world = boss.level;
        BlockPos pos = boss.blockPosition();
        world.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundCategory.HOSTILE, 10.0F, 1.0F);

        for (Entity entity : world.getEntities(boss, new AxisAlignedBB(pos).inflate(5), e -> e instanceof LivingEntity)) {
            LivingEntity target = (LivingEntity) entity;
            if (!target.isBlocking()) {
                target.hurt(DamageSource.mobAttack(boss), 10.0F); // Example damage value
            }
        }
    }
}
