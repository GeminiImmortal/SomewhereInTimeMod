package net.geminiimmortal.mobius.tileentity;

import net.geminiimmortal.mobius.entity.custom.AbstractImperialEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class CapturePointTileEntity extends TileEntity implements IAnimatable, ITickableTileEntity {

    private final AnimationFactory factory = new AnimationFactory(this);

    public CapturePointTileEntity() {
        super(ModTileEntities.CAPTURE_POINT.get());
    }

    @Override
    public void tick() {
        if (level instanceof ServerWorld && !level.isClientSide && level.getGameTime() % 20 == 0) {
            ServerWorld serverWorld = (ServerWorld) level;
            BlockPos origin = this.worldPosition;
            double radius = 40.0;
            double leashRadius = 20.0;
            List<AbstractImperialEntity> entities = serverWorld.getEntitiesOfClass(
                    AbstractImperialEntity.class,
                    new AxisAlignedBB(
                            origin.offset(-radius, -radius, -radius),
                            origin.offset(radius, radius, radius)
                    )
            );

            for (AbstractImperialEntity imperial : entities) {
                double distanceSq = imperial.blockPosition().distSqr(origin);
                if (imperial.getTarget() == null || !imperial.getTarget().isAlive()) {
                    if (distanceSq > radius * radius) {
                        imperial.getNavigation().moveTo(
                                origin.getX() + 0.5,
                                origin.getY(),
                                origin.getZ() + 0.5,
                                1.0
                        );
                    }
                }
                if (distanceSq > leashRadius * leashRadius) {
                    imperial.setTarget(null);
                    imperial.setRemainingPersistentAngerTime(0);
                    imperial.setPersistentAngerTarget(null);
                    imperial.addEffect(new EffectInstance(Effects.REGENERATION, 200, 5));
                    imperial.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 200, 5));
                    imperial.getNavigation().moveTo(
                            origin.getX() + 0.5,
                            origin.getY(),
                            origin.getZ() + 0.5,
                            1.0
                    );
                } else {
                    ServerPlayerEntity player = (ServerPlayerEntity) level.getNearestPlayer(imperial, 12D);
                    imperial.setTarget(player);
                }
            }
        }
    }


    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}

