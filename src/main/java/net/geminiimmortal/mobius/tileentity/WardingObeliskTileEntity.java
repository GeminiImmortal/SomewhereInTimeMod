package net.geminiimmortal.mobius.tileentity;

import net.geminiimmortal.mobius.block.custom.WardingObeliskBlock;
import net.geminiimmortal.mobius.block.custom.WardingObeliskType;
import net.geminiimmortal.mobius.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class WardingObeliskTileEntity extends TileEntity implements IAnimatable, ITickableTileEntity {
    public String type;
    private boolean wardIsCurrentlyActive = true;
    private boolean active = true;
    public WardingObeliskTileEntity() {
        super(ModTileEntities.WARDING_OBELISK.get());
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.warding_obelisk.active", true));
        return PlayState.CONTINUE;
    }

    public boolean isActive() {
        return active;
    }


/*    public void handleObeliskDestroyed() {
        if (this.level != null && !this.level.isClientSide() && Objects.equals(this.getBlockState().getValue(WardingObeliskBlock.TYPE), WardingObeliskType.WARDING_TOWER)) {
            this.active = false;
            notifyMainObeliskOfDeactivation();
            System.out.println("Warding tower at " + this.getBlockPos() + " destroyed. Notifying main obelisk.");
        }
    }


    private void notifyMainObeliskOfDeactivation() {
        int radius = 128;

        BlockPos pos = this.getBlockPos();
        BlockPos.betweenClosedStream(
                pos.offset(-radius, -10, -radius),
                pos.offset(radius, 10, radius)
        ).forEach(p -> {
            TileEntity te = level.getBlockEntity(p);
            if (te instanceof WardingObeliskTileEntity) {
                WardingObeliskTileEntity other = (WardingObeliskTileEntity) te;
                if (Objects.equals(other.getBlockState().getValue(WardingObeliskBlock.TYPE), WardingObeliskType.GOVERNOR_TOWER)) {
                    other.onSatelliteDeactivated(this.getBlockPos());
                }
            }
        });

        System.out.println("Main obelisk should be notified.");
    }

    private void onSatelliteDeactivated(BlockPos satellitePos) {
        if (!wardIsCurrentlyActive) return; // Already deactivated

        active = false;
        wardIsCurrentlyActive = false;

        // Optional: visual or sound effects here
        System.out.println("Governor obelisk at " + this.getBlockPos() + " deactivated due to satellite at " + satellitePos);
    }*/



    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putBoolean("Active", isActive());
        compound.putBoolean("WardActive", wardIsCurrentlyActive);
        compound.putString("type", this.getBlockState().getValue(WardingObeliskBlock.TYPE).getSerializedName());
        return compound;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        active = compound.getBoolean("Active");
        wardIsCurrentlyActive = compound.getBoolean("WardActive");
        type = compound.getString("type");
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void tick() {
        if (wardIsCurrentlyActive) {
            int WARD_RADIUS = 64;
            if (this.level != null && !this.level.isClientSide()) {
                ServerWorld serverWorld = (ServerWorld) level;
                BlockPos origin = this.worldPosition;

                if (this.getBlockState().getValue(WardingObeliskBlock.TYPE).getSerializedName().equals(WardingObeliskType.GOVERNOR_TOWER.getSerializedName()) && isActive()) {
                    List<ServerPlayerEntity> players = serverWorld.getEntitiesOfClass(
                            ServerPlayerEntity.class,
                            new AxisAlignedBB(
                                    origin.offset(-WARD_RADIUS, -WARD_RADIUS, -WARD_RADIUS),
                                    origin.offset(WARD_RADIUS, WARD_RADIUS, WARD_RADIUS)
                            )
                    );
                    for (ServerPlayerEntity nearbyPlayers : players) {
                        if (!nearbyPlayers.isCreative() && !nearbyPlayers.isSpectator() && !((nearbyPlayers.getMainHandItem().getItem().equals(ModItems.GOVERNOR_CREST.get()) || (nearbyPlayers.getOffhandItem().getItem().equals(ModItems.GOVERNOR_CREST.get()))))) {
                            double dx = nearbyPlayers.getX() - this.getBlockPos().getX();
                            double dz = nearbyPlayers.getZ() - this.getBlockPos().getZ();
                            double dist = Math.sqrt(dx * dx + dz * dz);
                            if (dist < WARD_RADIUS) {
                                double pushStrength = 1.5 * (1 - dist / WARD_RADIUS); // scales push
                                nearbyPlayers.setDeltaMovement(dx / dist * pushStrength, 0, dz / dist * pushStrength);
                                nearbyPlayers.getDeltaMovement().normalize();
                                try {
                                    Field velocityField = Entity.class.getDeclaredField("hasImpulse");
                                    velocityField.setAccessible(true);
                                    velocityField.setBoolean(nearbyPlayers, true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (nearbyPlayers instanceof ServerPlayerEntity) {
                                    ((ServerPlayerEntity) nearbyPlayers).connection.send(new SEntityVelocityPacket(nearbyPlayers));
                                }
                            }

                            serverWorld.sendParticles((ServerPlayerEntity) nearbyPlayers, ParticleTypes.SOUL_FIRE_FLAME, true, nearbyPlayers.getX(), nearbyPlayers.getY(), nearbyPlayers.getZ(), 24, 0.1D, 0.1D, 0.1D, 0.1D);
                            nearbyPlayers.displayClientMessage(new StringTextComponent("A strange magical force repels you...").setStyle(Style.EMPTY), true);
                        }
                    }
                }
            }
        }
    }
}

