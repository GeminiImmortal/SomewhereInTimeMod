package net.geminiimmortal.mobius.entity.custom;

import com.google.common.collect.ImmutableList;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.entity.goals.GiantStompGoal;
import net.geminiimmortal.mobius.network.GiantStompPacket;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.task.FindNewAttackTargetTask;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Random;

public class GiantEntity extends CreatureEntity implements IAnimatable, IMob {
    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.defineId(GiantEntity.class, DataSerializers.BOOLEAN);


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
    }

    public static boolean canMobSpawn(EntityType<? extends CreatureEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        return (world.getBlockState(pos.below()) == ModBlocks.HEMATITE.get().defaultBlockState()) || (world.getBlockState(pos.below()) == ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState());
    }

    public void setAttacking(boolean bool) {
        this.entityData.set(ATTACKING, bool);
    }

    public boolean getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public GiantEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.maxUpStep = 1;
        this.dropExperience();
        this.setPersistenceRequired();
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return CreatureEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 180.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.7D)
                .add(Attributes.ATTACK_DAMAGE, 22.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 5.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1D);
    }

    @Override
    public boolean isAggressive() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new GiantStompGoal(this, 0.5D, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, VillagerEntity.class, false));
        this.goalSelector.addGoal(5, new RandomWalkingGoal(this, 0.3D));
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<GiantEntity> controller = new AnimationController<>(this, "controller", 0, this::creatureController);

        controller.registerSoundListener(this::soundListener);
        data.addAnimationController(controller);
    }

    private <ENTITY extends IAnimatable> void soundListener(SoundKeyframeEvent<ENTITY> event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(ModSounds.GIANT_STOMP.get(), 1, 1);
        }
        GiantEntity self = (GiantEntity) event.getEntity();
        ModNetwork.NETWORK_CHANNEL.sendToServer(new GiantStompPacket(self.getId()));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        super.doHurtTarget(target);
        return true;
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(ModSounds.GIANT_STOMP.get(), 0.45f, 0.7f);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.GIANT_HURT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.GIANT_HURT.get();
    }

    public void stompAttack() {
        List<LivingEntity> nearby = this.level.getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(4),
                e -> !(e instanceof GiantEntity) && e.isAlive()

        );

        for (LivingEntity entity : nearby) {
            this.doHurtTarget(entity);
        }
        spawnStompParticles(3);
        this.setAttacking(false);
    }

    private void spawnStompParticles(double radius) {
        BlockState dirtState = Blocks.DIRT.defaultBlockState();

        for (int i = 0; i < 20; i++) {
            double angle = this.random.nextDouble() * 2 * Math.PI;
            double dist = this.random.nextDouble() * radius;
            double x = this.getX() + Math.cos(angle) * dist;
            double z = this.getZ() + Math.sin(angle) * dist;
            double y = this.getY();

            // Check ground height for visual accuracy
            BlockPos pos = new BlockPos(x, y - 0.1, z);
            if (!level.getBlockState(pos.below()).isAir()) {
                ((ServerWorld) this.level).sendParticles(
                        new BlockParticleData(ParticleTypes.BLOCK, dirtState),
                        x, y, z,
                        5, // count
                        0.25, 0.1, 0.25, // offset spread
                        0.05 // speed
                );
            }
        }
    }



    private <E extends IAnimatable> PlayState creatureController(AnimationEvent<E> event) {
        GiantEntity entity = (GiantEntity) event.getAnimatable();
        AnimationController<?> controller = event.getController();

        if (controller.getCurrentAnimation() != null &&
                controller.getCurrentAnimation().animationName.equals("animation.giant.stomp") &&
                !controller.getAnimationState().equals(AnimationState.Stopped)) {
            return PlayState.CONTINUE;
        }

        if (this.getAttacking()) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.giant.stomp", false));
            return PlayState.CONTINUE;
        }

        if (entity.getDeltaMovement().length() > 0.05) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.giant.walk", true));
            return PlayState.CONTINUE;
        }

        controller.setAnimation(new AnimationBuilder().addAnimation("animation.giant.idle", true));
        return PlayState.CONTINUE;
    }

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected int getXpToDrop() {
        int baseXp = this.random.nextInt(45) + 15;
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
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
    }

}
