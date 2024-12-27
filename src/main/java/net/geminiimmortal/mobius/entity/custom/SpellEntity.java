package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.damage.KnivesOutDamageSource;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.geminiimmortal.mobius.util.CustomDamageEventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.List;

public class SpellEntity extends Entity {
    private float rotationAngle = 0;
    private int duration = 100;

    public SpellEntity(EntityType<? extends SpellEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData() {

    }

    private void spawnKnifeParticle() {
        for (int i = 0; i < 1; i++) {
            this.level.addParticle(ModParticles.KNIFE_RAIN_PARTICLE.get(),
                    this.getX() + (Math.random() - 0.5) * 2,
                    this.getY(),
                    this.getZ() + (Math.random() - 0.5) * 2,
                    0, 0.01, 0);
        }
    }



    @Override
    public void tick() {
        super.tick();
        AxisAlignedBB knifeBoundingBox = new AxisAlignedBB(this.getX() - 3, this.getY() - 20, this.getZ() - 3, this.getX() + 3, this.getY() + 2, this.getZ() + 3);
        List<PlayerEntity> players = level.getEntitiesOfClass(PlayerEntity.class, knifeBoundingBox);
        for (PlayerEntity player : players) {
            if (knifeBoundingBox.intersects(player.getBoundingBox())) {
                player.hurt(KnivesOutDamageSource.KNIVES_OUT, 1f); // Apply damage
            }
        }
        spawnKnifeParticle();

        rotationAngle += 1.0f; // Increment rotation angle (adjust speed as needed)
        if (rotationAngle >= 360.0f) {
            rotationAngle -= 360.0f; // Keep the angle within 0-360 degrees
        }

        if(duration > 0) {
           duration--;
        } else {
           kill();
        }
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

