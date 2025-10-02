package net.geminiimmortal.mobius.entity.custom;

import com.google.common.base.Predicates;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.entity.goals.FaedeerRunGoal;
import net.geminiimmortal.mobius.entity.goals.LookForPlayerGoal;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
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

import javax.annotation.Nullable;
import java.util.Random;

public class JackalopeEntity extends RabbitEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final DataParameter<Boolean> ALERTED = EntityDataManager.defineId(JackalopeEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLEEING = EntityDataManager.defineId(JackalopeEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<BlockPos> FLEE_POSITION = EntityDataManager.defineId(JackalopeEntity.class, DataSerializers.BLOCK_POS);
    public GroundPathNavigator moveControl;
    private int particleTickCounter = 0;
    private static final int PARTICLE_SPAWN_INTERVAL = 5;




    public JackalopeEntity(EntityType<? extends RabbitEntity> type, World worldIn) {
        super(type, worldIn);
        this.dropExperience();
        this.maxUpStep = 1;
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.27D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    public static boolean canMobSpawn(EntityType<? extends CreatureEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        return (world.getBlockState(pos.below()) == ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState());
    }

    protected int getXpToDrop() {
        int baseXp = this.random.nextInt(2) + 2;
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
    public void tick() {
        super.tick();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){
        return SoundEvents.RABBIT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.RABBIT_DEATH;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.RABBIT_AMBIENT;
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.RABBIT_JUMP, 0.45f, 0.7f);
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<JackalopeEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        data.addAnimationController(controller);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.jackalope.hop", true));
        return PlayState.CONTINUE;

    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

}


