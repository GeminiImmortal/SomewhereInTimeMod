package net.geminiimmortal.mobius.mixin;


import net.geminiimmortal.mobius.damage.EctoplasmDamageSource;
import net.geminiimmortal.mobius.fluid.ModFluids;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "travel", at = @At("HEAD"))
    private void modifyMovementInFluid(Vector3d travelVector, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.level.isClientSide()) return;

        FluidState fluidState = entity.level.getFluidState(entity.blockPosition());

        if (mobius$isInEctoplasm(fluidState)) {
            // Reduce movement speed
            entity.setSpeed(entity.getSpeed() * 0.5f);
            entity.hurt(EctoplasmDamageSource.ECTOPLASM, 0.25f);
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SOUL_ESCAPE, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }

    @Unique
    private boolean mobius$isInEctoplasm(FluidState fluidState) {
        return fluidState.getType() == ModFluids.ECTOPLASM_FLUID.get() || fluidState.getType() == ModFluids.FLOWING_ECTOPLASM.get();
    }
}

