package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;

public class MarkerEntity extends Entity {

    public MarkerEntity(EntityType<? extends MarkerEntity> type, World world) {
        super(type, world);
        this.noPhysics = true;
    }

    private int lifetime = 1200;
    protected boolean governorBossSealMarker = false;
    protected boolean governorBossSpawnMarker = false;
    private boolean captainBossSpawnMarker = false;
    private boolean persistentMarker = false;
    private CompoundNBT nbt = new CompoundNBT();

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.putBoolean("PersistentMarker", persistentMarker);
        nbt.putBoolean("CaptainBossSpawnMarker", captainBossSpawnMarker);
        nbt.putBoolean("GovernorBossSpawnMarker", governorBossSpawnMarker);
        nbt.putBoolean("GovernorBossSealMarker", governorBossSealMarker);
    }

    @Override
    public void tick() {
        if (!level.isClientSide && --lifetime <= 0) remove();
        if (nbt.getBoolean("PersistentMarker")) {
            this.lifetime = 9999999;
            this.persistentMarker = true;
        }
        if (nbt.getBoolean("CaptainBossSpawnMarker")) {
            this.captainBossSpawnMarker = true;
        }
        if (this.captainBossSpawnMarker) {
            AxisAlignedBB spawnDetectionBox = new AxisAlignedBB(this.blockPosition().getX(), this.blockPosition().getY(),
                    this.blockPosition().getZ(), this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ()).inflate(12.5D);
            List<ServerPlayerEntity> playersInBox = this.level.getEntitiesOfClass(ServerPlayerEntity.class, spawnDetectionBox);

            if (!playersInBox.isEmpty() && playersInBox.stream().noneMatch(ServerPlayerEntity::isCreative) && playersInBox.stream().noneMatch(ServerPlayerEntity::isSpectator)) {
                GrimcrowCaptainBossEntity captainBoss = new GrimcrowCaptainBossEntity(ModEntityTypes.GRIMCROW_CAPTAIN.get(), this.level);
                captainBoss.setPos(spawnDetectionBox.getCenter().x, spawnDetectionBox.getCenter().y, spawnDetectionBox.getCenter().z);
                this.level.addFreshEntity(captainBoss);
                this.remove();
            }
        }
    }

    protected void resetGovernorFight(GovernorEntity governor) {
        if (governor.playerHasDied) {
            IntroGovernorCloneEntity introGovernorClone = new IntroGovernorCloneEntity(ModEntityTypes.INTRO_GOVERNOR_CLONE.get(), this.level);
            introGovernorClone.moveTo(this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ());
            this.level.addFreshEntity(introGovernorClone);
        }
    }

    protected void governorSealBlocks(IntroGovernorCloneEntity clone) {
        if (!clone.removed || clone.isAlive()) {
            List<BlockPos> sealPositions = new ArrayList<>();
            BlockPos base = this.blockPosition().offset(-2, 0, -2);

            for (int dx = 0; dx < 5; dx++) {
                for (int dz = 0; dz < 5; dz++) {
                    sealPositions.add(base.offset(dx, 0, dz));
                }
            }

            sealPositions.forEach(block -> this.level.setBlockAndUpdate(block, ModBlocks.DUNGEON_BLOCK.get().defaultBlockState()));
        }
    }

    protected void governorUnSealBlocks(GovernorEntity governor) {
        if (!governor.removed || governor.isAlive()) {
            List<BlockPos> sealPositions = new ArrayList<>();
            BlockPos base = this.blockPosition().offset(-2, 0, -2);

            for (int dx = 0; dx < 4; dx++) {
                for (int dz = 0; dz < 4; dz++) {
                    sealPositions.add(base.offset(dx, 0, dz));
                }
            }

            sealPositions.forEach(block -> this.level.setBlockAndUpdate(block, Blocks.AIR.defaultBlockState()));
        }
    }

    @Override
    public boolean isInvisible() { return true; }

    @Override
    public boolean isPickable() { return false; }

    @Override
    public boolean isPushable() { return false; }

    @Override
    public boolean canBeCollidedWith() { return false; }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return new AxisAlignedBB(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) { return false; }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        if (nbt.getBoolean("PersistentMarker")) {
            this.lifetime = 999999;
            this.persistentMarker = true;
        }
        if (nbt.getBoolean("CaptainBossSpawnMarker")) {
            this.captainBossSpawnMarker = true;
        }
        if (nbt.getBoolean("GovernorBossSpawnMarker")) {
            this.governorBossSpawnMarker = true;
        }
        if (nbt.getBoolean("GovernorBossSealMarker")) {
            this.governorBossSealMarker = true;
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

