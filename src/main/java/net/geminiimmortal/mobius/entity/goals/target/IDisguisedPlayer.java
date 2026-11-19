package net.geminiimmortal.mobius.entity.goals.target;

import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public interface IDisguisedPlayer {
    static boolean isCriminal(@Nullable PlayerEntity player) {
        return false;
    }
}
