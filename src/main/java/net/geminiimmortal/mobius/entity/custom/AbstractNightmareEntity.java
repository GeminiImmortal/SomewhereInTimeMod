package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.faction.FactionType;
import net.geminiimmortal.mobius.faction.IFactionCarrier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractNightmareEntity extends MonsterEntity implements IFactionCarrier {
    private static final DataParameter<Optional<UUID>> RAID_LEADER =
            EntityDataManager.defineId(AbstractNightmareEntity.class, DataSerializers.OPTIONAL_UUID);
    
    private static final DataParameter<Boolean> IS_PART_OF_RAID = EntityDataManager.defineId(AbstractNightmareEntity.class, DataSerializers.BOOLEAN);
    
    protected AbstractNightmareEntity(EntityType<? extends MonsterEntity> entityType, World world) {
        super(entityType, world);
        this.setPersistenceRequired();
        this.maxUpStep = 1;
    }

    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(RAID_LEADER, Optional.empty());
        this.entityData.define(IS_PART_OF_RAID, false);
    }

    public void setIsPartOfRaid(boolean isPartOfRaid) {
        this.entityData.set(IS_PART_OF_RAID, isPartOfRaid);
    }

    @Override
    public boolean isAggressive() {
        return true;
    }

    public boolean isRaidMember() {
        return this.entityData.get(IS_PART_OF_RAID);
    }

    @Override
    public FactionType getFaction() {
        return FactionType.NIGHTMARE;
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(19, new WaterAvoidingRandomWalkingGoal(this, 0.95D));
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractImperialEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractRebelEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
        this.getRaidLeader().ifPresent(uuid -> p_213281_1_.putUUID("RaidLeader", uuid));
        p_213281_1_.putBoolean("InRaid", this.isInRaid());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        
        if (p_70037_1_.hasUUID("RaidLeader")) {
            this.setRaidLeader(Optional.of(p_70037_1_.getUUID("RaidLeader")));
        } else {
            this.setRaidLeader(Optional.empty());
        }

        this.setInRaid(p_70037_1_.getBoolean("InRaid"));
    }

    public void setRaidLeader(Optional<UUID> uuid) {
        this.entityData.set(RAID_LEADER, uuid);
    }

    public Optional<UUID> getRaidLeader() {
        return this.entityData.get(RAID_LEADER);
    }

    @Nullable
    public LivingEntity getLeaderEntity(ServerWorld world) {
        return getRaidLeader()
                .map(world::getEntity)
                .filter(e -> e instanceof LivingEntity)
                .map(e -> (LivingEntity) e)
                .orElse(null);
    }

    public void setInRaid(boolean inRaid) {
        this.entityData.set(IS_PART_OF_RAID, inRaid);
    }

    public boolean isInRaid() {
        return this.entityData.get(IS_PART_OF_RAID);
    }
}
