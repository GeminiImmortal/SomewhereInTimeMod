package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.item.ModItems;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
import java.util.UUID;

public class FuyukazeEntity extends WolfEntity implements IAnimatable {
    private static final DataParameter<Boolean> SITTING = EntityDataManager.defineId(FuyukazeEntity.class, DataSerializers.BOOLEAN);


    public FuyukazeEntity(EntityType<? extends WolfEntity> type, World worldIn) {
        super(type, worldIn);
        this.fireImmune();
        this.maxUpStep = 1;
        this.setTame(false);
    }
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return CreatureEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 25.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0D);
    }


    @Override
    public FuyukazeEntity getBreedOffspring(ServerWorld world, AgeableEntity mate) {
        FuyukazeEntity baby = ModEntityTypes.FUYUKAZE.get().create(world);
        System.out.println("Breeding attempted...");
        UUID uuid = this.getOwnerUUID();
        if (uuid != null) {
            baby.setOwnerUUID(uuid);
            baby.setTame(true);
        }
        return baby;
    }

    @Override
    public boolean canMate(AnimalEntity mate) {
        if (mate == this) {
            return false;
        } else if (!this.isTame()) {
            return false;
        } else if (!(mate instanceof FuyukazeEntity)) {
            return false;
        } else {
            FuyukazeEntity fuyukazeEntity = (FuyukazeEntity) mate;
            if (!fuyukazeEntity.isTame()) {
                return false;
            } else if (fuyukazeEntity.isInSittingPose()) {
                return false;
            } else {
                return this.isInLove() && fuyukazeEntity.isInLove();
            }
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return item == ModItems.RAW_FAE_VENISON.get().getItem();
    }


    @Override
    protected void registerGoals() {

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, FaecowEntity.class, true));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, FaedeerEntity.class, true));

        // Basic wolf goals
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.1D, true));
        this.goalSelector.addGoal(3, new SitGoal(this));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(6, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
    }

    public void setSitting(Boolean bool){
        this.entityData.set(SITTING, bool);
    }

    public boolean getSitting() {
        return this.entityData.get(SITTING);
    }


    @Override
    public void setOrderedToSit(boolean orderedToSit) {
        if (!this.level.isClientSide()) {
            if (this.getOwner() != null) {
                ServerPlayerEntity owner = (ServerPlayerEntity) this.getOwner();
                if (this.isOrderedToSit() && this.isTame()) {
                    assert owner != null;
                    owner.displayClientMessage(new TranslationTextComponent("entity.mobius.harukaze.is_sitting.true") {
                    }, true);
                    this.setSitting(false);
                    super.setOrderedToSit(orderedToSit);
                } else if (!this.isOrderedToSit() && this.isTame()) {
                    assert owner != null;
                    owner.displayClientMessage(new TranslationTextComponent("entity.mobius.harukaze.is_sitting.false") {
                    }, true);
                    this.setSitting(true);
                    super.setOrderedToSit(orderedToSit);
                } else {
                    assert owner != null;
                    owner.displayClientMessage(new TranslationTextComponent("entity.mobius.harukaze.is_sitting.false") {
                    }, true);
                    this.setSitting(true);
                    super.setOrderedToSit(orderedToSit);
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<FuyukazeEntity> controller = new AnimationController<>(this, "controller", 0, this::fuyukazeController);
        data.addAnimationController(controller);
    }


    private static final RawAnimation IDLE = new RawAnimation("animation.fuyukaze.idle", true);
    private static final RawAnimation RUN = new RawAnimation("animation.fuyukaze.run", true);
    private static final RawAnimation SIT = new RawAnimation("animation.fuyukaze.sit", true);

    private <E extends IAnimatable> PlayState fuyukazeController(AnimationEvent<E> event) {
        FuyukazeEntity entity = (FuyukazeEntity) event.getAnimatable();

        if (entity.hurtTime > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(RUN.animationName));
            this.setSitting(false);
            return PlayState.CONTINUE;
        }


        if (entity.getSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SIT.animationName));
            return PlayState.CONTINUE;
        }

        if (entity.getDeltaMovement().length() > 0.1) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(RUN.animationName));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation(IDLE.animationName));
        return PlayState.CONTINUE;
    }


    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        if (this.isTame() || !this.isAlive()) {
            return super.mobInteract(player, hand);
        }



        Item item = player.getItemInHand(hand).getItem();


        if (item == ModItems.RAW_MANA.get()) {
            if (!player.isCreative()) {
                player.getItemInHand(hand).shrink(1);
            }

            if (!this.level.isClientSide) {
                if (this.random.nextInt(3) == 0) { // 33% chance to tame
                    this.tame(player);
                    this.setOrderedToSit(true);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.level.broadcastEntityEvent(this, (byte) 7); // Heart particles
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6); // Smoke particles
                }
            }
            return ActionResultType.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    public static boolean canMobSpawn(EntityType<? extends AnimalEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        return (world.getBlockState(pos.below()) == ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState());
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.HARUKAZE_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15f, 0.7f);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.HARUKAZE_AMBIENT.get();
    }

    @Override
    public boolean canAttack(LivingEntity type) {
        return super.canAttack(type);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        spawnParticles();
    }

    private void spawnParticles() {
        for (int i = 0; i < 1; i++) {
            this.level.addParticle(ParticleTypes.FLAME,
                    this.getX() + (Math.random() - 0.5) * 2,
                    this.getY() + 1.0,
                    this.getZ() + (Math.random() - 0.5) * 2,
                    0.1, 0.01, 0.1);
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void addPersistentAngerSaveData(CompoundNBT p_233682_1_) {
        super.addPersistentAngerSaveData(p_233682_1_);
    }

    @Override
    public void readPersistentAngerSaveData(ServerWorld p_241358_1_, CompoundNBT p_241358_2_) {
        super.readPersistentAngerSaveData(p_241358_1_, p_241358_2_);
    }

    @Override
    public void updatePersistentAnger(ServerWorld p_241359_1_, boolean p_241359_2_) {
        super.updatePersistentAnger(p_241359_1_, p_241359_2_);
    }

    @Override
    public boolean isAngryAt(LivingEntity p_233680_1_) {
        return super.isAngryAt(p_233680_1_);
    }

    @Override
    public boolean isAngryAtAllPlayers(World p_241357_1_) {
        return super.isAngryAtAllPlayers(p_241357_1_);
    }

    @Override
    public boolean isAngry() {
        return super.isAngry();
    }

    @Override
    public void stopBeingAngry() {
        super.stopBeingAngry();
    }
}
