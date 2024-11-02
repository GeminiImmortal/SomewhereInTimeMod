package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.goals.FaedeerRunGoal;
import net.geminiimmortal.mobius.entity.goals.LookForPlayerGoal;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
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

public class FaedeerEntity extends MobEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final DataParameter<Boolean> ALERTED = EntityDataManager.defineId(FaedeerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLEEING = EntityDataManager.defineId(FaedeerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<BlockPos> FLEE_POSITION = EntityDataManager.defineId(FaedeerEntity.class, DataSerializers.BLOCK_POS);
    public GroundPathNavigator moveControl;
    private int particleTickCounter = 0;
    private static final int PARTICLE_SPAWN_INTERVAL = 5;




    public FaedeerEntity(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
        this.dropExperience();
        this.maxUpStep = 1;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ALERTED, false);
        this.entityData.define(FLEEING, false);
        this.entityData.define(FLEE_POSITION, this.getBlockPosBelowThatAffectsMyMovement());
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.05D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new LookForPlayerGoal(this, 24));
        this.goalSelector.addGoal(2, new FaedeerRunGoal(this, 12, 1.2D));
    }

    protected int getXpToDrop() {
        int baseXp = this.random.nextInt(2) + 2;
        return baseXp;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
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
            this.level.addParticle(ModParticles.FAEDEER_PARTICLE.get(),
                    this.getX() + (Math.random() - 0.5) * 2,
                    this.getY() + 1.0,
                    this.getZ() + (Math.random() - 0.5) * 2,
                    0, 0.01, 0);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){
        return ModSounds.FAEDEER_HURT.get();
    }


    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<FaedeerEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        AnimationController<FaedeerEntity> alertedController = new AnimationController<>(this, "alertedController", 0, this::alertedPredicate);

        data.addAnimationController(controller);
        data.addAnimationController(alertedController);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getFleeing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("run", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    private <E extends IAnimatable> PlayState alertedPredicate(AnimationEvent<E> event) {
        if (this.getAlerted() && !this.getFleeing()) {
           event.getController().setAnimation(new AnimationBuilder().addAnimation("startled", false));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public void setAlerted(boolean alerted) {
        this.entityData.set(ALERTED, alerted);
    }

    public void setFleeing(boolean fleeing) {
        this.entityData.set(FLEEING, fleeing);
    }

    public boolean getFleeing() {
        return this.entityData.get(FLEEING);
    }

    public boolean getAlerted() {
        return this.entityData.get(ALERTED);
    }

}


