package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.entity.goals.FootmanAttackGoal;
import net.geminiimmortal.mobius.event.ImperialReinforcementHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Random;

public class ImperialTowerRegularEntity extends FootmanEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.defineId(ImperialTowerRegularEntity.class, DataSerializers.BOOLEAN);

    public ImperialTowerRegularEntity(EntityType<? extends AbstractImperialEntity> type, World worldIn) {
        super(type, worldIn);
        this.dropExperience();
        this.maxUpStep = 1;
        this.setPersistenceRequired();
    }

    @Override
    public Rank getRank() {
        return Rank.REGULAR;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 6.5D)
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.ARMOR, 15.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 1.3D)
                .add(Attributes.ATTACK_KNOCKBACK, 2.22D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
    }

    public static boolean canMobSpawn(EntityType<? extends AbstractImperialEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        int existing = world.getEntitiesOfClass(FootmanEntity.class, new AxisAlignedBB(pos).inflate(20)).size();
        return (world.getBlockState(pos.below()) == ModBlocks.HEMATITE.get().defaultBlockState()) && existing < 3;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new FootmanAttackGoal(this, 1.1, true));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(5, new LookAtGoal(this, VillagerEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
    }

    @Override
    protected int getXpToDrop() {
        int baseXp = this.random.nextInt(15) + 8;
        return baseXp;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (!this.level.isClientSide()) {
            int experiencePoints = this.getXpToDrop();

            // Drop the experience orbs
            while (experiencePoints > 0) {
                int experienceToDrop = experiencePoints;
                experiencePoints -= experienceToDrop;
                this.level.addFreshEntity(new ExperienceOrbEntity(this.level, this.getX(), this.getY(), this.getZ(), experienceToDrop));
            }
            givePlayerInfamyOnDeath(source, 25);
        }
        if (this.getRank() == Rank.GRUNT && !this.level.isClientSide() && source.getEntity() instanceof ServerPlayerEntity && this.isPatrolMember()) {
            this.level.getEntitiesOfClass(
                    AbstractImperialEntity.class,
                    this.getBoundingBox().inflate(32),
                    e -> e.getRank().equals(Rank.OFFICER) && e.isAlive()
            ).stream().findFirst().ifPresent(ImperialReinforcementHandler::queueReinforcements);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){
        return SoundEvents.VINDICATOR_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<FootmanEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        data.addAnimationController(controller);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.imperial_regular.attack", false));
            this.setAttacking(false);
            return PlayState.CONTINUE;
        }

        if (this.getDeltaMovement().length() > 0.075) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.imperial_regular.run", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.imperial_regular.idle", true));
        return PlayState.CONTINUE;
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public Boolean getAttacking() {
        return this.entityData.get(ATTACKING);
    }
}
