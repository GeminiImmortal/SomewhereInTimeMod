package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.goals.*;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class SorcererEntity extends MobEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final DataParameter<Boolean> CASTING = EntityDataManager.defineId(SorcererEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLEEING = EntityDataManager.defineId(SorcererEntity.class, DataSerializers.BOOLEAN);
    public GroundPathNavigator moveControl;
    private int particleTickCounter = 0;
    private static final int PARTICLE_SPAWN_INTERVAL = 5;




    public SorcererEntity(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
        this.dropExperience();
        this.maxUpStep = 1;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CASTING, false);
        this.entityData.define(FLEEING, false);
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.7D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.05D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new SorcererBackAwayGoal(this, 1.4, 12));
        this.goalSelector.addGoal(2, new SorcererCastSpellGoal(this, 16));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 0.2f));
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(3, new SorcererLightningStrikeGoal(this, 20, 24));
    }

    protected int getXpToDrop() {
        int baseXp = this.random.nextInt(5) + 2;
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
            this.level.addParticle(ParticleTypes.DRAGON_BREATH,
                    this.getX() + (Math.random() - 0.5) * 2,
                    this.getY() + 1.0,
                    this.getZ() + (Math.random() - 0.5) * 2,
                    0, 0.01, 0);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){
        return SoundEvents.PILLAGER_HURT;
    }


    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<SorcererEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        AnimationController<SorcererEntity> alertedController = new AnimationController<>(this, "alertedController", 0, this::alertedPredicate);

        data.addAnimationController(controller);
        data.addAnimationController(alertedController);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getFleeing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.walk", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    private <E extends IAnimatable> PlayState alertedPredicate(AnimationEvent<E> event) {
        if (this.getCasting()) {
           event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.cast", false));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public void setCasting(boolean alerted) {
        this.entityData.set(CASTING, alerted);
    }
    public void setFleeing(boolean fleeing) { this.entityData.set(FLEEING, fleeing); }

    public boolean getCasting() {
        return this.entityData.get(CASTING);
    }

    public boolean getFleeing() {
        return this.entityData.get(FLEEING);
    }



}


