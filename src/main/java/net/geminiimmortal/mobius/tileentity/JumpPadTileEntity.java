package net.geminiimmortal.mobius.tileentity;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.block.custom.JumpPadType;
import net.geminiimmortal.mobius.block.custom.JumpPadBlock;
import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.lang.reflect.Field;
import java.util.List;

public class JumpPadTileEntity extends TileEntity implements IAnimatable, ITickableTileEntity {
    public String type;
    private boolean active = false;
    private boolean linked = false;

    public JumpPadTileEntity() {
        super(ModTileEntities.JUMP_PAD.get());
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    public boolean isActive() {
        if (ModBlocks.JUMP_PAD != null) {
            active = this.getBlockState().getValue(JumpPadBlock.ACTIVE);
        }
        return active;
    }

    public boolean isLinked() {
        if (ModBlocks.JUMP_PAD != null) {
            linked = this.getBlockState().getValue(JumpPadBlock.LINKED);
        }
        return linked;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putBoolean("Active", isActive());
        compound.putBoolean("Linked", isLinked());
        compound.putString("type", this.getBlockState().getValue(JumpPadBlock.TYPE).getSerializedName());
        return compound;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        active = compound.getBoolean("Active");
        type = compound.getString("type");
        linked = compound.getBoolean("Linked");
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void tick() {
        boolean getLinked = isLinked();
        boolean getActive = isActive();

        if (getLinked) {
            if (getActive) {
                if (this.level != null && !this.level.isClientSide()) {
                    ServerWorld serverWorld = (ServerWorld) level;
                    BlockPos origin = this.worldPosition;

                    List<ServerPlayerEntity> players = serverWorld.getEntitiesOfClass(
                            ServerPlayerEntity.class,
                            new AxisAlignedBB(this.worldPosition).inflate(0.25D)
                    );

                    if (this.getBlockState().getValue(JumpPadBlock.TYPE).getSerializedName().equals(JumpPadType.THUNDERBIRD.getSerializedName()) && isActive()) {

                        for (ServerPlayerEntity nearbyPlayers : players) {
                            nearbyPlayers.setDeltaMovement(0, 2, 0);
                            nearbyPlayers.getDeltaMovement().normalize();
                            try {
                                Field velocityField = Entity.class.getDeclaredField("hasImpulse");
                                velocityField.setAccessible(true);
                                velocityField.setBoolean(nearbyPlayers, true);
                            } catch (Exception ignored) {

                            }

                            if (nearbyPlayers instanceof ServerPlayerEntity) {
                                nearbyPlayers.connection.send(new SEntityVelocityPacket(nearbyPlayers));
                                nearbyPlayers.getCapability(ModCapabilities.BOOST_CAPABILITY).ifPresent(iBoostData -> iBoostData.setIgnoreNextFall(true));
                                nearbyPlayers.addEffect(new EffectInstance(Effects.SLOW_FALLING, 100));
                                serverWorld.playSound(null, origin.getX(), origin.getY(), origin.getZ(), ModSounds.DIESELYTRA_BOOST.get(), SoundCategory.BLOCKS, 2.0f, 1.0f);
                            }
                        }
                    }

                    if (this.getBlockState().getValue(JumpPadBlock.TYPE).getSerializedName().equals(JumpPadType.NONE.getSerializedName()) && isActive()) {

                        for (ServerPlayerEntity nearbyPlayers : players) {
                            nearbyPlayers.setDeltaMovement(0, 4.0, 0);
                            nearbyPlayers.getDeltaMovement().normalize();
                            try {
                                Field velocityField = Entity.class.getDeclaredField("hasImpulse");
                                velocityField.setAccessible(true);
                                velocityField.setBoolean(nearbyPlayers, true);
                            } catch (Exception ignored) {

                            }

                            if (nearbyPlayers instanceof ServerPlayerEntity) {
                                nearbyPlayers.connection.send(new SEntityVelocityPacket(nearbyPlayers));
                                nearbyPlayers.getCapability(ModCapabilities.BOOST_CAPABILITY).ifPresent(iBoostData -> iBoostData.setIgnoreNextFall(true));
                                nearbyPlayers.addEffect(new EffectInstance(Effects.SLOW_FALLING, 200, 1));
                                serverWorld.playSound(null, origin.getX(), origin.getY(), origin.getZ(), ModSounds.DIESELYTRA_BOOST.get(), SoundCategory.BLOCKS, 2.0f, 1.0f);
                            }
                        }
                    }
                }
            }
        }
    }
}

