package net.geminiimmortal.mobius.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class SorcererObliteratorEntity extends Entity {
    private float rotationAngle = 0;
    private boolean beamFiring = false;
    private int shrinkTicks = 0;

    public SorcererObliteratorEntity(EntityType<? extends SorcererObliteratorEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData() {

    }


    public void startBeam() {
        this.beamFiring = true;
        this.shrinkTicks = 5;
    }

    public boolean isBeamFiring(){
        return beamFiring;
    }

    public int getShrinkTicks() {
        return shrinkTicks;
    }



    @Override
    public void tick() {
        super.tick();
        rotationAngle += 1.0f; // Increment rotation angle (adjust speed as needed)
        if (rotationAngle >= 360.0f) {
            rotationAngle -= 360.0f; // Keep the angle within 0-360 degrees
        }
        if (beamFiring) shrinkTicks++;
    }

    public float getRotationAngle() {
        return rotationAngle;
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {}

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {}
}

