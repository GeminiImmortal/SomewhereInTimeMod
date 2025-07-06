package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.damage.CloneShatterDamageSource;
import net.geminiimmortal.mobius.entity.goals.CloneExplodeOnProximityGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
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
import java.util.UUID;

import static net.geminiimmortal.mobius.damage.CloneShatterDamageSource.CLONE_SHATTER;

public class GovernorCloneEntity extends MonsterEntity implements IAnimatable {
    AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private GovernorEntity governor;
    private static final DataParameter<Boolean> IS_COUNTDOWN = EntityDataManager.defineId(GovernorCloneEntity.class, DataSerializers.BOOLEAN);
    private int lifetimeTicks = 0;
    private static final int MAX_LIFETIME = 40; // 2 seconds

    public GovernorCloneEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
        this.xpReward = 0;
        this.governor = getOriginalGovernor();
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
    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {
            lifetimeTicks++;
            if (lifetimeTicks > MAX_LIFETIME) {
                this.explode();
            }

            if (this.entityData.get(IS_COUNTDOWN)) {
                if (lifetimeTicks % 20 == 0) {
                    level.playSound(null, blockPosition(), SoundEvents.ILLUSIONER_CAST_SPELL, SoundCategory.HOSTILE, 1.0F, 1.5F);
                }
                this.setGlowing(true);
            }
        }
    }

    private UUID governorUUID;

    public void setOriginalGovernor(GovernorEntity governor, UUID governorUUID) {
        this.governor = governor;
        this.governorUUID = governorUUID;
    }

    public GovernorEntity getOriginalGovernor() {
        if (governor == null) return null;
        if (level instanceof ServerWorld) {
            Entity entity = ((ServerWorld) level).getEntity(governorUUID);
            if (entity instanceof GovernorEntity) return governor = (GovernorEntity) entity;
        }
        return null;
    }


    public void setFromGovernor(GovernorEntity governor) {
        this.setItemInHand(Hand.MAIN_HAND, governor.getMainHandItem().copy());
        this.setCustomName(new StringTextComponent("Clone of " + governor.getName().getString()));
        this.setCustomNameVisible(true);
        this.setHealth(this.getMaxHealth() * 0.5F); // clones have half HP
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new CloneExplodeOnProximityGoal(this, 2.5));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 8.0F));
    }

    public void startCountdown() {
        this.entityData.set(IS_COUNTDOWN, true);
    }

    public void explode() {
        if (this.level.isClientSide) return;

        List<PlayerEntity> nearby = this.level.getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(this.blockPosition()).inflate(3.0D));
        for (PlayerEntity player : nearby) {
            player.hurt((CLONE_SHATTER), 6.0F);
        }

        this.level.playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundCategory.HOSTILE, 0.2F, 1.0F);
        this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1.5F, Explosion.Mode.NONE);
        this.remove();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.isExplosion() && source.getEntity() == this;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // You can make clones tanky or immune to arrows, etc.
        return super.hurt(source, amount);
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

