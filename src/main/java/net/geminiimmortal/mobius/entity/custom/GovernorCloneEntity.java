package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.damage.CloneShatterDamageSource;
import net.geminiimmortal.mobius.effects.ModEffects;
import net.geminiimmortal.mobius.entity.goals.SonicBoomGoal;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GovernorCloneEntity extends MonsterEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private GovernorEntity governor;
    private UUID governorUUID;

    private static final DataParameter<Boolean> IS_COUNTDOWN = EntityDataManager.defineId(GovernorCloneEntity.class, DataSerializers.BOOLEAN);
    private int lifetimeTicks = 0;
    private static final int MAX_LIFETIME = 100;

    public GovernorCloneEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
        this.xpReward = 0;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.ARMOR, 15.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 2.5D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.7D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_COUNTDOWN, false);
    }

    @Override
    public boolean save(CompoundNBT nbt) {
        if (governorUUID != null) {
            nbt.putUUID("OriginalGovernor", governorUUID);
        }
        return super.save(nbt);
    }

    @Override
    public void load(CompoundNBT nbt) {
        super.load(nbt);

        if (nbt.hasUUID("OriginalGovernor")) {
            governorUUID = nbt.getUUID("OriginalGovernor");
            if (level instanceof ServerWorld) {
                Entity entity = ((ServerWorld) level).getEntity(governorUUID);
                if (entity instanceof GovernorEntity) {
                    governor = (GovernorEntity) entity;
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        GovernorEntity gov = this.getOriginalGovernor();
        if (gov != null) {
            if (gov.isOnFire()) {
                this.setSecondsOnFire(10);
            }

            if (!this.level.isClientSide) {
                if (!gov.getCorrectHit() && gov.getGCD() <= 99) {
                    this.startCountdown();
                    this.triggerExplodeOnHit();
                    lifetimeTicks++;
                    if (gov.getGCD() <= 2) {
                        this.explode();
                    }
                }

                if (gov.getCorrectHit()) {
                    this.remove();
                }

                if (gov.getHealth() < gov.getMaxHealth() * 0.5) {
                    this.goalSelector.addGoal(1, new SonicBoomGoal(this));
                }
            }
        }
    }

    private void triggerExplodeOnHit() {
        if (this.hurtTime > 1 && !Objects.equals(this.getLastDamageSource(), DamageSource.ON_FIRE)) {
            this.explode();
        }
    }

    public void setOriginalGovernor(GovernorEntity governor, UUID governorUUID) {
        this.governor = governor;
        this.governorUUID = governorUUID;
    }

    public GovernorEntity getOriginalGovernor() {
        if (governor != null) return governor;

        if (level instanceof ServerWorld && governorUUID != null) {
            Entity entity = ((ServerWorld) level).getEntity(governorUUID);
            if (entity instanceof GovernorEntity) governor = (GovernorEntity) entity;
        }
        return governor;
    }

    public void setFromGovernor(GovernorEntity governor) {
        this.setItemInHand(Hand.MAIN_HAND, governor.getMainHandItem().copy());
        this.setHealth(this.getMaxHealth() * 0.5F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 50.0F));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public void startCountdown() {
        this.entityData.set(IS_COUNTDOWN, true);
    }

    public void explode() {
        if (this.level.isClientSide) return;

        List<PlayerEntity> nearby = this.level.getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(this.blockPosition()).inflate(4.0D));
        for (PlayerEntity player : nearby) {
            player.hurt(CloneShatterDamageSource.CLONE_SHATTER, 12f);
            player.addEffect(new EffectInstance(ModEffects.EXPOSED_EFFECT.get(), 100));
        }

        this.level.playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundCategory.HOSTILE, 0.2F, 1.0F);
        this.remove();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.isExplosion() && source.getEntity() == this;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return ModSounds.GOVERNOR_HURT.get();
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<GovernorCloneEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        data.addAnimationController(controller);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.governor.idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
