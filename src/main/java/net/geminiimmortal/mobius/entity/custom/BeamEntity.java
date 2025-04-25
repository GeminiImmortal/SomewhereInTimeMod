package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

public class BeamEntity extends Entity {
    private double xSpeed, ySpeed, zSpeed;
    private PlayerEntity target;
    private int duration;
    private IParticleData particleEffect;
    private int particleTickCounter;

    public BeamEntity(EntityType<? extends BeamEntity> type, World world) {
        super(type, world);
    }

    public BeamEntity(World world, double x, double y, double z) {
        this(ModEntityTypes.BEAM_ENTITY.get(), world); // if you're using DeferredRegister
        this.setPos(x, y, z);
    }

    public void setDirection(double xSpeed, double ySpeed, double zSpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
    }

    public void setTarget(PlayerEntity target) {
        this.target = target;
    }

    public void setParticleEffect(IParticleData particleEffect) {
        this.particleEffect = particleEffect;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void tick() {
        super.tick();

        // Move the beam in the direction it was set
        this.setPos(this.getX() + xSpeed, this.getY() + ySpeed, this.getZ() + zSpeed);

        // Spawn particles at the current position
        if (this.level instanceof ServerWorld) {
            ((ServerWorld) this.level).sendParticles(particleEffect, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0.1);
        }

        // Check if the beam is touching the player and deal damage
        if (target != null && this.distanceTo(target) < 1.0) {
            target.hurt(DamageSource.MAGIC, 2.0F); // Damage amount
        }

        // End the beam if the duration is over
        if (duration > 0) {
            duration--;
        } else {
            this.remove();
        }

        particleTickCounter++;

        int PARTICLE_SPAWN_INTERVAL = 3;
        if (particleTickCounter >= PARTICLE_SPAWN_INTERVAL) {
            spawnGlowParticle();
            particleTickCounter = 0;
        }
    }


    private void spawnGlowParticle() {
        for (int i = 0; i < 1; i++) {
            this.level.addParticle(ModParticles.FAEDEER_PARTICLE.get(),
                    this.getX() + (Math.random() - 0.5) * 2,
                    this.getY() + 1.0,
                    this.getZ() + (Math.random() - 0.5) * 2,
                    0, 0.01, 0);

        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {

    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        // Add necessary entity data here
    }

    @Override
    public void remove() {
        super.remove();
    }
}

