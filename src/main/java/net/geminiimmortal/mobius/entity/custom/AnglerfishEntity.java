package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.fluid.ModFluids;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.DolphinLookController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
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

import java.util.Random;

public class AnglerfishEntity extends WaterMobEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public AnglerfishEntity(EntityType<? extends WaterMobEntity> type, World worldIn) {
        super(type, worldIn);
        this.lookControl = new DolphinLookController(this, 10);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.4)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0D);
    }

    @Override
    protected void registerGoals() {

        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, SquidEntity.class, true));

        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0, 4));
    }

    @Override
    protected PathNavigator createNavigation(World world) {
        return new SwimmerPathNavigator(this, world);
    }

    public boolean isInCustomFluid() {
        return this.level.getFluidState(this.blockPosition()).getType() == ModFluids.BOG_WATER_FLUID.get() || this.level.getFluidState(this.blockPosition()).getType() == ModFluids.ECTOPLASM_FLUID.get();
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return isInCustomFluid();
    }

    public static boolean canMobSpawn(EntityType<? extends MobEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        FluidState fluidState = world.getFluidState(pos.below());
        return (fluidState.getType() == ModFluids.BOG_WATER_FLUID.get().getFluid()) || (fluidState.getType() == ModFluids.ECTOPLASM_FLUID.get().getFluid());
    }

    /*@Override
    public void travel(Vector3d travelVector) {
        if (this.isInCustomFluid()) {
            this.moveRelative(0.02F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
        } else {
            super.travel(travelVector);
        }
    }*/


    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ANGLERFISH_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_INSIDE, 0.15f, 0.7f);
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
        if (entity.isInCustomFluid()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SWIM.animationName));
        }
        return PlayState.CONTINUE;
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
