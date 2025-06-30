package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.faction.FactionType;
import net.geminiimmortal.mobius.faction.IFactionCarrier;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public abstract class AbstractImperialEntity extends CreatureEntity implements IFactionCarrier {
    protected AbstractImperialEntity(EntityType<? extends CreatureEntity> entityType, World world) {
        super(entityType, world);
        this.setPersistenceRequired();
    }

    public FactionType getFaction() {
        return FactionType.IMPERIAL;
    }

    protected void registerGoals() {
        this.targetSelector.addGoal(10, new HurtByTargetGoal(this, PlayerEntity.class).setAlertOthers(AbstractImperialEntity.class));
    }
}
