package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.block.custom.MysticTearBlock;
import net.geminiimmortal.mobius.entity.goals.ReturnToPortalGoal;
import net.geminiimmortal.mobius.hook.MusicTickerHook;
import net.geminiimmortal.mobius.item.ModItems;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
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

public class FaestagEntity extends AbstractHorseEntity implements IAnimatable, IJumpingMount {
    private static final DataParameter<Boolean> ALERTED = EntityDataManager.defineId(FaestagEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMONED = EntityDataManager.defineId(FaestagEntity.class, DataSerializers.BOOLEAN);
    private BlockPos portalPosition;


    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public FaestagEntity(EntityType<? extends AbstractHorseEntity> entityType, World worldIn) {
        super(entityType, worldIn);
        this.maxUpStep = 3;
    }

    @Override
    public boolean isSaddleable() {
        return false;
    }

    @Override
    public void equipSaddle(@Nullable SoundCategory p_230266_1_) {
    }

    @Override
    public boolean isSaddled() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ALERTED, false);
        this.entityData.define(SUMMONED, false);
    }

    public boolean getAlerted() {
        return this.entityData.get(ALERTED);
    }

    public void setAlerted(boolean alerted) {
        this.entityData.set(ALERTED, alerted);
    }

    public boolean getSummoned() {
        return this.entityData.get(SUMMONED);
    }

    public void setSummoned(boolean summoned) {
        this.entityData.set(SUMMONED, summoned);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new ReturnToPortalGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.7D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.85D);
    }

    @Override
    protected double generateRandomSpeed() {
        return 0.4D;
    }

    @Override
    protected float generateRandomMaxHealth() {
        return 40f;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        if (this.isTamed()) {
            this.doPlayerRide(p_230254_1_);
            return ActionResultType.sidedSuccess(this.level.isClientSide);
        }
        return ActionResultType.FAIL;
    }

    public BlockPos getTargetPortalPosition() {
        return this.portalPosition;
    }

    public void setTargetPortalPosition(BlockPos portalLocation) {
        this.portalPosition = portalLocation;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<FaestagEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        data.addAnimationController(controller);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<FaestagEntity> event) {
        if (this.getAlerted() && this.getDeltaMovement().length() > 0.1) {
            playAnimation(event, "run", "LOOP");
            return PlayState.CONTINUE;
        }
        if (this.getControllingPassenger() != null && this.getControllingPassenger().isSprinting()) {
            playAnimation(event, "run", "LOOP");
            return PlayState.CONTINUE;
        } else if (this.getDeltaMovement().length() > 0.1) {
            playAnimation(event,"walk", "LOOP");
            return PlayState.CONTINUE;
        }
        playAnimation(event, "attack", "PLAY_ONCE");
        return PlayState.CONTINUE;
    }

    private void playAnimation(AnimationEvent<FaestagEntity> event ,String animationName, String shouldLoop) {
        ILoopType animationLoopType = ILoopType.EDefaultLoopTypes.valueOf(shouldLoop);
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, animationLoopType));
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset();
    }

    @Override
    public void tick() {
        freezeWaterUnderStag();

        if (this.getSummoned()) {
            this.setAlerted(false);
            this.setTamed(true);
        }
        if (this.level.getBlockState(this.blockPosition()).is(Blocks.WATER)) {
            this.moveTo(this.position().x, this.position().y + 1, this.position().z);
        }
        super.tick();
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return !this.noPhysics && super.canCollideWith(entity);
    }

    @Override
    protected int calculateFallDamage(float p_225508_1_, float p_225508_2_) {
        return 0;
    }

    @Override
    public boolean save(CompoundNBT nbt) {
        nbt.putBoolean("Summoned", getSummoned());
        nbt.putBoolean("Alerted", getAlerted());
        return super.save(nbt);
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.entityData.set(SUMMONED, nbt.getBoolean("Summoned"));
        this.entityData.set(ALERTED, nbt.getBoolean("Alerted"));
        super.load(nbt);
    }

    @Override
    public void travel(Vector3d travelVector) {
        LivingEntity rider = this.getControllingPassenger() instanceof LivingEntity
                ? (LivingEntity) this.getControllingPassenger()
                : null;

        if (rider != null) {
            this.yRot = rider.yRot;
            this.yBodyRot = this.yRot;
            this.yHeadRot = this.yRot;
            this.xRot = rider.xRot * 0.5F;

            float forward = MathHelper.clamp(rider.zza, -1.0F, 1.0F);
            float strafe  = MathHelper.clamp(rider.xxa, -1.0F, 1.0F);

            freezeWaterUnderStag();

            float speed = rider.isSprinting() ? 0.5F : 0.4F;
            this.setSpeed(speed);

            super.travel(new Vector3d(strafe, travelVector.y, forward));

        } else {
            super.travel(travelVector);
        }
    }


    private void freezeWaterUnderStag() {
        BlockPos center = this.blockPosition();
        int radius = 3;

        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -1, -radius),
                center.offset(radius, -1, radius))) {

            if (this.level.getBlockState(pos).getBlock() == Blocks.WATER) {
                BlockPos above = pos.above();
                if (this.level.isEmptyBlock(above)) {
                    this.level.setBlock(pos, Blocks.FROSTED_ICE.defaultBlockState(), 3);
                    this.level.getBlockTicks().scheduleTick(pos, Blocks.FROSTED_ICE, 60 + this.random.nextInt(60));
                }
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    protected void playGallopSound(SoundType p_190680_1_) {
        this.playSound(ModSounds.FAESTAG_GALLOP.get(), p_190680_1_.getVolume() * 0.15F, p_190680_1_.getPitch());
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.FAESTAG_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return ModSounds.FAESTAG_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.FAESTAG_AMBIENT.get();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canJump() {
        return false;
    }

    /*@Override
    public void onPlayerJump(int jumpPower) {
        this.setJumping(true);
        if (jumpPower < 0) {
            jumpPower = 0;
        }

        if (jumpPower >= 90) {
            this.playerJumpPendingScale = 1.0F;
        } else {
            this.playerJumpPendingScale = 0.4F + 0.4F * (float)jumpPower / 90.0F;
        }
    }*/


    @Override
    public void handleStartJump(int p_184775_1_) {
        super.handleStartJump(p_184775_1_);
    }

    @Override
    public void handleStopJump() {
        super.handleStopJump();
    }
}
