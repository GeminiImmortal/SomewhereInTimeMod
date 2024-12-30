package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.goals.ShatterCloneGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class CloneEntity extends MobEntity implements IAnimatable, IRangedAttackMob {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final DataParameter<Boolean> CASTING = EntityDataManager.defineId(CloneEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RUNNING = EntityDataManager.defineId(CloneEntity.class, DataSerializers.BOOLEAN);
    private int particleTickCounter = 0;
    private static final int PARTICLE_SPAWN_INTERVAL = 5;





    public CloneEntity(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
        this.dropExperience();
        this.maxUpStep = 1;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CASTING, false);
        this.entityData.define(RUNNING, false);
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.6D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.ARMOR, 15.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 2.5D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.7D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        //this.goalSelector.addGoal(5, new RangedAttackGoal(this, 1.0D, 20, 15.0F));
        this.goalSelector.addGoal(2, new ShatterCloneGoal(this));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 30f));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));

    }

    private void createExpandingCircleParticles(World world, double centerX, double centerY, double centerZ) {
        int particleCount = 50; // Number of particles in the circle
        double radius = 0.5; // Initial radius of the circle
        double radiusIncrement = 0.1; // How much the radius expands per tick
        int duration = 30; // Number of ticks the effect lasts

        for (int tick = 0; tick < duration; tick++) {
            double currentRadius = radius + (tick * radiusIncrement);
            for (int i = 0; i < particleCount; i++) {
                double angle = 2 * Math.PI * i / particleCount; // Calculate angle for each particle
                double offsetX = Math.cos(angle) * currentRadius;
                double offsetZ = Math.sin(angle) * currentRadius;

                // Schedule the particle spawn with a delay
                int finalTick = tick;
                world.getServer().execute(() -> {
                    world.addParticle(ParticleTypes.PORTAL, // Replace with your particle type
                            centerX + offsetX, centerY, centerZ + offsetZ,
                            offsetX * 0.1, 0, offsetZ * 0.1); // Apply slight velocity outward
                });
            }
        }
    }


    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        // Create a new trident entity
        TridentEntity trident = new TridentEntity(this.level, this, new ItemStack(Items.TRIDENT));
        double dx = target.getX() - this.getX();
        double dy = target.getY(0.5D) - trident.getY();
        double dz = target.getZ() - this.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);

        // Set velocity for the trident
        trident.shoot(dx, dy + distance * 0.2, dz, 1.5F, 0.5F);

        // Set additional properties
        trident.setOwner(this);
        trident.pickup = AbstractArrowEntity.PickupStatus.DISALLOWED;

        // Spawn the trident in the world
        this.level.addFreshEntity(trident);
    }


    protected int getXpToDrop() {
        int baseXp = 0;
        return baseXp;
    }


    @Override
    public void die(DamageSource source) {
        super.die(source);
        createExpandingCircleParticles(this.level, this.getX(), this.getY(), this.getZ());

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
        // Get the mob's velocity
        Vector3d motion = this.getDeltaMovement();

        // Check if the mob is moving
        if (motion.x != 0 || motion.y != 0 || motion.z != 0) {
            this.setRunning(true);
        }
    }

    public boolean getRunning() {
        return this.entityData.get(RUNNING);
    }

    public void setRunning(boolean running) {
        this.entityData.set(RUNNING,running);
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
        return SoundEvents.PILLAGER_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }


    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<CloneEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        AnimationController<CloneEntity> alertedController = new AnimationController<>(this, "alertedController", 0, this::alertedPredicate);

        data.addAnimationController(controller);
        data.addAnimationController(alertedController);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (!this.getRunning()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.idle", true));
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.run", true));
            return PlayState.CONTINUE;
        }
    }


    private <E extends IAnimatable> PlayState alertedPredicate(AnimationEvent<E> event) {
        if (this.getCasting()) {
           event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.attack", true));
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
    public boolean getCasting() {
        return this.entityData.get(CASTING);
    }



}


