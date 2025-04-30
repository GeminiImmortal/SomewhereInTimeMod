package net.geminiimmortal.mobius.entity.custom.spell;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SpellProjectileEntity extends ThrowableEntity implements SpellTypeEntity {

    public SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> type, World world) {
        super(type, world);
    }

    public SpellProjectileEntity(World world, double x, double y, double z) {
        super(ModEntityTypes.SPELL_PROJECTILE.get(), world); // Replace with your registry
        this.setPos(x, y, z);
    }

    public SpellProjectileEntity(World world, LivingEntity shooter) {
        super(ModEntityTypes.SPELL_PROJECTILE.get(), shooter, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide) {
            spawnRingParticles();
        }

        // Optional: Remove after some time
        if (this.tickCount > 100) {
            this.remove();
        }
    }

    private void spawnRingParticles() {
        double radius = 0.5D;
        int points = 20;
        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            double xOffset = Math.cos(angle) * radius;
            double zOffset = Math.sin(angle) * radius;
            double yOffset = Math.sin(angle) * radius;

            Vector3d particlePos = this.position().add(xOffset, yOffset, zOffset);
            this.level.addParticle(ParticleTypes.DRAGON_BREATH,
                    particlePos.x, particlePos.y, particlePos.z,
                    0, 0, 0);
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof SpellTypeEntity) onCollideWith((SpellTypeEntity) result.getEntity());
        result.getEntity().hurt(DamageSource.MAGIC, 2f);
        this.remove();
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        super.onHitBlock(result);
        this.remove();
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {}

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {}

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onCollideWith(SpellTypeEntity other) {
        if (other instanceof ArcaneCircleEntity) {
            ((ArcaneCircleEntity) other).damageShield(2f);
            this.remove();
            return;
        }
        if (other.getSpellType() == SpellType.DEFENSIVE) {
            this.remove();
        }
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.OFFENSIVE;
    }
}

