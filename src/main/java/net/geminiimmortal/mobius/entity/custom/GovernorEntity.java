package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.entity.goals.*;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.geminiimmortal.mobius.util.TitleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public class GovernorEntity extends VindicatorEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final DataParameter<Boolean> CASTING = EntityDataManager.defineId(GovernorEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLEEING = EntityDataManager.defineId(GovernorEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CLONED = EntityDataManager.defineId(GovernorEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> GLOBAL_COOLDOWN = EntityDataManager.defineId(GovernorEntity.class, DataSerializers.INT);
    private static final int G_COOLDOWN_DURATION = 160;
    private int gcd = 160;
    private final List<CloneEntity> activeClones = new ArrayList<>();
    private static final int MAX_CLONES = 6;
    private static boolean isAttacking;

    IFormattableTextComponent rank = (StringTextComponent) new StringTextComponent("[CHAMPION FOE] ").setStyle(Style.EMPTY.withColor(TextFormatting.GOLD).withBold(true));
    IFormattableTextComponent name = (StringTextComponent) new StringTextComponent("His Lordship, The Governor").setStyle(Style.EMPTY.withColor(TextFormatting.DARK_BLUE).withBold(false));
    IFormattableTextComponent namePlate = rank.append(name);

    private final ServerBossInfo bossInfo = new ServerBossInfo(
            namePlate,  // Boss name
            BossInfo.Color.PURPLE,
            BossInfo.Overlay.NOTCHED_20
    );

    public List<CloneEntity> getActiveClones() {
        return activeClones;
    }

    public void addClone(CloneEntity cloneEntity) {
        activeClones.add(cloneEntity);
    }

    public boolean canSummonMoreClones() {
        return activeClones.size() < MAX_CLONES;
    }

    public GovernorEntity(EntityType<? extends VindicatorEntity> type, World worldIn) {
        super(type, worldIn);
        this.dropExperience();
        this.maxUpStep = 1;
        this.setPersistenceRequired();
        this.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.DIAMOND_SWORD));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false; // Prevent despawning when the player moves far away
    }

    @Override
    public boolean save(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsPersistentBoss", true);
        return true;
    }

    @Override
    public void load(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        if (tag.getBoolean("IsPersistentBoss")) {
            this.setPersistenceRequired();
        }
    }

    @Override
    public void checkDespawn() {
        // Do nothing to prevent the boss from despawning
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CASTING, false);
        this.entityData.define(FLEEING, false);
        this.entityData.define(CLONED, false);
        this.entityData.define(GLOBAL_COOLDOWN, 160);
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
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(8, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(7, new GovernorCloneSummonGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }


    protected int getXpToDrop() {
        int baseXp = this.random.nextInt(50) + 20;
        return baseXp;
    }


    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (!this.level.isClientSide()) {
            int experiencePoints = this.getXpToDrop();
            TitleUtils.sendTitle((ServerPlayerEntity) this.lastHurtByPlayer, "DUTY COMPLETE", "Legendary foe vanquished!", 10, 100, 40);

            // Drop the experience orbs
            while (experiencePoints > 0) {
                int experienceToDrop = experiencePoints;
                experiencePoints -= experienceToDrop;
                this.level.addFreshEntity(new ExperienceOrbEntity(this.level, this.getX(), this.getY(), this.getZ(), experienceToDrop));
            }

            BlockPos deathSpot = new BlockPos(this.getX(), this.getY() + 1, this.getZ());
            this.level.setBlock(deathSpot, ModBlocks.GOVERNOR_BOSS_EXIT_BLOCK.get().defaultBlockState(), 3);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(!(this.getTarget() == null)) {
            if (shouldAttackPlayer((PlayerEntity) this.getTarget())) {
                isAttacking = true;
            }
        }

        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if (this.gcd < 0) { this.gcd = G_COOLDOWN_DURATION; }
           else { this.gcd--; }

        // Get the mob's velocity
        Vector3d motion = this.getDeltaMovement();

        // Check if the mob is moving
        if (motion.x != 0 || motion.y != 0 || motion.z != 0) {
            this.setFleeing(true);
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayerEntity player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
        if(FMLEnvironment.dist == Dist.CLIENT) {
        //    ClientMusicHandler.playGovernorBossMusic();
        //    ClientMusicHandler.stopCustomMusic(Minecraft.getInstance());
        }
        /*if(FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
        //    PlayMusicPacket packet = new PlayMusicPacket("governor_start");
            ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
        }*/
    }

    @Override
    public void stopSeenByPlayer(ServerPlayerEntity player) {
        super.stopSeenByPlayer(player);

        if (FMLEnvironment.dist == Dist.CLIENT) {
        //    ClientMusicHandler.setGovernor(false);
        //    ClientMusicHandler.stopCustomMusic(Minecraft.getInstance());
        }
        /*if(FMLEnvironment.dist == Dist.DEDICATED_SERVER) {

        //    PlayMusicPacket packet = new PlayMusicPacket("governor_stop");
            ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
        }*/

        this.bossInfo.removePlayer(player);
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
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){
        return SoundEvents.PILLAGER_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BEACON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }


    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<GovernorEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        AnimationController<GovernorEntity> alertedController = new AnimationController<>(this, "alertedController", 0, this::alertedPredicate);
        AnimationController<GovernorEntity> attackController = new AnimationController<>(this, "attackController", 0, this::attackPredicate);

        data.addAnimationController(attackController);
        data.addAnimationController(controller);
        data.addAnimationController(alertedController);

    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (!this.getFleeing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.idle", true));
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.run", true));
            return PlayState.CONTINUE;
        }
    }


    private <E extends IAnimatable> PlayState alertedPredicate(AnimationEvent<E> event) {
        if (this.getCasting()) {
           event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.attack", false));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if (this.swinging) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.sword_attack", false));
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.idle", true));
            isAttacking = false;
            return PlayState.CONTINUE;
        }
    }

    public boolean shouldAttackPlayer(PlayerEntity player) {
        double distance = this.distanceTo(player);
        return distance < 2.4;
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public void setCasting(boolean alerted) {
        this.entityData.set(CASTING, alerted);
    }

    public void setGlobalCooldown(int cd) {
        this.entityData.set(GLOBAL_COOLDOWN, cd);
    }

    public int getGlobalCooldown() {
        return this.entityData.get(GLOBAL_COOLDOWN);
    }

    public void setFleeing(boolean fleeing) { this.entityData.set(FLEEING, fleeing); }
    public void setCloned(boolean cloned) { this.entityData.set(CLONED, cloned); }
    public boolean getCasting() {
        return this.entityData.get(CASTING);
    }
    public boolean getCloned() { return this.entityData.get(CLONED); }
    public boolean getFleeing() {
        return this.entityData.get(FLEEING);
    }



}


