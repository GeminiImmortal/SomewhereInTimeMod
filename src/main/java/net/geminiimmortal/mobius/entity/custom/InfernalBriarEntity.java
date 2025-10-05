package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.goals.ShootFireballGoal;
import net.geminiimmortal.mobius.faction.FactionType;
import net.geminiimmortal.mobius.faction.IFactionCarrier;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtWithoutMovingGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Random;

public class InfernalBriarEntity extends MobEntity implements IAnimatable, IFactionCarrier, IMob {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private int particleTickCounter = 0;
    private static final int PARTICLE_SPAWN_INTERVAL = 5;




    public InfernalBriarEntity(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
        this.dropExperience();
        this.fireImmune();
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isAggressive() {
        return true;
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        return ActionResultType.FAIL;
    }

    public static boolean canMobSpawn(EntityType<? extends MobEntity> entityType,
                                      IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
        ServerWorld level = world.getLevel();

        int existing = level.getEntitiesOfClass(InfernalBriarEntity.class,
                new AxisAlignedBB(pos).inflate(28)).size();
        int quartermasters = level.getEntitiesOfClass(RebelQuartermasterEntity.class,
                new AxisAlignedBB(pos).inflate(70)).size();
        int villagers = level.getEntitiesOfClass(VillagerEntity.class,
                new AxisAlignedBB(pos).inflate(50)).size();

        long timeOfDay = level.getDayTime() % 24000;
        boolean isMobius = level.dimension() == ModDimensions.MOBIUS_WORLD;
        boolean isNight = (isMobius && (timeOfDay >= 13600 && timeOfDay <= 23000)) || (level.isThundering() && isMobius);
        boolean isPeaceful = level.getDifficulty() == Difficulty.PEACEFUL;

        BlockState ground = level.getBlockState(pos.below());
        boolean validGround = ground.is(ModBlocks.SOUL_PODZOL.get())
                || ground.is(ModBlocks.HEMATITE.get())
                || ground.is(ModBlocks.AURORA_GRASS_BLOCK.get());

        boolean darkEnough = level.getBrightness(LightType.BLOCK, pos) <= 7;

        boolean canSeeSky = level.canSeeSkyFromBelowWater(pos);
        if (canSeeSky && timeOfDay < 13600 && !level.isThundering() && !darkEnough) {
            return false;
        }

        return validGround && isNight && darkEnough && existing < 1 && quartermasters < 1 && !isPeaceful && villagers < 1;
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1D);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        this.goalSelector.addGoal(1, new ShootFireballGoal(this, 30.0D, 40));
        this.goalSelector.addGoal(5, new LookAtWithoutMovingGoal(this, PlayerEntity.class, 30f, 1f));
    }

    protected int getXpToDrop() {
        int baseXp = this.random.nextInt(2) + 2;
        return baseXp;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!this.level.isClientSide()) {
            int experiencePoints = this.getXpToDrop();

            // Drop the experience orbs
            while (experiencePoints > 0) {
                int experienceToDrop = experiencePoints;
                experiencePoints -= experienceToDrop;
                this.level.addFreshEntity(new ExperienceOrbEntity(this.level, this.getX(), this.getY(), this.getZ(), experienceToDrop));
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide && this.level.isDay() && !this.level.getLevelData().isThundering()) {
            this.remove();
        }

        if(this.level.getLevelData().getDifficulty().equals(Difficulty.PEACEFUL)) {
            this.remove();
        }

        particleTickCounter++;

        if (particleTickCounter >= PARTICLE_SPAWN_INTERVAL) {
            spawnGlowParticle();
            particleTickCounter = 0;
        }
    }

    private void spawnGlowParticle() {
        for (int i = 0; i < 1; i++) {
            this.level.addParticle(ParticleTypes.FLAME,
                    this.getX() + (Math.random() - 0.5) * 2,
                    this.getY() + 1.0,
                    this.getZ() + (Math.random() - 0.5) * 2,
                    0, 0.01, 0);
        }
    }

    @Override
    protected SoundEvent getAmbientSound(){
        return ModSounds.INFERNAL_BRIAR_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CROP_BREAK;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.CROP_BREAK;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<InfernalBriarEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        data.addAnimationController(controller);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.targetSelector.getRunningGoals().getClass().getCanonicalName() != null) {
            if ((this.targetSelector.getRunningGoals().getClass().getCanonicalName().equals(NearestAttackableTargetGoal.class.getCanonicalName()) &&
                    this.targetSelector.getRunningGoals().getClass().getCanonicalName() != null)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.infernal_briar.attack", true));
                return PlayState.CONTINUE;
            }
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.infernal_briar.idle", true));
        return PlayState.CONTINUE;
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public FactionType getFaction() {
        return FactionType.DANGEROUS_TO_VILLAGES;
    }
}


