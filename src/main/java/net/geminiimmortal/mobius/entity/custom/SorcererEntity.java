package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.spell.ObliteratorEntity;
import net.geminiimmortal.mobius.entity.goals.*;
import net.geminiimmortal.mobius.network.BeamEndPacket;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class SorcererEntity extends AbstractImperialEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final DataParameter<Boolean> CASTING = EntityDataManager.defineId(SorcererEntity.class, DataSerializers.BOOLEAN);
    private int particleTickCounter = 0;
    private static final int PARTICLE_SPAWN_INTERVAL = 5;
    ShatterCloneEntity spell = new ShatterCloneEntity(ModEntityTypes.SHATTER_CLONE.get(), this.level);
    private LaserTrackerAttackGoal laserTrackerGoal;
    private ArcaneBeamAttackGoal arcaneBeamAttackGoal;

    public SorcererEntity(EntityType<? extends AbstractImperialEntity> type, World worldIn) {
        super(type, worldIn);
        this.dropExperience();
        this.maxUpStep = 1;
        this.setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CASTING, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.ARMOR, 15.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 2.5D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.7D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1D);
    }

    @Override
    public Rank getRank() {
        return Rank.OFFICER;
    }

    @Override
    public void setSecondsOnFire(int seconds) {
        // Do nothing – prevents the mob from visually catching fire
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.arcaneBeamAttackGoal = new ArcaneBeamAttackGoal(this, new ObliteratorEntity(ModEntityTypes.OBLITERATOR.get(), this.level), 1200);
        this.laserTrackerGoal = new LaserTrackerAttackGoal(this, 60, 160);
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, laserTrackerGoal);
        this.goalSelector.addGoal(3, arcaneBeamAttackGoal);
        this.goalSelector.addGoal(6, new SorcererBackAwayGoal(this, 1.1, 7));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 40f));
    }


    protected int getXpToDrop() {
        int baseXp = this.random.nextInt(50) + 2;
        return baseXp;
    }

    public void teleportTo(double x, double y, double z) {
        this.level.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY(), this.getZ(), 0.5, 0.5, 0.5);
        super.teleportTo(x, y, z);
        this.level.playSound(null, x, y, z, SoundEvents.ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 10.0F, 1.0F);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isNoGravity()) {
            return false; // Ignore damage during barrage
        }
        if (source == DamageSource.FALL) return false;
        return super.hurt(source, amount);
    }


    @Override
    public void die(DamageSource source) {
        super.die(source);

        AxisAlignedBB boundingBox = new AxisAlignedBB(
                this.getX() - 20, this.getY() - 20, this.getZ() - 20, // Adjust range as needed
                this.getX() + 20, this.getY() + 20, this.getZ() + 20
        );

        List<Entity> nearbyEntities = this.level.getEntities(this, boundingBox);

        for (Entity entity : nearbyEntities) {
            if (entity instanceof ShatterCloneEntity) { // Replace with your entity class
                entity.kill(); // Remove the entity
            }
        }
        givePlayerInfamyOnDeath(source, 100);

        spell.kill();
        if(this.getTarget() != null) {
            ModNetwork.NETWORK_CHANNEL.send(
                    PacketDistributor.TRACKING_CHUNK.with(() -> this.level.getChunkAt(this.getTarget().blockPosition())),
                    new BeamEndPacket(this.getTarget().blockPosition())
            );
        }

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

        this.setRemainingFireTicks(0);

        if(laserTrackerGoal != null) {
            laserTrackerGoal.tickCooldown();
        }

        if (arcaneBeamAttackGoal != null) {
            arcaneBeamAttackGoal.tickCooldown();
        }

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
        return SoundEvents.EVOKER_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<SorcererEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        AnimationController<SorcererEntity> alertedController = new AnimationController<>(this, "alertedController", 0, this::alertedPredicate);

        data.addAnimationController(controller);
        data.addAnimationController(alertedController);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getDeltaMovement().length() > 0.05) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.walk", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    private <E extends IAnimatable> PlayState alertedPredicate(AnimationEvent<E> event) {
        AnimationController<?> controller = event.getController();

        if (this.getCasting()) {
            // Force animation replay
            controller.markNeedsReload(); // Ensures GeckoLib resets its animation state
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.model.cast", false));
            return PlayState.CONTINUE;
        }

        // Clear animations if not casting
        if (controller.getCurrentAnimation() != null) {
            controller.setAnimation(new AnimationBuilder().clearAnimations());
        }
        return PlayState.CONTINUE;
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


