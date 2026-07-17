package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.faction.FactionType;
import net.geminiimmortal.mobius.faction.IFactionCarrier;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class AbstractGrimcrowEntity extends MonsterEntity implements IMob, IFactionCarrier {
    @Nullable
    private BlockPos boundOrigin;
    protected static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(AbstractGrimcrowEntity.class, DataSerializers.BYTE);

    protected AbstractGrimcrowEntity(EntityType<? extends MonsterEntity> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new ChargeAttackGoal());
        this.goalSelector.addGoal(2, new MoveRandomGoal());
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static boolean canMobSpawn(EntityType<? extends AbstractGrimcrowEntity> entityType,
                                      IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
        ServerWorld level = world.getLevel();

        int existing = level.getEntitiesOfClass(AbstractGrimcrowEntity.class,
                new AxisAlignedBB(pos).inflate(16)).size();

        int bossExists = level.getEntitiesOfClass(GrimcrowCaptainBossEntity.class,
                new AxisAlignedBB(pos).inflate(100)).size();

        BlockState ground = level.getBlockState(pos.below());
        boolean validGround = ground.is(Blocks.AIR);

        return validGround && existing < 3 && bossExists < 1;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    @Override
    public FactionType getFaction() {
        return FactionType.GRIMCROW;
    }

    protected boolean getFlag(int p_190656_1_) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        return (i & p_190656_1_) != 0;
    }

    protected void setFlag(int p_190660_1_, boolean p_190660_2_) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        if (p_190660_2_) {
            i = i | p_190660_1_;
        } else {
            i = i & ~p_190660_1_;
        }

        this.entityData.set(DATA_FLAGS_ID, (byte)(i & 255));
    }

    public boolean isCharging() {
        return this.getFlag(1);
    }

    public void setIsCharging(boolean charging) {
        this.setFlag(1, charging);
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos p_190651_1_) {
        this.boundOrigin = p_190651_1_;
    }


    public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        if (p_70037_1_.contains("BoundX")) {
            this.boundOrigin = new BlockPos(p_70037_1_.getInt("BoundX"), p_70037_1_.getInt("BoundY"), p_70037_1_.getInt("BoundZ"));
        }
    }

    public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
        if (this.boundOrigin != null) {
            p_213281_1_.putInt("BoundX", this.boundOrigin.getX());
            p_213281_1_.putInt("BoundY", this.boundOrigin.getY());
            p_213281_1_.putInt("BoundZ", this.boundOrigin.getZ());
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.VINDICATOR_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.equals(DamageSource.FALL)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    class ChargeAttackGoal extends Goal {
        public ChargeAttackGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            return AbstractGrimcrowEntity.this.getTarget() != null && !AbstractGrimcrowEntity.this.getMoveControl().hasWanted() && AbstractGrimcrowEntity.this.random.nextInt(3) == 0;

        }

        public boolean canContinueToUse() {
            return AbstractGrimcrowEntity.this.moveControl.hasWanted() && AbstractGrimcrowEntity.this.isCharging() && AbstractGrimcrowEntity.this.getTarget() != null && AbstractGrimcrowEntity.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = AbstractGrimcrowEntity.this.getTarget();
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            AbstractGrimcrowEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            AbstractGrimcrowEntity.this.setIsCharging(true);
        }

        public void stop() {
            AbstractGrimcrowEntity.this.setIsCharging(false);
        }

        public void tick() {
            LivingEntity livingentity = AbstractGrimcrowEntity.this.getTarget();
            if (AbstractGrimcrowEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                AbstractGrimcrowEntity.this.doHurtTarget(livingentity);
                AbstractGrimcrowEntity.this.setIsCharging(false);
            } else {
                double d0 = AbstractGrimcrowEntity.this.distanceToSqr(livingentity);
                if (d0 < 9.0D) {
                    Vector3d vector3d = livingentity.getEyePosition(1.0F);
                    AbstractGrimcrowEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                }
            }

        }
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(AbstractGrimcrowEntity grimcrow) {
            super(grimcrow);
        }

        public void tick() {
            if (this.operation == MovementController.Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.wantedX - AbstractGrimcrowEntity.this.getX(), this.wantedY - AbstractGrimcrowEntity.this.getY(), this.wantedZ - AbstractGrimcrowEntity.this.getZ());
                double d0 = vector3d.length();
                if (AbstractGrimcrowEntity.this.isOnGround()) {
                    AbstractGrimcrowEntity.this.moveControl.setWantedPosition(AbstractGrimcrowEntity.this.getX(), AbstractGrimcrowEntity.this.getY() + 0.5, AbstractGrimcrowEntity.this.getZ(), 1.0D);
                }
                if (d0 < AbstractGrimcrowEntity.this.getBoundingBox().getSize()) {
                    this.operation = MovementController.Action.WAIT;
                    AbstractGrimcrowEntity.this.setDeltaMovement(AbstractGrimcrowEntity.this.getDeltaMovement().scale(0.75D));
                } else {
                    AbstractGrimcrowEntity.this.setDeltaMovement(AbstractGrimcrowEntity.this.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                    if (AbstractGrimcrowEntity.this.getTarget() == null) {
                        Vector3d vector3d1 = AbstractGrimcrowEntity.this.getDeltaMovement();
                        AbstractGrimcrowEntity.this.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                        AbstractGrimcrowEntity.this.yBodyRot = AbstractGrimcrowEntity.this.yRot;
                    } else {
                        double d2 = AbstractGrimcrowEntity.this.getTarget().getX() - AbstractGrimcrowEntity.this.getX();
                        double d1 = AbstractGrimcrowEntity.this.getTarget().getZ() - AbstractGrimcrowEntity.this.getZ();
                        AbstractGrimcrowEntity.this.yRot = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                        AbstractGrimcrowEntity.this.yBodyRot = AbstractGrimcrowEntity.this.yRot;
                    }
                }

            }
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            return !AbstractGrimcrowEntity.this.moveControl.hasWanted() && AbstractGrimcrowEntity.this.random.nextInt(7) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = AbstractGrimcrowEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = AbstractGrimcrowEntity.this.blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(AbstractGrimcrowEntity.this.random.nextInt(15) - 7, AbstractGrimcrowEntity.this.random.nextInt(11) - 5, AbstractGrimcrowEntity.this.random.nextInt(15) - 7);
                if (AbstractGrimcrowEntity.this.level.isEmptyBlock(blockpos1)) {
                    AbstractGrimcrowEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (AbstractGrimcrowEntity.this.getTarget() == null) {
                        AbstractGrimcrowEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }
}
