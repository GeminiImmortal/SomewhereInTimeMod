package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.effects.ModEffects;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.goals.GrimcrowLightningTetherGoal;
import net.geminiimmortal.mobius.faction.FactionType;
import net.geminiimmortal.mobius.faction.IFactionCarrier;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
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
import java.util.EnumSet;

public class GrimcrowCaptainBossEntity extends AbstractImperialBossEntity implements IAnimatable, IFactionCarrier {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private int attackPhaseTicks = 0; // counts phase duration
    private boolean chargingAOE = false;
    private Vector3d aoeTarget = null; // where player was when AOE started
    private boolean performingSlam = false;
    private int slamAirTicks = 0;
    private int postSlamCooldown = 0;
    @Nullable
    private BlockPos boundOrigin;
    private int crewSummonTicks = 0;
    private float breakBar = 0f;
    private final float breakThreshold = 50f; // Amount of damage needed to break tether
    public boolean tetherActive = false;
    public int lightningAttackCooldown = 0;
    private int chargedStacks = 0;
    private static final DataParameter<Float> BREAKBAR_PROGRESS = EntityDataManager.defineId(GrimcrowCaptainBossEntity.class, DataSerializers.FLOAT);

    private final ServerBossInfo breakBarInfo = new ServerBossInfo(
            new StringTextComponent("breakbar"),
            BossInfo.Color.YELLOW,
            BossInfo.Overlay.PROGRESS
    );

    protected static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(GrimcrowCaptainBossEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Integer> LIGHTNING_TICKS = EntityDataManager.defineId(GrimcrowCaptainBossEntity.class, DataSerializers.INT);
    protected static final DataParameter<Integer> CHARGED_STACKS = EntityDataManager.defineId(GrimcrowCaptainBossEntity.class, DataSerializers.INT);

    // Synced state flags for animations
    private static final DataParameter<Integer> ATTACK_PHASE =
            EntityDataManager.defineId(GrimcrowCaptainBossEntity.class, DataSerializers.INT);
// 0 = idle, 1 = strikes, 2 = windup, 3 = leap, 4 = slam impact

    private boolean getFlag(int p_190656_1_) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        return (i & p_190656_1_) != 0;
    }

    private void setFlag(int p_190660_1_, boolean p_190660_2_) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        if (p_190660_2_) {
            i = i | p_190660_1_;
        } else {
            i = i & ~p_190660_1_;
        }

