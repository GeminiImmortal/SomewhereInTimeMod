package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.faction.FactionType;
import net.geminiimmortal.mobius.faction.IFactionCarrier;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public abstract class AbstractRebelEntity extends CreatureEntity implements IFactionCarrier {
    private static final DataParameter<Boolean> IS_PART_OF_SKIRMISH = EntityDataManager.defineId(AbstractRebelEntity.class, DataSerializers.BOOLEAN);

    protected AbstractRebelEntity(EntityType<? extends CreatureEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(IS_PART_OF_SKIRMISH, false);
    }

    public void setIsPartOfSkirmish(boolean isPartOfSkirmish) {
        this.entityData.set(IS_PART_OF_SKIRMISH, isPartOfSkirmish);
    }


    public boolean isPartOfSkirmish() {
        return this.entityData.get(IS_PART_OF_SKIRMISH);
    }

    public FactionType getFaction() {
        return FactionType.REBEL;
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            if (this.getTarget() instanceof ServerPlayerEntity) {
                this.setTarget(null);
            }
        }
    }

    protected void registerGoals() {
        this.targetSelector.addGoal(9, (new HurtByTargetGoal(this, PlayerEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(10, new NearestAttackableTargetGoal<>(this, AbstractImperialEntity.class,true, false));
    }
}
