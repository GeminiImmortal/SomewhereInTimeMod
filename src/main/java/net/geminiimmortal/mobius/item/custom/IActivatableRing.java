package net.geminiimmortal.mobius.item.custom;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

public interface IActivatableRing {
    void onKeyActivate(ServerPlayerEntity player, ItemStack stack);
}
