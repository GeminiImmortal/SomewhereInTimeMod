package net.geminiimmortal.mobius.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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

public class GrimcrowEntity extends AbstractGrimcrowEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final DataParameter<Integer> ATTACK_PHASE =
            EntityDataManager.defineId(GrimcrowEntity.class, DataSerializers.INT);


    public GrimcrowEntity(EntityType<? extends AbstractGrimcrowEntity> entityType, World worldIn) {
        super(entityType, worldIn);
        this.moveControl = new MoveHelperController(this);
        this.setNoGravity(true);
        this.xpReward = 15;
        this.maxUpStep = 1;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_PHASE, 0);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.125D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.05D);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.goalSelector.getRunningGoals().anyMatch(prioritizedGoal -> prioritizedGoal.getGoal() instanceof ChargeAttackGoal)) {
            if (this.getTarget() != null) {
                performRelentlessStrikes();
            }
        }
    }

    public int getAttackPhase() {
        return this.entityData.get(ATTACK_PHASE);
    }

    public void setAttackPhase(int phase) {
        this.entityData.set(ATTACK_PHASE, phase);
    }

    private void performRelentlessStrikes() {
        LivingEntity target = this.getTarget();
        if (target == null) {
            return;
        }

        if (this.distanceTo(target) >= 2.0D) {
            this.setAttackPhase(0);
        }

        if (this.distanceTo(target) < 2.0D) {
            this.setAttackPhase(1);
            doHurtTarget(target);
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<GrimcrowEntity> controller = new AnimationController<>(this, "controller", 2f, this::predicate);
        data.addAnimationController(controller);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<GrimcrowEntity> event) {
        if (this.getAttackPhase() == 1) {
            playAnimation(event,"attack", "LOOP");
            return PlayState.CONTINUE;
        } else if(this.getAttackPhase() == 0) {
            playAnimation(event,"fly", "LOOP");
            return PlayState.CONTINUE;
        }
        playAnimation(event, "fly", "LOOP");
        return PlayState.CONTINUE;
    }

    private void playAnimation(AnimationEvent<GrimcrowEntity> event ,String animationName, String shouldLoop) {
        ILoopType animationLoopType = ILoopType.EDefaultLoopTypes.valueOf(shouldLoop);
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, animationLoopType));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
