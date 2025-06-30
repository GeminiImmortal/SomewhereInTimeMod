package net.geminiimmortal.mobius.util;

import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.geminiimmortal.mobius.capability.infamy.IInfamy;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.geminiimmortal.mobius.network.SInfamySyncPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class InfamyHelper {
    public static IInfamy get(PlayerEntity player) {
        return player.getCapability(ModCapabilities.INFAMY_CAPABILITY)
                .orElseThrow(() -> new IllegalStateException("Missing Infamy capability!"));
    }
    public static void sync(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            int infamy = InfamyHelper.get(player).getInfamy();
            ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SInfamySyncPacket(infamy));
        }
    }
}

