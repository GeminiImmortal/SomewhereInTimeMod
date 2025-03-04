package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;

public class TornadoEntity extends Entity implements IEntityAdditionalSpawnData {
    private UUID casterUUID;
    private int lifetime = 200; // 10 seconds at 20 ticks per second
    private double rotationAngle = 0;
    private final double radius = 5;
    private int tickCounter = 0;

    public TornadoEntity(EntityType<? extends TornadoEntity> type, World world) {
        super(type, world);
    }

    public TornadoEntity(World world, double x, double y, double z, PlayerEntity caster) {
        super(ModEntityTypes.TORNADO.get(), world);
        this.setPos(x, y, z);
        this.casterUUID = caster.getUUID();
    }

    @Override
    public void tick() {
        super.tick();
        tickCounter++;
        lifetime--;
        if (lifetime <= 0) {
            this.remove();
            return;
        }

        PlayerEntity caster = getCaster();
        if (caster == null) {
            this.remove();
            return;
        }

        rotationAngle += 10; // Faster spin
        double offsetX = radius * Math.cos(Math.toRadians(rotationAngle));
        double offsetZ = radius * Math.sin(Math.toRadians(rotationAngle));
        this.setPos(caster.getX() + offsetX, caster.getY(), caster.getZ() + offsetZ);

        if (level.isClientSide) {
            generateParticles();
        }

        if (!level.isClientSide) {
            affectEntities();
        }
    }

    private void generateParticles() {
        for (double yOffset = 0; yOffset < 5.0; yOffset += 0.4) {
            double angle = rotationAngle + (yOffset * 60);
            double x = getX() + radius * Math.cos(Math.toRadians(angle));
            double z = getZ() + radius * Math.sin(Math.toRadians(angle));
            level.addParticle(ParticleTypes.SWEEP_ATTACK, x, getY() + yOffset, z, 0, 0.1, 0);
        }

        for (int i = 0; i < 20; i++) {
            double randomX = getX() + (random.nextDouble() * 2 - 1) * radius;
            double randomZ = getZ() + (random.nextDouble() * 2 - 1) * radius;
            level.addParticle(ParticleTypes.CLOUD, randomX, getY() + random.nextDouble() * 3, randomZ, 0, 0.1, 0);
        }
    }

    private void affectEntities() {
        AxisAlignedBB boundingBox = getBoundingBox().inflate(1.5);
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, boundingBox)) {
            if (!entity.getUUID().equals(casterUUID)) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, 0.3, 0));
                if (tickCounter % 20 == 0) { // Apply damage every second
                    entity.hurt(DamageSource.MAGIC, 4.0F);
                }
            }
        }
    }

    private PlayerEntity getCaster() {
        return casterUUID != null ? level.getPlayerByUUID(casterUUID) : null;
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        if (compound.hasUUID("CasterUUID")) {
            casterUUID = compound.getUUID("CasterUUID");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        if (casterUUID != null) {
            compound.putUUID("CasterUUID", casterUUID);
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeUUID(casterUUID);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        casterUUID = buffer.readUUID();
    }
}