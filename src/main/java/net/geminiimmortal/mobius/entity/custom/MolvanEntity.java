package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.goals.AngryOnChestOpenGoal;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class MolvanEntity extends CreatureEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final DataParameter<Boolean> ANGRY = EntityDataManager.defineId(MolvanEntity.class, DataSerializers.BOOLEAN);
    private int particleTickCounter = 0;
    private static final int PARTICLE_SPAWN_INTERVAL = 5;
    private boolean angry;





    public MolvanEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.dropExperience();
        this.maxUpStep = 1;
        this.setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANGRY, false);
    }



    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 2.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0f, true));
        this.goalSelector.addGoal(3, new AngryOnChestOpenGoal(this, 16));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 30f));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, PlayerEntity.class));
    }

    protected int getXpToDrop() {
        int baseXp = 0;
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
        particleTickCounter++;


        if (particleTickCounter >= PARTICLE_SPAWN_INTERVAL) {
            spawnGlowParticle();
            particleTickCounter = 0;
        }
    }


    private void spawnGlowParticle() {
        for (int i = 0; i < 1; i++) {
            this.level.addParticle(ParticleTypes.PORTAL,
                    this.getX() + (Math.random() - 0.5) * 2,
                    this.getY() + 1.0,
                    this.getZ() + (Math.random() - 0.5) * 2,
                    0, 0.01, 0);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){
        return ModSounds.MOLVAN_ANGRY.get();
    }

    //@Override
    //protected SoundEvent getAmbientSound() {
    //    return SoundEvents.VINDICATOR_AMBIENT;
    //}

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.MOLVAN_ANGRY.get();
    }


    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<MolvanEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        AnimationController<MolvanEntity> alertedController = new AnimationController<>(this, "alertedController", 0, this::alertedPredicate);

        data.addAnimationController(controller);
        data.addAnimationController(alertedController);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getMoveControl().hasWanted()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.molvan.move", true));
            return PlayState.CONTINUE;
        }
        if (!this.getMoveControl().hasWanted()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.molvan.idle", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    private <E extends IAnimatable> PlayState alertedPredicate(AnimationEvent<E> event) {
        if (this.getAngry()) {
           event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.molvan.attack", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public boolean getAngry() {
        return this.entityData.get(ANGRY);
    }

    public void setAngry(boolean angry) {
        this.entityData.set(ANGRY, angry);
    }



    @Override
    public AnimationFactory getFactory() {
        return factory;
    }



}


