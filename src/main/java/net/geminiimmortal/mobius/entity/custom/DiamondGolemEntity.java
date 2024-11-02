package net.geminiimmortal.mobius.entity.custom;


import net.geminiimmortal.mobius.entity.goals.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class DiamondGolemEntity extends IronGolemEntity {
    private int attackAnimationTimer = 0;
    private final int attackAnimationDuration = 20;

    public DiamondGolemEntity(EntityType<? extends IronGolemEntity> type, World worldIn) {
        super(type, worldIn);
        this.dropExperience();
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.ARMOR, 20.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 10.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void registerGoals() {
        // Existing goals (like wandering, attacking, etc.)

//        this.goalSelector.addGoal(1, new TauntGoal(this));               // Aggro management
        this.goalSelector.addGoal(2, new DefensiveStanceGoal(this));     // Defensive behavior when health is low
        this.goalSelector.addGoal(3, new ProtectAllyGoal(this));         // Protect allies when attacked
        this.goalSelector.addGoal(6, new FollowAllyGoal(this, 1.0D, 20.0F, 3)); // Follow the Hearts Golem
        this.goalSelector.addGoal(4,new CustomDiamondGolemMeleeAttackGoal(this, 1.0D, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
//        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    protected int getXpToDrop() {
        int baseXp = this.random.nextInt(10) + 5; // Base XP drop
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

    public void playAttackAnimation(){
        this.attackAnimationTimer = attackAnimationDuration;
    }
    public int getAttackAnimationDuration() {
        return attackAnimationDuration;
    }

    public int getAttackAnimationTimer() {
        return attackAnimationTimer;
    }

    private int attackAnimationTicking(){
        super.aiStep();
        if (this.attackAnimationTimer > 0) {
            --this.attackAnimationTimer;

        }
        return this.attackAnimationTimer;
    }

}
