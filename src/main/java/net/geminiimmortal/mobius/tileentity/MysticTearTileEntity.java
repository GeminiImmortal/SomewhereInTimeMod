package net.geminiimmortal.mobius.tileentity;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.FaestagEntity;
import net.geminiimmortal.mobius.entity.goals.util.TeleportUtil;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.UUID;

public class MysticTearTileEntity extends TileEntity implements ITickableTileEntity {
    private UUID faestagUUID;
    private FaestagEntity cachedFaestag;

    public MysticTearTileEntity() {
        super(ModTileEntities.MYSTIC_TEAR.get());
    }

    public void setFaestag(FaestagEntity faestag) {
        this.faestagUUID = faestag.getUUID();
        this.cachedFaestag = faestag;
        setChanged();
    }

    @Override
    public void setRemoved() {
        if (this.cachedFaestag != null && this.cachedFaestag.isAlive()) {
            this.cachedFaestag.remove();
        }
        super.setRemoved();
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        if (nbt.hasUUID("FaestagUUID")) {
            faestagUUID = nbt.getUUID("FaestagUUID");
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        if (faestagUUID != null)
            nbt.putUUID("FaestagUUID", faestagUUID);
        return nbt;
    }

    @Override
    public void tick() {
        if (level.isClientSide) return;

        if (faestagUUID != null && cachedFaestag == null) {
            Entity e = ((ServerWorld)level).getEntity(faestagUUID);
            if (e instanceof FaestagEntity) cachedFaestag = (FaestagEntity)e;
        }

        if (level instanceof ServerWorld && !level.isClientSide && level.getGameTime() % 20 == 0) {
            BlockPos origin = this.worldPosition;
            double radius = 40.0;
            FaestagEntity stag = new FaestagEntity(ModEntityTypes.FAESTAG.get(), level);

            List<FaestagEntity> entities = ((ServerWorld) level).getEntitiesOfClass(
                    FaestagEntity.class,
                    new AxisAlignedBB(
                            origin.offset(-radius, -radius, -radius),
                            origin.offset(radius, radius, radius)
                    )
            );

            if (entities.isEmpty() && cachedFaestag == null) {
                Vector3d spawnPos = Vector3d.atCenterOf(origin.offset(4, 0, 4));
                stag.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                Vector3d spawnPosSafe = TeleportUtil.findSafeTeleportPosition(stag.level, stag, 30, 40);
                if (spawnPosSafe != null) {
                    stag.setPos(spawnPosSafe.x, spawnPosSafe.y, spawnPosSafe.z);
                }
                level.addFreshEntity(stag);
                stag.setAlerted(false);
                stag.setSummoned(false);
                cachedFaestag = stag;
            }

            if (cachedFaestag != null && cachedFaestag.getAlerted() && cachedFaestag.level.getGameTime() % 20 == 0) {
                cachedFaestag.setTargetPortalPosition(origin);
            }

            if (cachedFaestag != null && cachedFaestag.distanceToSqr(origin.getX(), origin.getY(), origin.getZ()) < 1.5 && this.getLevel() != null) {
                cachedFaestag.remove();
                EntityType<?> lightningBoltType = EntityType.LIGHTNING_BOLT;
                LightningBoltEntity lightningBolt = (LightningBoltEntity) lightningBoltType.create(this.getLevel());
                if (lightningBolt != null) {
                    lightningBolt.moveTo(origin, 1.0f, 1.0f);
                    this.getLevel().addFreshEntity(lightningBolt);
                }
                this.getLevel().setBlockAndUpdate(origin, Blocks.AIR.defaultBlockState());
                this.setRemoved();
            }

        }

        if (this.level != null && !this.level.isClientSide() && level.getGameTime() % 100 == 0) {
            BlockPos origin = this.worldPosition;
            this.level.playSound(null, origin, ModSounds.MYSTIC_TEAR_AMBIENT.get(), SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }

    public FaestagEntity getFaestag() {
        return cachedFaestag;
    }
}

