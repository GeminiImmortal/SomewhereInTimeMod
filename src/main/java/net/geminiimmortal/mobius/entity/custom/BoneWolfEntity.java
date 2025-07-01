package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.faction.FactionType;
import net.geminiimmortal.mobius.faction.IFactionCarrier;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.RawAnimation;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Random;
import java.util.function.Predicate;

public class BoneWolfEntity extends WolfEntity implements IAnimatable, IFactionCarrier {
    private static final DataParameter<Boolean> SITTING = EntityDataManager.defineId(BoneWolfEntity.class, DataSerializers.BOOLEAN);
    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_213440_0_) -> {
        EntityType<?> entitytype = p_213440_0_.getType();
        return entitytype == ModEntityTypes.FAEDEER.get();
    };
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public BoneWolfEntity(EntityType<? extends WolfEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0D);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new NonTamedTargetGoal<>(this, PlayerEntity.class, false, null));
        this.targetSelector.addGoal(5, new NonTamedTargetGoal<>(this, AnimalEntity.class, false, PREY_SELECTOR));
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.1D, true));
        this.goalSelector.addGoal(3, new SitGoal(this));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    public FactionType getFaction() {
        return FactionType.DANGEROUS_TO_VILLAGES;
    }

    public void setSitting(Boolean bool){
        this.entityData.set(SITTING, bool);
    }

    public boolean getSitting() {
        return this.entityData.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean orderedToSit) {
        if (!this.level.isClientSide()) {
            if (this.getOwner() != null) {
                ServerPlayerEntity owner = (ServerPlayerEntity) this.getOwner();
                if (this.isOrderedToSit() && this.isTame()) {
                    this.setSitting(false);
                    super.setOrderedToSit(orderedToSit);
                } else if (!this.isOrderedToSit() && this.isTame()) {
                    this.setSitting(true);
                    super.setOrderedToSit(orderedToSit);
                } else {
                    this.setSitting(true);
                    super.setOrderedToSit(orderedToSit);
                }
            }
        }
    }


    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        return ActionResultType.FAIL;
    }

    public static boolean canMobSpawn(EntityType<? extends CreatureEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        return (world.getBlockState(pos.below()) == ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState());
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.BONE_WOLF_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.SKELETON_STEP, 0.15f, 0.7f);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WOLF_GROWL;
    }

    @Override
    public boolean canAttack(LivingEntity type) {
        return !this.isTame() && super.canAttack(type); // Don't attack if tamed
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<BoneWolfEntity> controller = new AnimationController<>(this, "controller", 0, this::boneWolfController);
        data.addAnimationController(controller);
    }


    private static final RawAnimation IDLE = new RawAnimation("animation.bone_wolf.idle", true);
    private static final RawAnimation RUN = new RawAnimation("animation.bone_wolf.run", true);
    private static final RawAnimation SIT = new RawAnimation("animation.bone_wolf.sit", true);

    private <E extends IAnimatable> PlayState boneWolfController(AnimationEvent<E> event) {
        BoneWolfEntity entity = (BoneWolfEntity) event.getAnimatable();

        if (entity.hurtTime > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(RUN.animationName));
            this.setSitting(false);
            return PlayState.CONTINUE;
        }


        if (this.getSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SIT.animationName));
            return PlayState.CONTINUE;
        }

        if (entity.getDeltaMovement().length() > 0.1) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(RUN.animationName));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation(IDLE.animationName));
        return PlayState.CONTINUE;
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void addPersistentAngerSaveData(CompoundNBT p_233682_1_) {
        super.addPersistentAngerSaveData(p_233682_1_);
    }

    @Override
    public void readPersistentAngerSaveData(ServerWorld p_241358_1_, CompoundNBT p_241358_2_) {
        super.readPersistentAngerSaveData(p_241358_1_, p_241358_2_);
    }

    @Override
    public void updatePersistentAnger(ServerWorld p_241359_1_, boolean p_241359_2_) {
        super.updatePersistentAnger(p_241359_1_, p_241359_2_);
    }

    @Override
    public boolean isAngryAt(LivingEntity p_233680_1_) {
        return super.isAngryAt(p_233680_1_);
    }

    @Override
    public boolean isAngryAtAllPlayers(World p_241357_1_) {
        return super.isAngryAtAllPlayers(p_241357_1_);
    }

    @Override
    public boolean isAngry() {
        return super.isAngry();
    }

    @Override
    public void stopBeingAngry() {
        super.stopBeingAngry();
    }
}
