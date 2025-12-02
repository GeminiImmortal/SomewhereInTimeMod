package net.geminiimmortal.mobius.mixin;

import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.geminiimmortal.mobius.capability.boost.BoostDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Disables vanilla anti-flight-in-survival-mode checks on server-side by resetting aboveGroundTickCount to 0, but
 * ONLY when utilizing Jump Pad blocks or the Dieselytra's jump boost ability.
 * Safeguards are in place to ensure that this cannot be used for malicious purposes, such as cheating.
 **/

@Mixin(ServerPlayNetHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow private int aboveGroundTickCount;
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "tick", at = @At("HEAD"))
    private void mobius$resetFlightKickTimerWhenBoosting(CallbackInfo ci) {
        if (this.player != null && this.player.getCapability(ModCapabilities.BOOST_CAPABILITY).isPresent()) {
            player.getCapability(BoostDataProvider.BOOST_CAP).ifPresent(boostData -> {
                if (boostData.shouldIgnoreNextFall()) {
                    if (!player.abilities.flying && !player.isSpectator())
                        this.aboveGroundTickCount = 0;
                }
            });
        }
    }
}