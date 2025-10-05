package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.faction.FactionType;
import net.geminiimmortal.mobius.faction.IFactionCarrier;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
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

public class BoneWolfEntity extends CreatureEntity implements IAnimatable, IFactionCarrier, IMob {
    private static final DataParameter<Boolean> SITTING = EntityDataManager.defineId(BoneWolfEntity.class, DataSerializers.BOOLEAN);
    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_213440_0_) -> {
        EntityType<?> entitytype = p_213440_0_.getType();
        return entitytype == ModEntityTypes.FAEDEER.get();
    };
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public BoneWolfEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
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
    public void tick() {
        super.tick();

        if (!this.level.isClientSide && this.level.isDay() && !this.level.getLevelData().isThundering()) {
            this.remove();
        }

        if(this.level.getLevelData().getDifficulty().equals(Difficulty.PEACEFUL)) {
            this.remove();
        }
    }

    @Override
    public boolean isAggressive() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, FaecowEntity.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, FaedeerEntity.class, true));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, VillagerEntity.class, true));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
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
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        return ActionResultType.FAIL;
    }

    public static boolean canMobSpawn(EntityType<? extends BoneWolfEntity> entityType,
                                      IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
        ServerWorld level = world.getLevel();

        int existing = level.getEntitiesOfClass(BoneWolfEntity.class,
                new AxisAlignedBB(pos).inflate(28)).size();
        int quartermasters = level.getEntitiesOfClass(RebelQuartermasterEntity.class,
                new AxisAlignedBB(pos).inflate(70)).size();
        int villagers = level.getEntitiesOfClass(VillagerEntity.class,
                new AxisAlignedBB(pos).inflate(50)).size();

        long timeOfDay = level.getDayTime() % 24000;
        boolean isMobius = level.dimension() == ModDimensions.MOBIUS_WORLD;
        boolean isNight = (isMobius && (timeOfDay >= 13600 && timeOfDay <= 23000)) || (level.isThundering() && isMobius);
        boolean isPeaceful = level.getDifficulty() == Difficulty.PEACEFUL;

        BlockState ground = level.getBlockState(pos.below());
        boolean validGround = ground.is(ModBlocks.SOUL_PODZOL.get())
                || ground.is(ModBlocks.HEMATITE.get())
                || ground.is(ModBlocks.AURORA_GRASS_BLOCK.get());

        boolean darkEnough = level.getBrightness(LightType.BLOCK, pos) <= 7;

        boolean canSeeSky = level.canSeeSkyFromBelowWater(pos);
        if (canSeeSky && timeOfDay < 13600 && !level.isThundering() && !darkEnough) {
            return false;
        }

        return validGround && isNight && darkEnough && existing < 1 && quartermasters < 1 && !isPeaceful && villagers < 1;
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
}
