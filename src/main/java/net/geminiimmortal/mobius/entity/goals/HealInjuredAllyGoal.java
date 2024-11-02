package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.ClubGolemEntity;
import net.geminiimmortal.mobius.entity.custom.DiamondGolemEntity;
import net.geminiimmortal.mobius.entity.custom.HeartGolemEntity;
import net.geminiimmortal.mobius.entity.custom.SpadeGolemEntity;
import net.geminiimmortal.mobius.entity.model.SpadeGolem;
import net.geminiimmortal.mobius.network.ParticlePacket;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.EnumSet;
import java.util.List;

public class HealInjuredAllyGoal extends Goal {
    private final MobEntity healer; // Hearts Golem
    public LivingEntity targetAlly; // The ally to heal
    private final double speed; // Movement speed toward the ally
    private final float healRange; // Range to apply healing
    private final float healAmount; // Amount of healing applied
    private final int cooldownTime; // Cooldown between heals
    private int cooldownTimer; // Tracks cooldown
    private final HeartGolemEntity golem;
    private int particleTimer = 0;
    private int attackAnimationTimer;
    private final int attackAnimationDuration = 40;
    //private boolean isHealing;

    public HealInjuredAllyGoal(HeartGolemEntity golem,MobEntity healer, double speed, float healRange, int healAmount, int cooldownTime) {
        this.healer = healer;
        this.speed = speed;
        this.healRange = healRange;
        this.healAmount = healAmount;
        this.cooldownTime = cooldownTime;
        this.cooldownTimer = 0;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.golem = golem;
        //this.isHealing = false;

      //  this.targetAlly = targetAlly;
    }

    public void playAttackAnimation() {
        this.attackAnimationTimer = 60;
        this.golem.setHealing(true);
    }


    private int getAttackAnimationDuration() {
        return attackAnimationDuration;
    }

    private int getAttackAnimationTimer() {
        return this.attackAnimationTimer;
    }

    // Check if there is an ally that needs healing
    @Override
    public boolean canUse() {
        if (cooldownTimer > 0) {
            cooldownTimer--;
            return false;

        }

        targetAlly = findInjuredAlly();
        this.golem.setHealing(false);
        return targetAlly != null;
    }

    // Continue if still healing or moving toward the target ally
    @Override
    public boolean canContinueToUse() {
        return targetAlly != null && targetAlly.isAlive() && targetAlly.getHealth() < targetAlly.getMaxHealth();
    }

    // Start moving towards the injured ally
    @Override
    public void start() {
        //    this.healer.getNavigation().moveTo(targetAlly, speed);
        this.particleTimer = 100;
        if (targetAlly != null) {
            double distanceToAlly = golem.distanceTo(targetAlly);
            World world = golem.level;

            double startX = golem.getX();
            double startY = golem.getY() + golem.getEyeHeight(); // Adjust height to eye level
            double startZ = golem.getZ();

            double endX = targetAlly.getX();
            double endY = targetAlly.getY() + targetAlly.getEyeHeight();
            double endZ = targetAlly.getZ();

            // Calculate the number of particles and interpolate their position
            int particleCount = 10; // Example count
            for (int i = 0; i < particleCount; i++) {
                double t = (double) i / (particleCount - 1);
                double particleX = startX + (endX - startX) * t;
                double particleY = startY + (endY - startY) * t;
                double particleZ = startZ + (endZ - startZ) * t;


                // If the golem is too far away, move closer but stay within healing range
                if (distanceToAlly > healRange) {
                    golem.getNavigation().moveTo(targetAlly, speed);
                } else if (distanceToAlly <= healRange && !(this.cooldownTimer > 0)) {
                    // If within healing range, heal the ally
                    this.healAlly(targetAlly);



                }
            }
        }
    }

    // Tick method is called every game tick to handle the healing
    @Override
    public void tick() {
        this.particleTimer = 100;
        if (targetAlly != null) {
            double distanceToAlly = golem.distanceTo(targetAlly);
            World world = golem.level;

            double startX = golem.getX();
            double startY = golem.getY() + golem.getEyeHeight(); // Adjust height to eye level
            double startZ = golem.getZ();

            double endX = targetAlly.getX();
            double endY = targetAlly.getY() + targetAlly.getEyeHeight();
            double endZ = targetAlly.getZ();

            // Calculate the number of particles and interpolate their position
            int particleCount = 10; // Example count
            for (int i = 0; i < particleCount; i++) {
                double t = (double) i / (particleCount - 1);
                double particleX = startX + (endX - startX) * t;
                double particleY = startY + (endY - startY) * t;
                double particleZ = startZ + (endZ - startZ) * t;
                if (this.attackAnimationTimer >= 0) {
                    attackAnimationTimer--;
                    ParticlePacket packet = new ParticlePacket(particleX, particleY, particleZ, "HAPPY_VILLAGER"); // or your chosen particle type
                    MobiusMod.NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), packet);
                } else {
                    this.golem.setHealing(false);
                    this.stop();
                }
            }
        }
    }


            // Called when the healing action completes
            @Override
            public void stop () {
                targetAlly = null;
                cooldownTimer = cooldownTime;// Reset the cooldown
            }

            private LivingEntity findInjuredAlly () {
                List<LivingEntity> allies = healer.level.getEntitiesOfClass(LivingEntity.class,
                        healer.getBoundingBox().inflate(10.0D),
                        entity -> isAlly(entity) && entity.isAlive() && entity.getHealth() < entity.getMaxHealth());

                if (!allies.isEmpty()) {
                    return allies.get(0); // Return the first or closest injured ally
                }

                return null;
            }

            // Apply healing to the ally
            private void healAlly (LivingEntity ally){
                ally.heal(healAmount); // Heals the ally by the specified amount
                this.cooldownTimer = cooldownTime; // Set the cooldown after healing
                healer.level.playSound(null, healer.getX(), healer.getY(), healer.getZ(), ModSounds.HEART_GOLEM_HEAL.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
                this.playAttackAnimation();

            }

            private boolean isAlly (LivingEntity entity){
                return entity instanceof HeartGolemEntity || entity instanceof ClubGolemEntity || entity instanceof DiamondGolemEntity|| entity instanceof SpadeGolemEntity;
            }

            public void spawnHealingParticles (World world, Entity healer, Entity target){
                // The number of particles and the steps between them
                int particleCount = 200;

                // Get positions of healer and target
                double healerX = healer.getX();
                double healerY = healer.getY() + healer.getEyeHeight();
                double healerZ = healer.getZ();

                double targetX = target.getX();
                double targetY = target.getY() + target.getEyeHeight();
                double targetZ = target.getZ();


                    // Loop to spawn particles along the path
                    for (int i = 0; i < particleCount; i++) {
                        double t = (double) i / (particleCount - 1);
                        double x = healerX + (targetX - healerX) * t;
                        double y = healerY + (targetY - healerY) * t;
                        double z = healerZ + (targetZ - healerZ) * t;

                        // Spawn the green particles (use your chosen particle type)
                        world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.05D, 0.0D);
                        //System.out.println("Spawned particle at " + x + y + z);
                    }

            }

}

