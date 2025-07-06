package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.goals.CloneSpellGoal;
import net.geminiimmortal.mobius.entity.goals.GovernorSummonCloneGoal;
import net.geminiimmortal.mobius.entity.goals.ShatterCloneGoal;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class GovernorEntity extends AbstractImperialBossEntity implements IAnimatable {
    private int cloneCooldown = 300;
    private static final DataParameter<Integer> GCD = EntityDataManager.defineId(GovernorEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> GRINNING = EntityDataManager.defineId(GovernorEntity.class, DataSerializers.BOOLEAN);
    AnimationFactory factory = GeckoLibUtil.createFactory(this);
    GovernorSummonCloneGoal governorSummonCloneGoal;

    public GovernorEntity(EntityType<? extends CreatureEntity> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GCD, 300);
        this.entityData.define(GRINNING, false);
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
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new GovernorSummonCloneGoal(this));
        this.goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 30.0f));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
    }

    @Override
    public void tick() {
        if (level.isClientSide) {
            if (!this.isAlive() || this.removed) {
                stopBossMusic();
            }
        }
        decrementCloneSummonCooldown();
        super.tick();
    }

    public int getGCD() {
        return this.entityData.get(GCD);
    }

    public void setGCD(int ticks) {
        this.entityData.set(GCD, ticks);
    }

    public boolean getGrinning() {
        return this.entityData.get(GRINNING);
    }

    public void setGrinning(boolean grinning) {
        this.entityData.set(GRINNING, grinning);
    }

    public void decrementCloneSummonCooldown() {
        int current = getGCD();
        if (current > 0) {
            setGCD(current - 1);
        }
        if (current <= 0) {
            setGrinning(false);
            setGCD(300);
        }
        if (current == 40) {
            setGrinning(true);
        }
    }

    @Override
    public void stopBossMusic() {
        super.stopBossMusic();
    }

    @Override
    public SoundEvent setBossMusic() {
        return ModSounds.BULLYRAG.get();
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<GovernorEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
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
