package net.geminiimmortal.mobius.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.DolphinLookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.RawAnimation;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class AnglerfishEntity extends WaterMobEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public AnglerfishEntity(EntityType<? extends WaterMobEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new MoveHelperController(this);
        this.lookControl = new DolphinLookController(this, 10);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.9)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0D);
    }

    @Override
    protected void registerGoals() {

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, SquidEntity.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, VillagerEntity.class, false));

        this.goalSelector.addGoal(0, new FindWaterGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0, 4));
    }

    public boolean canBeControlledByRider() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.WATER;
    }

    @Override
    protected PathNavigator createNavigation(World world) {
        return new SwimmerPathNavigator(this, world);
    }


    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SILVERFISH_HURT;
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.FISH_SWIM, 0.15f, 0.7f);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.FISH_SWIM;
    }

    private static final RawAnimation SWIM = new RawAnimation("animation.anglerfish.swim", true);

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<AnglerfishEntity> controller = new AnimationController<>(this, "controller", 0, this::creatureController);
        data.addAnimationController(controller);
    }

    private <E extends IAnimatable> PlayState creatureController(AnimationEvent<E> event) {
        AnglerfishEntity entity = (AnglerfishEntity) event.getAnimatable();
        event.getController().setAnimation(new AnimationBuilder().addAnimation(SWIM.animationName));
        return PlayState.CONTINUE;
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    static class MoveHelperController extends MovementController {
        private final AnglerfishEntity anglerfish;

        public MoveHelperController(AnglerfishEntity p_i48945_1_) {
            super(p_i48945_1_);
            this.anglerfish = p_i48945_1_;
        }

        public void tick() {
            if (this.anglerfish.isInWater()) {
                this.anglerfish.setDeltaMovement(this.anglerfish.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
            }

            if (this.operation == Action.MOVE_TO && !this.anglerfish.getNavigation().isDone()) {
                double d0 = this.wantedX - this.anglerfish.getX();
                double d1 = this.wantedY - this.anglerfish.getY();
                double d2 = this.wantedZ - this.anglerfish.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < (double)2.5000003E-7F) {
                    this.mob.setZza(0.0F);
                } else {
                    float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                    this.anglerfish.yRot = this.rotlerp(this.anglerfish.yRot, f, 10.0F);
                    this.anglerfish.yBodyRot = this.anglerfish.yRot;
                    this.anglerfish.yHeadRot = this.anglerfish.yRot;
                    float f1 = (float)(this.speedModifier * this.anglerfish.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    if (this.anglerfish.isInWater()) {
                        this.anglerfish.setSpeed(f1 * 0.02F);
                        float f2 = -((float)(MathHelper.atan2(d1, (double)MathHelper.sqrt(d0 * d0 + d2 * d2)) * (double)(180F / (float)Math.PI)));
                        f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85.0F, 85.0F);
                        this.anglerfish.xRot = this.rotlerp(this.anglerfish.xRot, f2, 5.0F);
                        float f3 = MathHelper.cos(this.anglerfish.xRot * ((float)Math.PI / 180F));
                        float f4 = MathHelper.sin(this.anglerfish.xRot * ((float)Math.PI / 180F));
                        this.anglerfish.zza = f3 * f1;
                        this.anglerfish.yya = -f4 * f1;
                    } else {
                        this.anglerfish.setSpeed(f1 * 0.1F);
                    }

                }
            } else {
                this.anglerfish.setSpeed(0.0F);
                this.anglerfish.setXxa(0.0F);
                this.anglerfish.setYya(0.0F);
                this.anglerfish.setZza(0.0F);
            }
        }
    }
}
