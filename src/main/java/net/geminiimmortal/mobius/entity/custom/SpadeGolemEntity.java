package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.goals.GolemBackAwayGoal;
import net.geminiimmortal.mobius.entity.goals.SpadeGolemRangedAttackGoal;
import net.geminiimmortal.mobius.faction.FactionType;
import net.geminiimmortal.mobius.faction.IFactionCarrier;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class SpadeGolemEntity extends IronGolemEntity implements IFactionCarrier {
    private int attackAnimationTimer = 0;
    private final int attackAnimationDuration = 20;

    public SpadeGolemEntity(EntityType<? extends IronGolemEntity> type, World worldIn) {
        super(type, worldIn);
        this.dropExperience();
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 13.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ARMOR_TOUGHNESS, 5);
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new SpadeGolemRangedAttackGoal(this, 0.3, 20, 20, 8));
        this.goalSelector.addGoal(1,new GolemBackAwayGoal(this, 1.0D, 8));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 0.3D));
//        this.goalSelector.addGoal(3, new DPSSupportOtherGolemsGoal(this, 1.0D, 30, 7));
//        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }


    protected int getXpToDrop() {
        int baseXp = this.random.nextInt(10) + 5; // Base XP drop
        int difficultyMultiplier = this.level.getDifficulty().getId(); // 0 = Peaceful, 1 = Easy, 2 = Normal, 3 = Hard
        return baseXp + difficultyMultiplier * 2;
    }

    public FactionType getFaction() {
        return FactionType.IMPERIAL;
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



    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn)
    {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.20F, 0.5F);
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