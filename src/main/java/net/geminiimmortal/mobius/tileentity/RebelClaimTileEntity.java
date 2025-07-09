package net.geminiimmortal.mobius.tileentity;

import net.geminiimmortal.mobius.entity.custom.RebelInstigatorEntity;
import net.geminiimmortal.mobius.entity.custom.RebelQuartermasterEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class RebelClaimTileEntity extends TileEntity implements IAnimatable, ITickableTileEntity {

    private final AnimationFactory factory = new AnimationFactory(this);

    public RebelClaimTileEntity() {
        super(ModTileEntities.REBEL_CLAIM.get());
    }

    @Override
    public void tick() {
        if (level instanceof ServerWorld && !level.isClientSide && level.getGameTime() % 20 == 0) {
            ServerWorld serverWorld = (ServerWorld) level;
            BlockPos origin = this.worldPosition;
            double radius = 10.0;
            double safetyRadius = 32.0;
            List<RebelQuartermasterEntity> entities = serverWorld.getEntitiesOfClass(
                    RebelQuartermasterEntity.class,
                    new AxisAlignedBB(
                            origin.offset(-radius, -radius, -radius),
                            origin.offset(radius, radius, radius)
                    )
            );
            List<RebelInstigatorEntity> rebelInstigatorEntities = serverWorld.getEntitiesOfClass(
                    RebelInstigatorEntity.class,
                    new AxisAlignedBB(
                            origin.offset(-radius, -radius, -radius),
                            origin.offset(radius, radius, radius)
                    )
            );

            for (RebelQuartermasterEntity rebelQuartermaster : entities) {
                double distanceSq = rebelQuartermaster.blockPosition().distSqr(origin);

                if (distanceSq > radius * radius && distanceSq <= safetyRadius * safetyRadius) {
                    rebelQuartermaster.getNavigation().moveTo(
                            origin.getX() + 0.5,
                            origin.getY(),
                            origin.getZ() + 0.5,
                            1.0
                    );
                }
                if (distanceSq > safetyRadius * safetyRadius) {
                    rebelQuartermaster.getNavigation().moveTo(
                            origin.getX() + 0.5,
                            origin.getY(),
                            origin.getZ() + 0.5,
                            1.0
                    );
                }
            }
            for (RebelInstigatorEntity rebelSoldier : rebelInstigatorEntities) {
                double distanceSq = rebelSoldier.blockPosition().distSqr(origin);

                if (distanceSq > radius * radius && distanceSq <= safetyRadius * safetyRadius) {
                    rebelSoldier.setTarget(null);
                    rebelSoldier.getNavigation().moveTo(
                            origin.getX() + 0.5,
                            origin.getY(),
                            origin.getZ() + 0.5,
                            1.0
                    );
                }
                if (distanceSq > safetyRadius * safetyRadius) {
                    rebelSoldier.getNavigation().moveTo(
                            origin.getX() + 0.5,
                            origin.getY(),
                            origin.getZ() + 0.5,
                            1.0
                    );
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

