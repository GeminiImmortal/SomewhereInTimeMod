package net.geminiimmortal.mobius.client;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.item.ModItems;
import net.geminiimmortal.mobius.network.ActivateRingPacket;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, value = Dist.CLIENT)
public class ClientInputHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().player != null) {
            while (ModKeybinds.activateRingTwoKey.consumeClick()) {
                ModNetwork.NETWORK_CHANNEL.sendToServer(new ActivateRingPacket(ModItems.RING_OF_THE_BERSERKER.get().getRegistryName()));
            }

            while (ModKeybinds.activateRingOneKey.consumeClick()) {
                ModNetwork.NETWORK_CHANNEL.sendToServer(new ActivateRingPacket(ModItems.RING_OF_REPULSION.get().getRegistryName()));
            }
        }
    }
}
