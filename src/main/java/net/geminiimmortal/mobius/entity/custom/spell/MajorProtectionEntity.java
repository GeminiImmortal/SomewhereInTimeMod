package net.geminiimmortal.mobius.entity.custom.spell;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;

public class MajorProtectionEntity extends BarrierEntity implements SpellTypeEntity {
    private final int MAX_LIFETIME_TICKS = 300;
    private UUID casterUUID;
    private final int WIDTH = 4;
    private final int HEIGHT = 12;
    private ServerPlayerEntity caster;

    public MajorProtectionEntity(EntityType<? extends BarrierEntity> type, World level, ServerPlayerEntity caster) {
        super(type, level);
        this.setNoGravity(true);
        this.caster = caster;
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
    public void tick() {
        super.tick();

        for (Entity nearby : this.level.getEntities(this, this.getBoundingBox().inflate(0.5))) {
            if (nearby instanceof SpellTypeEntity) {
                this.onCollideWith((SpellTypeEntity) nearby);
            }
        }
        if (caster != null && !caster.isDeadOrDying()) {
            this.setPos(caster.getX(), caster.getY(), caster.getZ());
        } else {
            this.remove();
        }
        this.setRemainingFireTicks(0);
        if (this.tickCount > MAX_LIFETIME_TICKS) {
            this.remove();
        }


        if (this.level.isClientSide) {
            int particleCount = 20;
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
    public AxisAlignedBB getBoundingBox() {
        return super.getBoundingBox().inflate((double) WIDTH / 2, (double) HEIGHT / 2, (double) WIDTH / 2);
    }


    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onCollideWith(SpellTypeEntity other) {

    }

    @Override
    public SpellType getSpellType() {
        return SpellType.DEFENSIVE;
    }
}



