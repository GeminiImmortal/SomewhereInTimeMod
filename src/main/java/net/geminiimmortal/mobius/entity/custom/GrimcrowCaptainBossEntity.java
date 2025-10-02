package net.geminiimmortal.mobius.entity.custom;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class GrimcrowCaptainBossEntity extends MonsterEntity {
    private int attackPhaseTicks = 0; // counts phase duration
    private boolean chargingAOE = false;
    private Vector3d aoeTarget = null; // where player was when AOE started
    private boolean performingSlam = false;
    private int slamAirTicks = 0;

    // Synced state flags for animations
    private static final DataParameter<Integer> ATTACK_PHASE =
            EntityDataManager.defineId(GrimcrowCaptainBossEntity.class, DataSerializers.INT);
// 0 = idle, 1 = strikes, 2 = windup, 3 = leap, 4 = slam impact

    public int getAttackPhase() {
        return this.entityData.get(ATTACK_PHASE);
    }

    public void setAttackPhase(int phase) {
        this.entityData.set(ATTACK_PHASE, phase);
    }


    public GrimcrowCaptainBossEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        // We’ll manually handle strike/aoe phases in tick() instead of normal goals
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level.isClientSide) return;

        attackPhaseTicks++;

        if (!chargingAOE && !performingSlam) {
            // ===== STRIKES PHASE =====
            this.setAttackPhase(1);
            if (attackPhaseTicks < 120) {
                performRelentlessStrikes();
            } else {
                // Enter windup
                chargingAOE = true;
                attackPhaseTicks = 0;
                this.setAttackPhase(2);
                if (this.getTarget() != null) {
                    aoeTarget = this.getTarget().position();
                }
                this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 1.5F, 0.8F);

                // particles to telegraph charge
                spawnChargeParticles();
            }
        } else if (chargingAOE && !performingSlam) {
            // ===== WINDUP =====
            this.setAttackPhase(2);
            if (attackPhaseTicks >= 40) { // 2s charge
                performLeapSlam();
                chargingAOE = false;
                attackPhaseTicks = 0;
            }
        }

        if (performingSlam) {
            this.setAttackPhase(3);
            slamAirTicks++;

            if (slamAirTicks >= 20 || this.isOnGround()) {
                slamImpact();
                performingSlam = false;
                slamAirTicks = 0;
                this.setAttackPhase(4); // impact animation
            }
        }
    }

    private void spawnChargeParticles() {
        for (int i = 0; i < 10; i++) {
            double offsetX = (this.random.nextDouble() - 0.5D) * 2.0D;
            double offsetZ = (this.random.nextDouble() - 0.5D) * 2.0D;
            double x = this.getX() + offsetX;
            double y = this.getY() + 0.1D;
            double z = this.getZ() + offsetZ;

            this.level.addParticle(ParticleTypes.PORTAL, x, y, z, 0, 0.1D, 0);
        }
    }


    private void slamImpact() {
        if (aoeTarget == null) return;

        AxisAlignedBB area = new AxisAlignedBB(
                aoeTarget.x - 4, aoeTarget.y - 1, aoeTarget.z - 4,
                aoeTarget.x + 4, aoeTarget.y + 3, aoeTarget.z + 4
        );

        this.playSound(SoundEvents.ANVIL_LAND, 2.0F, 0.5F);

        for (LivingEntity e : this.level.getEntitiesOfClass(LivingEntity.class, area)) {
            if (e instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) e;
                if (player.isBlocking()) {
                    player.hurt(DamageSource.mobAttack(this), 3.0F);
                } else {
                    player.hurt(DamageSource.mobAttack(this), 25.0F);
                }
            }
        }
    }


    private void performRelentlessStrikes() {
        LivingEntity target = this.getTarget();
        if (target == null) return;

        if (this.onGround && this.distanceTo(target) > 2.0D) {
            // Short aggressive leap toward target
            Vector3d vec = new Vector3d(
                    target.getX() - this.getX(),
                    0.0D,
                    target.getZ() - this.getZ()
            );
            if (vec.lengthSqr() > 1.0E-7D) {
                vec = vec.normalize().scale(0.9D);
                this.setDeltaMovement(vec.x, 0.5D, vec.z);
            }
        }

        if (this.distanceTo(target) < 2.5D) {
            doHurtTarget(target);
        }
    }

    private void performLeapSlam() {
        if (aoeTarget == null) return;

        // Boss leaps into the air
        this.setDeltaMovement(0, 1.0D, 0);
        this.performingSlam = true;
        this.slamAirTicks = 0;

        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.5F, 0.8F);
    }

}

