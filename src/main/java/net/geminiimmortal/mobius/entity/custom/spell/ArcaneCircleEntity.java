package net.geminiimmortal.mobius.entity.custom.spell;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.spell.util.StunUtil;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ArcaneCircleEntity extends Entity implements SpellTypeEntity {
    private PlayerEntity caster = null;
    private int lifeTicks = 200; // lasts for 10 seconds (20 ticks/sec)
    private float endurance = 20.0f; // Starting endurance value, adjust as needed


    public ArcaneCircleEntity(EntityType<? extends ArcaneCircleEntity> type, World world) {
        super(type, world);
    }

    public ArcaneCircleEntity(World world, double x, double y, double z, PlayerEntity caster) {
        super(ModEntityTypes.ARCANE_CIRCLE.get(), world);
        this.setPos(x, y, z);
        this.caster = caster;
    }

    public void damageShield(float amount) {
        this.endurance -= amount;
        if (this.endurance <= 0) {
            if (caster != null) {
                StunUtil.stun(caster, 40);

                if (caster.level.isClientSide) {
                    caster.level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), ModSounds.DASH_IMPACT.get(), SoundCategory.PLAYERS, 10.0F, 1.0F);
                }
                this.remove();
            }
        }
    }


    @Override
    public void tick() {
        super.tick();
        if (caster == null || !caster.isAlive() || lifeTicks-- <= 0) {
            this.remove();
            return;
        }

        // Update position to always stay in front of player
        Vector3d look = caster.getLookAngle();
        double distance = 2.0;
        this.setPos(
                caster.getX() + look.x * distance,
                caster.getY() + caster.getEyeHeight() + look.y * distance,
                caster.getZ() + look.z * distance
        );

        if (level.isClientSide) {
            spawnParticles();
        }
    }

    private void spawnParticles() {
        double pulse = 0.1 * Math.sin(lifeTicks * 0.2); // slowly pulsate radius
        double baseRadius = 1.5 + pulse;
        int rings = 3;
        int particlesPerRing = 40;
        float rotationSpeed = lifeTicks * 2.5f;


        Vector3d lookVec = this.getEyePosition(1.0f);
        Vector3d upVec = new Vector3d(0, 1, 0);
        Vector3d rightVec = lookVec.cross(upVec).normalize();
        upVec = rightVec.cross(lookVec).normalize(); // Recalculate true "up" vector

        for (int ring = 0; ring < rings; ring++) {
            double radius = baseRadius - (ring * 0.2);
            double heightOffset = 0.0;

            for (int i = 0; i < particlesPerRing; i++) {
                double angle = ((double) i / particlesPerRing) * 2.0 * Math.PI + Math.toRadians(rotationSpeed * (ring + 1));
                double x = Math.cos(angle) * radius;
                double y = Math.sin(angle) * radius;

                // Now project x, y into 3D space based on player look
                Vector3d offset = rightVec.scale(x).add(upVec.scale(y));

                Vector3d pos = this.getPosition(1).add(offset);

                level.addParticle(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, 0, 0, 0);
            }
        }
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

    }

    @Override
    public SpellType getSpellType() {
        return SpellType.DEFENSIVE;
    }
}

