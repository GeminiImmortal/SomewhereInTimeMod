package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.spell.ObliteratorEntity;
import net.geminiimmortal.mobius.entity.goals.*;
import net.geminiimmortal.mobius.network.BeamEndPacket;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.geminiimmortal.mobius.sound.ClientMusicHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.*;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
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

public class SorcererEntity extends MobEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final DataParameter<Boolean> CASTING = EntityDataManager.defineId(SorcererEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLEEING = EntityDataManager.defineId(SorcererEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DASHING = EntityDataManager.defineId(SorcererEntity.class, DataSerializers.BOOLEAN);
    public GroundPathNavigator moveControl;
    private int particleTickCounter = 0;
    private static final int PARTICLE_SPAWN_INTERVAL = 5;
    private static final DataParameter<Boolean> doesCircleExist = EntityDataManager.defineId(SorcererEntity.class, DataSerializers.BOOLEAN);
    ShatterCloneEntity spell = new ShatterCloneEntity(ModEntityTypes.SHATTER_CLONE.get(), this.level);
    private LaserTrackerAttackGoal laserTrackerGoal;
    private SonicBoomGoal sonicBoomGoal;


    IFormattableTextComponent rank = (StringTextComponent) new StringTextComponent("[CHAMPION FOE] ").setStyle(Style.EMPTY.withColor(TextFormatting.GOLD).withBold(true));
    IFormattableTextComponent name = (StringTextComponent) new StringTextComponent("Lucius, The Court Wizard").setStyle(Style.EMPTY.withColor(TextFormatting.AQUA).withBold(false));
    IFormattableTextComponent namePlate = rank.append(name);

    private final ServerBossInfo bossInfo = new ServerBossInfo(
            namePlate,  // Boss name
            BossInfo.Color.BLUE,
            BossInfo.Overlay.NOTCHED_10
    );





    public SorcererEntity(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
        this.dropExperience();
        this.maxUpStep = 1;
        this.setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CASTING, false);
        this.entityData.define(FLEEING, false);
        this.entityData.define(doesCircleExist, false);
        this.entityData.define(DASHING, false);
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.ARMOR, 15.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 2.5D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.7D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1D);
    }

    @Override
    public void setSecondsOnFire(int seconds) {
        // Do nothing – prevents the mob from visually catching fire
    }


    @Override
    protected void registerGoals() {
        this.laserTrackerGoal = new LaserTrackerAttackGoal(this, 40, 100);
        this.sonicBoomGoal = new SonicBoomGoal(this);
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new AerialLightningBarrageGoal(this, 10, 6, 15));
        this.goalSelector.addGoal(4, laserTrackerGoal);
    //    this.goalSelector.addGoal(3, new MagiDashGoal(this));
    //    this.goalSelector.addGoal(6, new SorcererBackAwayGoal(this, 1.4, 5));
    //    this.goalSelector.addGoal(6, new ArcaneBeamAttackGoal(this, new ObliteratorEntity(ModEntityTypes.OBLITERATOR.get(), this.level)));
    //    this.goalSelector.addGoal(3, sonicBoomGoal);
        this.goalSelector.addGoal(5, new BubbleAttackGoal(this, 4, 20, 80));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 40f));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));

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

        spell.kill();
        if(this.getTarget() != null) {
            ModNetwork.NETWORK_CHANNEL.send(
                    PacketDistributor.TRACKING_CHUNK.with(() -> this.level.getChunkAt(this.getTarget().blockPosition())),
                    new BeamEndPacket(this.getTarget().blockPosition())
            );
        }

        this.level.setSkyFlashTime(5);
        this.level.playSound(null, this.position().x, this.position().y, this.position().z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundCategory.HOSTILE, 100.0f, 1.1f);
        this.level.playSound(null, this.position().x, this.position().y, this.position().z, SoundEvents.ENDER_CHEST_OPEN, SoundCategory.HOSTILE, 100.0f, 0.8f);

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

        if (sonicBoomGoal != null) {
            sonicBoomGoal.tickCooldown();
        }


        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        particleTickCounter++;

        if (particleTickCounter >= PARTICLE_SPAWN_INTERVAL) {
            spawnGlowParticle();
            particleTickCounter = 0;
        }
        if (this.level.isClientSide) {
            ClientMusicHandler.stopVanillaMusic(Minecraft.getInstance());
        }
        this.removeEffect(Effects.LEVITATION);
        summonCircle(this);

    }

    private void summonCircle(SorcererEntity boss) {
        LivingEntity target = this.getTarget();

        if (target != null && target.isAlive() && !getDoesCircleExist()) {
            this.level.addFreshEntity(spell);
            setDoesCircleExist(true);
        }
        spell.setPos(boss.getX(), boss.getY(), boss.getZ());
    }

    @Override
    public void startSeenByPlayer(ServerPlayerEntity player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
        if (this.level.isClientSide) {
            ClientMusicHandler.playCourtWizardBossMusic(Minecraft.getInstance());
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayerEntity player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
        if (this.level.isClientSide) {
            ClientMusicHandler.stopCustomMusic(Minecraft.getInstance());
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
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BEACON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
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
        AnimationController<?> controller = event.getController();

        if (this.getCasting()) {
            // Force animation replay
            controller.markNeedsReload(); // Ensures GeckoLib resets its animation state
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.model.cast", false));
            return PlayState.CONTINUE;
        } else if (this.getDashing()) {
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.model.kneel", false));
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
    public void setDashing(boolean dashing) { this.entityData.set(DASHING, dashing); }
    public void setFleeing(boolean fleeing) { this.entityData.set(FLEEING, fleeing); }
    public void setDoesCircleExist(boolean exists) { this.entityData.set(doesCircleExist,exists); }

    public boolean getCasting() {
        return this.entityData.get(CASTING);
    }

    public boolean getDashing() {
        return this.entityData.get(DASHING);
    }

    public boolean getFleeing() {
        return this.entityData.get(FLEEING);
    }

    public boolean getDoesCircleExist() {
        return this.entityData.get(doesCircleExist);
    }



}


