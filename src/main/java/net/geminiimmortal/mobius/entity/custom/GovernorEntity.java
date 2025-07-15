package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.entity.goals.GovernorSummonCloneGoal;
import net.geminiimmortal.mobius.entity.goals.util.TeleportUtil;
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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class GovernorEntity extends AbstractImperialBossEntity implements IAnimatable {
    private static final String[] TAUNTS = {
            "Over here, dumbo!", "Don't blink! You might miss me! HA!",
            "Hit me already, fool!", "Stop trying to hit me and hit me!",
            "Think fast!"
    };

    private static final DataParameter<Integer> GCD = EntityDataManager.defineId(GovernorEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> GRINNING = EntityDataManager.defineId(GovernorEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CORRECT_HIT = EntityDataManager.defineId(GovernorEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FIGHT_STARTED = EntityDataManager.defineId(GovernorEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> INTRO_OVER = EntityDataManager.defineId(GovernorEntity.class, DataSerializers.BOOLEAN);
    AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public GovernorEntity(EntityType<? extends CreatureEntity> entityType, World worldIn) {
        super(entityType, worldIn);
        this.setPersistenceRequired();
        this.setNoAi(true);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GCD, 140);
        this.entityData.define(GRINNING, false);
        this.entityData.define(CORRECT_HIT, false);
        this.entityData.define(FIGHT_STARTED, false);
        this.entityData.define(INTRO_OVER, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.ARMOR, 15.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 2.5D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.7D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new GovernorSummonCloneGoal(this));
        this.goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 30.0f));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
    }

    @Override
    protected void playBossMusic() {
        super.playBossMusic();
    }

    @Override
    public void tick() {
        if (level.isClientSide) {
            if (!this.isAlive() || this.removed) {
                stopBossMusic();
            }
        }
        if (this.getFightStarted()) {

            decrementCloneSummonCooldown();
        }
        this.setBossbarPercent();

        if (this.hurtTime > 0 && !this.getFightStarted()) {
            this.setGCD(112);
            this.setFightStarted(true);
            this.setNoAi(false);
        }
        super.tick();
    }

    public boolean getFightStarted() { return this.entityData.get(FIGHT_STARTED); }

    public void setFightStarted(boolean fightStarted) { this.entityData.set(FIGHT_STARTED, fightStarted); }

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

    public boolean getCorrectHit() {
        return this.entityData.get(CORRECT_HIT);
    }

    public void setCorrectHit(boolean correctHit) {
        this.entityData.set(CORRECT_HIT, correctHit);
    }

    public void preCloneSummonEvent() {
        ServerWorld world = (ServerWorld) this.level;

        if (this.level != null && !this.level.isClientSide()) {
            setCorrectHit(false);
            ServerPlayerEntity target = (ServerPlayerEntity) this.level.getNearestPlayer(this, 50);

            this.addEffect(new EffectInstance(Effects.INVISIBILITY, 5, 1));

            this.playSound(ModSounds.GOVERNOR_POOF.get(), 12.0F, 1.0F);
            setCorrectHit(false);
            world.sendParticles(
                    ParticleTypes.CLOUD,
                    this.getX(), this.getY(), this.getZ(),
                    20, 0.1, 0.1, 0.1, 0.1
            );
            setCorrectHit(false);
            if (target != null) {
                this.playSound(ModSounds.GOVERNOR_LAUGH.get(), 50.0f, 1.0f);

                this.moveTo(target.getX(), this.getY(), target.getZ());
            }
            setCorrectHit(false);
            Vector3d safePos = TeleportUtil.findSafeTeleportPosition(this.level, this, 9, 50);
            if (safePos != null) {
                this.moveTo(safePos);
                setCorrectHit(false);
            }
            if (target != null) {
                double dx = target.getX() - this.getX();
                double dz = target.getZ() - this.getZ();

                float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx))) - 90F;

                this.yRot = yaw;
                this.yHeadRot = yaw;
                this.yBodyRot = yaw;
            }
        }
    }

    public void decrementCloneSummonCooldown() {
        int current = getGCD();

        if (current > 0) {
            setGCD(current - 1);
        }
        if (current <= 0) {
            if (!getCorrectHit()) {
                setGCD(100);
                setGrinning(true);
            }
            if (getCorrectHit()) {
                setGCD(200);
                setGrinning(false);
            }
        }
        if (current == 105) {
            // Taunt nearby players
            if (!this.level.isClientSide) {
                List<ServerPlayerEntity> players = this.level.getEntitiesOfClass(ServerPlayerEntity.class, this.getBoundingBox().inflate(50));
                for (ServerPlayerEntity player : players) {
                    player.sendMessage(
                            new StringTextComponent(TAUNTS[random.nextInt(TAUNTS.length)]).withStyle(TextFormatting.DARK_PURPLE),
                            player.getUUID()
                    );
                }
            }
            setGrinning(true);
        }
        if (current < 100 && !this.getCorrectHit() && !this.level.isClientSide()) {
            if (this.hurtTime == 10 && current < 98 && !Objects.equals(this.getLastDamageSource(), DamageSource.ON_FIRE)) {
                setCorrectHit(true);
            }
        }
    }

    @Override
    public void stopBossMusic() {
        super.stopBossMusic();
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.setFightStarted(nbt.getBoolean("HasFightStarted"));
        this.setGCD(nbt.getInt("GlobalCooldown"));
        this.entityData.set(INTRO_OVER, nbt.getBoolean("IntroOver"));
        super.load(nbt);
    }

    @Override
    public boolean save(CompoundNBT nbt) {
        nbt.putBoolean("HasFightStarted", this.getFightStarted());
        nbt.putInt("GlobalCooldown", this.getGCD());
        nbt.putBoolean("IntroOver", this.entityData.get(INTRO_OVER));
        return super.save(nbt);
    }

    @Override
    public void die(DamageSource source) {
        this.level.setBlock(this.blockPosition(), ModBlocks.GOVERNOR_BOSS_EXIT_BLOCK.get().defaultBlockState(),3);
        super.die(source);
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
