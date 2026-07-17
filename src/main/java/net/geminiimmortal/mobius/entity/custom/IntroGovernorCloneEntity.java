package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
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

import javax.annotation.Nullable;
import java.util.List;

public class IntroGovernorCloneEntity extends CreatureEntity implements IAnimatable {
    AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public IntroGovernorCloneEntity(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
        this.setPersistenceRequired();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
    }

    protected void triggerExplodeOnHit() {
        this.playSound(ModSounds.GOVERNOR_POOF.get(), 12.0F, 1.0F);
        this.playSound(ModSounds.GOVERNOR_LAUGH.get(), 12.0F, 1.0F);
        AxisAlignedBB markerDetection = new AxisAlignedBB(this.blockPosition()).inflate(25);
        List<MarkerEntity> marker = this.level.getEntitiesOfClass(MarkerEntity.class, markerDetection);
        if (!marker.isEmpty()) {
            marker.forEach(markerEntity -> {
                if (markerEntity.governorBossSealMarker) {
                    markerEntity.governorSealBlocks(this);
                }
            });
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.triggerExplodeOnHit();

        double bossX = this.blockPosition().getX();
        double bossY = this.blockPosition().getY();
        double bossZ = this.blockPosition().getZ();

        EntityType<GovernorEntity> bossEntityType = ModEntityTypes.GOVERNOR.get();
        GovernorEntity bossEntity = bossEntityType.create(this.level);
        if (bossEntity != null) {
            bossEntity.moveTo(bossX, bossY, bossZ);
            this.level.addFreshEntity(bossEntity);
            this.level.playSound(null, this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ(), SoundEvents.WITHER_SPAWN, SoundCategory.HOSTILE, 1.0F, 1.0F);
            this.remove();
        }
        return super.hurt(source, amount);
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

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return ModSounds.GOVERNOR_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.GOVERNOR_DEATH.get();
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<IntroGovernorCloneEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
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
