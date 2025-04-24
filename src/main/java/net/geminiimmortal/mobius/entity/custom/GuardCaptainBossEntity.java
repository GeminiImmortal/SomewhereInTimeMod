package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.goals.ChargeAttackGoal;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.geminiimmortal.mobius.network.PlayMusicPacket;
import net.geminiimmortal.mobius.sound.ClientMusicHandler;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class GuardCaptainBossEntity extends MonsterEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

    IFormattableTextComponent rank = (StringTextComponent) new StringTextComponent("[CHAMPION FOE] ").setStyle(Style.EMPTY.withColor(TextFormatting.GOLD).withBold(true));
    IFormattableTextComponent name = (StringTextComponent) new StringTextComponent("Darius, Captain of the Royal Guard").setStyle(Style.EMPTY.withColor(TextFormatting.DARK_RED).withBold(false));
    IFormattableTextComponent namePlate = rank.append(name);

    private final ServerBossInfo bossInfo = new ServerBossInfo(
            namePlate,  // Boss name
            BossInfo.Color.RED,
            BossInfo.Overlay.NOTCHED_10
    );

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
    public void startSeenByPlayer(ServerPlayerEntity player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayerEntity player) {
        super.stopSeenByPlayer(player);

        this.bossInfo.removePlayer(player);
    }

    public GuardCaptainBossEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
        this.shouldDropExperience();
    }

    private int chargeCooldown = 0;

    @Override
    public void tick() {
        super.tick();
        if (chargeCooldown > 0) chargeCooldown--;
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public void spawnCleaveParticles() {
        if (!(this.level instanceof ServerWorld)) return;
        ServerWorld serverWorld = (ServerWorld) this.level;

        Vector3d forward = this.getForward();
        Vector3d origin = this.position().add(0, 1.0, 0); // a bit above feet

        for (int angle = -60; angle <= 60; angle += 10) {
            double radians = Math.toRadians(angle);
            double x = forward.x * Math.cos(radians) - forward.z * Math.sin(radians);
            double z = forward.x * Math.sin(radians) + forward.z * Math.cos(radians);
            Vector3d offset = new Vector3d(x, 0, z).normalize().scale(1.5);

            if(this.level.isClientSide){
                this.level.addParticle(ParticleTypes.SWEEP_ATTACK,
                        origin.x + offset.x,
                        origin.y,
                        origin.z + offset.z,
                        0, 0, 0);
            }

            serverWorld.sendParticles(
                    ParticleTypes.SWEEP_ATTACK,
                    origin.x + offset.x,
                    origin.y,
                    origin.z + offset.z,
                    1,
                    0, 0, 0,
                    0.05
            );

        }
    }





    public boolean canCharge() {
        return chargeCooldown <= 0;
    }

    public void setChargeCooldown(int ticks) {
        this.chargeCooldown = ticks;
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new ChargeAttackGoal(this));
    //    this.goalSelector.addGoal(2, new ExecutionAttackGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 250.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.7D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    // Animation logic
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 10, event -> {
            if (this.swinging) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illagerOverlord.attack", false));
                spawnCleaveParticles();
                return PlayState.CONTINUE;
            }
            if (event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illagerOverlord.dash", true));

            } else if (!event.isMoving()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illagerOverlord.idleHostile", true));

            }

            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.CAPTAIN_HURTS.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.CAPTAIN_HURTS.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }
}

