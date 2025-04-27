package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.custom.spell.SpellType;
import net.geminiimmortal.mobius.entity.goals.util.TrackingLaserBeam;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;
import net.geminiimmortal.mobius.entity.custom.spell.SpellTypeEntity;

public class BarrierEntity extends Entity implements SpellTypeEntity {
    private final int MAX_LIFETIME_TICKS = 300;
    private UUID casterUUID;
    private final int WIDTH = 4;
    private final int HEIGHT = 12;

    public BarrierEntity(EntityType<? extends Entity> type, World level) {
        super(type, level);
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData() {

    }


    @Override
    public boolean canCollideWith(Entity entity) {
        return !(entity instanceof PlayerEntity);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }




    @Override
    public boolean isPushable() {
        return false;
    }


    @Override
    public void tick() {
        super.tick();

        for (Entity nearby : this.level.getEntities(this, this.getBoundingBox().inflate(0.5))) {
            if (nearby instanceof SpellEntity) {
                this.onCollideWith((SpellEntity) nearby);
            }
        }


        this.setRemainingFireTicks(0);
        if (this.tickCount > MAX_LIFETIME_TICKS) {
            this.remove();
        }


        if (this.level.isClientSide) {
            int particleCount = 100;
            double radius = 2.5;
            double height = 3.0;

            double time = this.tickCount + Minecraft.getInstance().getFrameTime(); // smooth movement

            for (int i = 0; i < particleCount; i++) {
                double angle = (i / (double) particleCount) * 2 * Math.PI + (time * 0.1);
                double xOffset = Math.cos(angle) * radius;
                double zOffset = Math.sin(angle) * radius;
                double yOffset = (i / (double) particleCount) * height;

                this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                        this.getX() + xOffset,
                        this.getY() + yOffset,
                        this.getZ() + zOffset,
                        0.0, 0.0, 0.0);
            }
        }
        }

    @Override
    protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {

    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return super.getBoundingBox().inflate((double) WIDTH / 2, (double) HEIGHT / 2, (double) WIDTH / 2);
    }


    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onCollideWith(net.geminiimmortal.mobius.entity.custom.SpellEntity other) {

    }

    @Override
    public SpellType getSpellType() {
        return SpellType.DEFENSIVE;
    }
}