        this.entityData.set(DATA_FLAGS_ID, (byte)(i & 255));
    }

    public void addChargedStack() {
        chargedStacks++;
    }

    public float getDamageMultiplier() {
        return 1.0F + (chargedStacks * 0.01F);
    }


    public boolean isCharging() {
        return this.getFlag(1);
    }

    public void setIsCharging(boolean charging) {
        this.setFlag(1, charging);
    }

    public int getAttackPhase() {
        return this.entityData.get(ATTACK_PHASE);
    }

    public void setAttackPhase(int phase) {
        this.entityData.set(ATTACK_PHASE, phase);
    }

    public int getChargedStacks() {
        return this.entityData.get(CHARGED_STACKS);
    }

    public void setChargedStacks() {
        if (this.getActiveEffectsMap().containsKey(ModEffects.CHARGED_EFFECT.get())) {
            this.chargedStacks = this.getActiveEffectsMap().get(ModEffects.CHARGED_EFFECT.get()).getAmplifier();
        }
        this.entityData.set(CHARGED_STACKS, this.chargedStacks);
    }

    public void setLightningTicks(int ticks) {
        this.entityData.set(LIGHTNING_TICKS, ticks);
    }

    public int getLightningTicks() {
        return this.entityData.get(LIGHTNING_TICKS);
    }

    @Override
    public FactionType getFaction() {
        return FactionType.GRIMCROW;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_PHASE, 0);
        this.entityData.define(LIGHTNING_TICKS, 0);
        this.entityData.define(CHARGED_STACKS, 0);
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(BREAKBAR_PROGRESS, 1.0f);
    }

    public void setBreakbarProgress(float progress) {
        this.entityData.set(BREAKBAR_PROGRESS, MathHelper.clamp(progress, 0.0F, 1.0F));
    }

    public float getBreakbarProgress() {
        return this.entityData.get(BREAKBAR_PROGRESS);
    }

    public GrimcrowCaptainBossEntity(EntityType<? extends AbstractImperialBossEntity> type, World worldIn) {
        super(type, worldIn);
        this.maxUpStep = 1;
        this.xpReward = 300;
        this.setNoGravity(true);
        this.moveControl = new MoveHelperController(this);
        this.setPersistenceRequired();
    }


    public static AttributeModifierMap.MutableAttribute createCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1600.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 13.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ARMOR_TOUGHNESS, 5);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new GrimcrowLightningTetherGoal(this));
        this.goalSelector.addGoal(4, new ChargeAttackGoal());
        this.goalSelector.addGoal(8, new MoveRandomGoal());
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos p_190651_1_) {
        this.boundOrigin = p_190651_1_;
    }


    public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        if (p_70037_1_.contains("BoundX")) {
            this.boundOrigin = new BlockPos(p_70037_1_.getInt("BoundX"), p_70037_1_.getInt("BoundY"), p_70037_1_.getInt("BoundZ"));
        }
        if (p_70037_1_.contains("ChargedStacks")) {
            this.chargedStacks = p_70037_1_.getInt("ChargedStacks");
            this.setChargedStacks();
        }
    }

    public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
        if (this.boundOrigin != null) {
            p_213281_1_.putInt("BoundX", this.boundOrigin.getX());
            p_213281_1_.putInt("BoundY", this.boundOrigin.getY());
            p_213281_1_.putInt("BoundZ", this.boundOrigin.getZ());
        }
        p_213281_1_.putInt("ChargedStacks", this.getChargedStacks());
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.DIESELYTRA_ENGINE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.PILLAGER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.equals(DamageSource.FALL)) {
            return false;
        }

        if (source == DamageSource.LIGHTNING_BOLT) {
            addChargedStack();
            return false;
        }

        if (source == DamageSource.ON_FIRE) {
            return false;
        }
        if (tetherActive) {
            addBreakDamage(amount);
        }
        return super.hurt(source, amount);
    }

    @Override
    public SoundEvent setBossMusic() {
        return ModSounds.HIGH_OCTANE.get();
    }

    private void summonCrewmates(LivingEntity target) {
        for (int i = 0; i < 4; i++) {
            GrimcrowEntity crewmate = new GrimcrowEntity(ModEntityTypes.GRIMCROW.get(), this.level);
            crewmate.moveTo(target.getX(), target.getY(), target.getZ());
            this.level.addFreshEntity(crewmate);
        }
    }

    public void startLightningTether() {
        tetherActive = true;
        breakBar = 0f; // Reset breakbar when starting new tether
        setBreakbarProgress(1.0f);
        breakBarInfo.setPercent(1.0f);
        breakBarInfo.setVisible(true);
    }

    public void endLightningTether(boolean interrupted) {
        tetherActive = false;
        lightningAttackCooldown = 600; // 30s cooldown at 20tps

        // Hide breakbar when tether ends
        breakBarInfo.setVisible(false);

        if (!interrupted && getTarget() != null) {
            LightningBoltEntity bolt = EntityType.LIGHTNING_BOLT.create(level);
            if (bolt != null) {
                bolt.moveTo(getTarget().getX(), getTarget().getY(), getTarget().getZ());
                level.addFreshEntity(bolt);
            }
        }
    }

    public void addBreakDamage(float amount) {
        if (!tetherActive) return;

        // Add damage to the break bar
        breakBar += amount;

        // Calculate progress (0.0 to 1.0)
        float progress = 1.0f - (breakBar / breakThreshold);
        progress = MathHelper.clamp(progress, 0.0f, 1.0f);

        // Update both the data parameter and boss bar
        setBreakbarProgress(progress);
        breakBarInfo.setPercent(progress);

        // Check if breakbar is broken
        if (breakBar >= breakThreshold) {
            endLightningTether(true);
            // Reset breakbar after breaking
            breakBar = 0f;
            setBreakbarProgress(1.0f);
            breakBarInfo.setPercent(1.0f);
        }
    }


    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide()) {
            if (!this.isAlive() || this.removed) {
                stopBossMusic();
            }
            return;
        }

        // In GrimcrowCaptainBossEntity.tick() method
        if (!tetherActive && getBreakbarProgress() > 0.0F) {
            setBreakbarProgress(0.0F);
        }

        // Update breakbar visibility and sync with data parameter
        if (tetherActive) {
            this.setChargedStacks();
            breakBarInfo.setVisible(true);
            // Ensure client-side sync
            if (this.tickCount % 2 == 0) { // Sync every 5 ticks to reduce network traffic
                breakBarInfo.setPercent(getBreakbarProgress());
            }
        } else {
            breakBarInfo.setVisible(false);
            // Only reset if not already reset
            if (breakBar > 0f) {
                breakBar = 0f;
                setBreakbarProgress(0.0f);
            }
        }

        if (lightningAttackCooldown > 0) lightningAttackCooldown--;

        if (this.getTarget() != null && this.canSee(this.getTarget())) {
            this.crewSummonTicks = 0;
        }

        if (this.getTarget() != null && !this.canSee(this.getTarget()) && !this.level.isClientSide()) {
            this.crewSummonTicks++;
            if (this.crewSummonTicks >= 200) {
                this.getTarget().sendMessage(new TranslationTextComponent("entity.mobius.grimcrow_captain.summon").withStyle(TextFormatting.DARK_RED), this.getTarget().getUUID());
                summonCrewmates(this.getTarget());
                this.crewSummonTicks = 0;
            }
        }

        if (this.getTarget() == null) {
            this.setAttackPhase(3);
        }



        if (postSlamCooldown > 0) {
            postSlamCooldown--;
            this.setAttackPhase(0); // idle animation
            this.setNoGravity(true);
            return; // skip attacking during cooldown
        }


        attackPhaseTicks++;

        if (!chargingAOE && !performingSlam && this.getTarget() != null) {
            // ===== STRIKES PHASE =====
            this.setAttackPhase(3);
            if (attackPhaseTicks < 240) {
                performRelentlessStrikes();
                this.setNoGravity(true);
            } else {
                // Enter windup
                chargingAOE = true;
                this.setNoGravity(true);
                attackPhaseTicks = 0;
                this.setAttackPhase(2);
                if (this.getTarget() != null) {
                    aoeTarget = this.getTarget().position();
                }
                if (!this.level.isClientSide()) {
                    this.playSound(ModSounds.DIESELYTRA_BOOST.get(), 1.5F, 0.8F);
                }

                // particles to telegraph charge
                spawnChargeParticles();
            }
        } else if (chargingAOE && !performingSlam) {
            // ===== WINDUP =====
            this.setAttackPhase(2);
            if (this.getTarget() != null) {
                if (attackPhaseTicks >= 40) { // 2s charge
                    performLeapSlam();
                    this.setNoGravity(true);
                    chargingAOE = false;
                    attackPhaseTicks = 0;
                }
            }
        }

        if (performingSlam) {
            this.setAttackPhase(4);
            slamAirTicks++;
            this.setNoGravity(true);

            if (slamAirTicks >= 20) {
                slamImpact();
                performingSlam = false;
                slamAirTicks = 0;
                this.setAttackPhase(4); // impact animation
            }
        }
    }

    private void spawnChargeParticles() {
        for (int i = 0; i < 10; i++) {
            double offsetX = (this.random.nextDouble() - 0.5D) * 2.0D;
            double offsetZ = (this.random.nextDouble() - 0.5D) * 2.0D;
            double x = this.getX() + offsetX;
            double y = this.getY() + 0.1D;
            double z = this.getZ() + offsetZ;
            if (this.level.isClientSide()) {
                this.level.addParticle(ModParticles.HELLFIRE_PARTICLE.get(), x, y, z, 0, 0.1D, 0);
            }
        }
    }


    private void slamImpact() {
        if (aoeTarget == null) {
            this.setAttackPhase(0);
            return;
        }

        AxisAlignedBB area = new AxisAlignedBB(
                aoeTarget.x - 4, aoeTarget.y - 1, aoeTarget.z - 4,
                aoeTarget.x + 4, aoeTarget.y + 3, aoeTarget.z + 4
        );
        if (this.level.isClientSide()) {
            this.playSound(ModSounds.DIESELYTRA_LANDS.get(), 2.0F, 0.5F);
        }
        for (LivingEntity e : this.level.getEntitiesOfClass(LivingEntity.class, area)) {
            if (e instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) e;
                if (player.isBlocking()) {
                    player.addEffect(new EffectInstance(ModEffects.EXPOSED_EFFECT.get(), 100, 1));
                } else {
                    player.hurt(DamageSource.mobAttack(this), 15.0F);
                    player.addEffect(new EffectInstance(ModEffects.EXPOSED_EFFECT.get(), 100, 2));
                }
            }
        }
        if (this.getTarget() != null) {
            this.moveControl.setWantedPosition(this.getTarget().getX(), this.getTarget().getY(), this.getTarget().getZ(), 1.0D);
        }
        this.postSlamCooldown = 10;
    }


    private void performRelentlessStrikes() {
        LivingEntity target = this.getTarget();
        if (target == null) {
            return;
        }

        if (this.distanceTo(target) >= 4.0D) {
            this.setAttackPhase(3);
        }

        if (this.distanceTo(target) < 4.0D) {
            this.setAttackPhase(1);
            doHurtTarget(target);
        }
    }

    private void performLeapSlam() {
        if (aoeTarget == null) return;

        // Boss leaps into the air
        this.setDeltaMovement(0, 1.0D, 0);
        this.performingSlam = true;
        this.slamAirTicks = 0;

        this.playSound(ModSounds.DIESELYTRA_BOOST.get(), 1.5F, 0.8F);
    }

    class ChargeAttackGoal extends Goal {
        public ChargeAttackGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (GrimcrowCaptainBossEntity.this.getTarget() != null && !GrimcrowCaptainBossEntity.this.getMoveControl().hasWanted() && GrimcrowCaptainBossEntity.this.random.nextInt(3) == 0) {
                return GrimcrowCaptainBossEntity.this.distanceToSqr(GrimcrowCaptainBossEntity.this.getTarget()) > 3.0D;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return GrimcrowCaptainBossEntity.this.moveControl.hasWanted() && GrimcrowCaptainBossEntity.this.isCharging() && GrimcrowCaptainBossEntity.this.getTarget() != null && GrimcrowCaptainBossEntity.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = GrimcrowCaptainBossEntity.this.getTarget();
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            GrimcrowCaptainBossEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            GrimcrowCaptainBossEntity.this.setIsCharging(true);
        }

        public void stop() {
            GrimcrowCaptainBossEntity.this.setIsCharging(false);
        }

        public void tick() {
            LivingEntity livingentity = GrimcrowCaptainBossEntity.this.getTarget();
            if (GrimcrowCaptainBossEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                GrimcrowCaptainBossEntity.this.doHurtTarget(livingentity);
                GrimcrowCaptainBossEntity.this.setAttackPhase(1);
                GrimcrowCaptainBossEntity.this.setIsCharging(false);
            } else {
                double d0 = GrimcrowCaptainBossEntity.this.distanceToSqr(livingentity);
                if (d0 < 9.0D) {
                    Vector3d vector3d = livingentity.getEyePosition(1.0F);
                    GrimcrowCaptainBossEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                }
            }

        }
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(GrimcrowCaptainBossEntity boss) {
            super(boss);
        }

        public void tick() {
            if (this.operation == MovementController.Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.wantedX - GrimcrowCaptainBossEntity.this.getX(), this.wantedY - GrimcrowCaptainBossEntity.this.getY(), this.wantedZ - GrimcrowCaptainBossEntity.this.getZ());
                double d0 = vector3d.length();
                if (d0 < GrimcrowCaptainBossEntity.this.getBoundingBox().getSize()) {
                    this.operation = MovementController.Action.WAIT;
                    GrimcrowCaptainBossEntity.this.setDeltaMovement(GrimcrowCaptainBossEntity.this.getDeltaMovement().scale(0.75D));
                } else {
                    GrimcrowCaptainBossEntity.this.setDeltaMovement(GrimcrowCaptainBossEntity.this.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                    if (GrimcrowCaptainBossEntity.this.getTarget() == null) {
                        Vector3d vector3d1 = GrimcrowCaptainBossEntity.this.getDeltaMovement();
                        GrimcrowCaptainBossEntity.this.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                        GrimcrowCaptainBossEntity.this.yBodyRot = GrimcrowCaptainBossEntity.this.yRot;
                    } else {
                        double d2 = GrimcrowCaptainBossEntity.this.getTarget().getX() - GrimcrowCaptainBossEntity.this.getX();
                        double d1 = GrimcrowCaptainBossEntity.this.getTarget().getZ() - GrimcrowCaptainBossEntity.this.getZ();
                        GrimcrowCaptainBossEntity.this.yRot = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                        GrimcrowCaptainBossEntity.this.yBodyRot = GrimcrowCaptainBossEntity.this.yRot;
                    }
                }

            }
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            return !GrimcrowCaptainBossEntity.this.moveControl.hasWanted() && GrimcrowCaptainBossEntity.this.random.nextInt(7) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = GrimcrowCaptainBossEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = GrimcrowCaptainBossEntity.this.blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(GrimcrowCaptainBossEntity.this.random.nextInt(15) - 7, GrimcrowCaptainBossEntity.this.random.nextInt(11) - 5, GrimcrowCaptainBossEntity.this.random.nextInt(15) - 7);
                if (GrimcrowCaptainBossEntity.this.level.isEmptyBlock(blockpos1)) {
                    GrimcrowCaptainBossEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (GrimcrowCaptainBossEntity.this.getTarget() == null) {
                        GrimcrowCaptainBossEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<GrimcrowCaptainBossEntity> controller = new AnimationController<>(this, "controller", 6, this::predicate);
        data.addAnimationController(controller);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<GrimcrowCaptainBossEntity> event) {
        String baseAnimationName = "animation.grimcrow_captain.";
        switch (this.getAttackPhase()) {
            case 1: // strikes
                playAnimation(event,baseAnimationName + "attack", "LOOP");
                break;
            case 2: // windup
                playAnimation(event,baseAnimationName + "windup", "PLAY_ONCE");
                break;
            case 3: // leap
                playAnimation(event, baseAnimationName + "leap", "LOOP");
                break;
            case 4: // slam impact
                playAnimation(event, baseAnimationName + "slam", "PLAY_ONCE");
                break;
            default:
                playAnimation(event, baseAnimationName + "idle", "LOOP");
                break;
        }
        return PlayState.CONTINUE;
    }

    private void playAnimation(AnimationEvent<GrimcrowCaptainBossEntity> event ,String animationName, String shouldLoop) {
        ILoopType animationLoopType = ILoopType.EDefaultLoopTypes.valueOf(shouldLoop);
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, animationLoopType));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}

