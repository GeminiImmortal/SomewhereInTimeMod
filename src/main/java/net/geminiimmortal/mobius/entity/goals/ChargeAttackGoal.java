package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.GuardCaptainBossEntity;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class ChargeAttackGoal extends Goal {
    private final GuardCaptainBossEntity mob;
    private LivingEntity target;


    public ChargeAttackGoal(GuardCaptainBossEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        this.target = mob.getTarget();
        return target != null && mob.position().distanceToSqr(target.position()) > 6.0D && mob.canCharge();
    }

    @Override
    public void start() {
        mob.getLookControl().setLookAt(target, 30F, 30F);
        Vector3d direction = target.getPosition(10).subtract(mob.getPosition(10)).normalize().scale(2);
        mob.setDeltaMovement(direction);
        mob.playSound(ModSounds.CAPTAIN_SCREAMS.get(), 1.0F, 1.0F);

        mob.setChargeCooldown(200);
    }

    @Override
    public void tick() {
        if (mob.getBoundingBox().inflate(4).intersects(target.getBoundingBox())) {
            target.hurt(DamageSource.thorns(mob), 8.0F);
            target.knockback(2F, mob.getX() - target.getX(), mob.getZ() - target.getZ());
            mob.playSound(ModSounds.CAPTAIN_ULTI.get(), 1.0F, 1.0F);
            mob.spawnCleaveParticles();
        }

        if(mob.level.isClientSide()){
            Vector3d behind = mob.getLookAngle().subtract(mob.getLookAngle().scale(0.5));
            mob.level.addParticle(ParticleTypes.CLOUD,
                    behind.x,
                    behind.y + 0.1,
                    behind.z,
                    0, 0, 0);
        }

        if (mob.level instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) mob.level;

            Vector3d behind = mob.getLookAngle().subtract(mob.getLookAngle().scale(0.5));
            serverWorld.sendParticles(
                    ParticleTypes.CLOUD,
                    behind.x,
                    behind.y + 0.1,
                    behind.z,
                    2,
                    0.1, 0.0, 0.1,
                    0.01
            );
        }



        AxisAlignedBB hitbox = mob.getBoundingBox().inflate(1.0, 0.5, 1.0).expandTowards(mob.getLookAngle());
        List<LivingEntity> hitEntities = mob.level.getEntitiesOfClass(LivingEntity.class, hitbox,
                e -> e != mob && e.isAlive() && !e.isSpectator());

        for (LivingEntity entity : hitEntities) {
            entity.hurt(DamageSource.thorns(mob), 8.0f); // Charge damage
            target.knockback(2F, mob.getX() - target.getX(), mob.getZ() - target.getZ());
            this.stop();
            break;
        }



    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

}

