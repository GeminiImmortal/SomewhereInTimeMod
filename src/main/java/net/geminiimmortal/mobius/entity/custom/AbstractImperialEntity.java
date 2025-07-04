package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.capability.infamy.IInfamy;
import net.geminiimmortal.mobius.entity.goals.ImperialFollowPatrolLeaderGoal;
import net.geminiimmortal.mobius.entity.goals.ImperialOfficerLeadPatrolGoal;
import net.geminiimmortal.mobius.entity.goals.target.TargetCriminalPlayerGoal;
import net.geminiimmortal.mobius.event.ImperialReinforcementHandler;
import net.geminiimmortal.mobius.faction.IRankedImperial;
import net.geminiimmortal.mobius.faction.FactionType;
import net.geminiimmortal.mobius.faction.IFactionCarrier;
import net.geminiimmortal.mobius.util.InfamyHelper;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractImperialEntity extends CreatureEntity implements IFactionCarrier, IRankedImperial, IAngerable {
    private static final DataParameter<Optional<UUID>> PATROL_LEADER =
            EntityDataManager.defineId(AbstractImperialEntity.class, DataSerializers.OPTIONAL_UUID);
    private static final DataParameter<Integer> DATA_REMAINING_ANGER_TIME = EntityDataManager.defineId(AbstractImperialEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> IS_PART_OF_PATROL = EntityDataManager.defineId(AbstractImperialEntity.class, DataSerializers.BOOLEAN);

    private static final RangedInteger PERSISTENT_ANGER_TIME = TickRangeConverter.rangeOfSeconds(20, 39);
    private UUID persistentAngerTarget;

    @Nullable
    private UUID patrolLeader;
    private boolean inPatrol;


    protected AbstractImperialEntity(EntityType<? extends CreatureEntity> entityType, World world) {
        super(entityType, world);
        this.setPersistenceRequired();
    }

    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(PATROL_LEADER, Optional.empty());
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
        this.entityData.define(IS_PART_OF_PATROL, false);
    }

    public void setIsPartOfPatrol(boolean isPartOfPatrol) {
        this.entityData.set(IS_PART_OF_PATROL, isPartOfPatrol);
        this.inPatrol = isPatrolMember();
    }



    public boolean isPatrolMember() {
        return this.entityData.get(IS_PART_OF_PATROL);
    }

    IRankedImperial.Rank rank;

    public IRankedImperial.Rank getRank() {
        return rank;
    }

    public FactionType getFaction() {
        return FactionType.IMPERIAL;
    }

    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    public void setRemainingPersistentAngerTime(int p_230260_1_) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, p_230260_1_);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.randomValue(this.random));
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerWorld)this.level, true);
        }
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        ServerPlayerEntity player;
        if (this.getTarget() != null) {
            if (this.getTarget().getClass().equals(ServerPlayerEntity.class)) {
                player = (ServerPlayerEntity) this.getTarget();
                if (InfamyHelper.get(player).getInfamyTier().ordinal() >= IInfamy.InfamyTier.NOTICED.ordinal()) {
                    return this.persistentAngerTarget;
                }
            }
        }
        return this.persistentAngerTarget;
    }

    public void setPersistentAngerTarget(@Nullable UUID p_230259_1_) {
        this.persistentAngerTarget = p_230259_1_;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(19, new WaterAvoidingRandomWalkingGoal(this, 0.95D));
        this.goalSelector.addGoal(20, new ImperialOfficerLeadPatrolGoal(this, 0.7D));
        this.goalSelector.addGoal(20, new ImperialFollowPatrolLeaderGoal(this, 0.9D));
        this.targetSelector.addGoal(9, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(10, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(11, new NearestAttackableTargetGoal<>(this, AbstractRebelEntity.class, true, false));
        this.targetSelector.addGoal(12, new TargetCriminalPlayerGoal(this));
    }


    public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
        this.addPersistentAngerSaveData(p_213281_1_);
        this.getPatrolLeader().ifPresent(uuid -> p_213281_1_.putUUID("PatrolLeader", uuid));
        p_213281_1_.putBoolean("InPatrol", this.isInPatrol());
    }

    public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);

        if (!level.isClientSide) {
            this.readPersistentAngerSaveData((ServerWorld) this.level, p_70037_1_);
        }

        if (p_70037_1_.hasUUID("PatrolLeader")) {
            this.setPatrolLeader(Optional.of(p_70037_1_.getUUID("PatrolLeader")));
        } else {
            this.setPatrolLeader(Optional.empty());
        }

        this.setInPatrol(p_70037_1_.getBoolean("InPatrol"));
    }

    public void setPatrolLeader(Optional<UUID> uuid) {
        this.entityData.set(PATROL_LEADER, uuid);
    }

    public Optional<UUID> getPatrolLeader() {
        return this.entityData.get(PATROL_LEADER);
    }

    @Nullable
    public LivingEntity getLeaderEntity(ServerWorld world) {
        return getPatrolLeader()
                .map(world::getEntity)
                .filter(e -> e instanceof LivingEntity)
                .map(e -> (LivingEntity) e)
                .orElse(null);
    }

    public void setInPatrol(boolean inPatrol) {
        this.entityData.set(IS_PART_OF_PATROL, inPatrol);
    }

    public boolean isInPatrol() {
        return this.entityData.get(IS_PART_OF_PATROL);
    }


    public void givePlayerInfamyOnDeath(DamageSource source, int amount) {
        if (source.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) source.getEntity();
            InfamyHelper.get(serverPlayer).addInfamy(amount);
            InfamyHelper.sync(serverPlayer);
        }
    }

    public void callForBackup(DamageSource source) {
        if (this.getRank() == Rank.GRUNT && !this.level.isClientSide() && source.getEntity() instanceof ServerPlayerEntity && this.isPatrolMember()) {
            this.level.getEntitiesOfClass(
                    AbstractImperialEntity.class,
                    this.getBoundingBox().inflate(32),
                    e -> e.getRank().equals(Rank.OFFICER) && e.isAlive()
            ).stream().findFirst().ifPresent(ImperialReinforcementHandler::queueReinforcements);
        }
    }
}
