package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.goals.HealInjuredAllyGoal;
import net.geminiimmortal.mobius.faction.FactionType;
import net.geminiimmortal.mobius.faction.IFactionCarrier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class HeartGolemEntity extends IronGolemEntity implements IFactionCarrier {

    private LivingEntity targetAlly;
    private static final DataParameter<Boolean> HEALING = EntityDataManager.defineId(HeartGolemEntity.class, DataSerializers.BOOLEAN);


    public HeartGolemEntity(EntityType<? extends IronGolemEntity> type, World worldIn) {
        super(type, worldIn);
        this.dropExperience();

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HEALING, false);
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new HealInjuredAllyGoal(this, this, 0.35, 10F, 6, 100));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
    }

    public FactionType getFaction() {
        return FactionType.IMPERIAL;
    }

    protected int getXpToDrop() {
        int baseXp = this.random.nextInt(7) + 5; // Base XP drop
        int difficultyMultiplier = this.level.getDifficulty().getId(); // 0 = Peaceful, 1 = Easy, 2 = Normal, 3 = Hard
        return baseXp + difficultyMultiplier * 2;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        // Check if the world is not remote (i.e., server-side)
        if (!this.level.isClientSide()) {
            // Define the amount of experience the golem should drop
            int experiencePoints = this.getXpToDrop();

            // Drop the experience orbs
            while (experiencePoints > 0) {
                int experienceToDrop = experiencePoints;
                experiencePoints -= experienceToDrop;
                this.level.addFreshEntity(new ExperienceOrbEntity(this.level, this.getX(), this.getY(), this.getZ(), experienceToDrop));
            }
        }
    }





    // Call this method when the golem starts healing
    public void setHealing(boolean healing) {
        this.entityData.set(HEALING, healing);
    }


    public boolean isHealing() {
        return this.entityData.get(HEALING);
    }

    @Override
    public void tick() {
        super.tick();

    }

}

