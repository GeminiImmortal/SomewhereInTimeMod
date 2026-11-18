package net.geminiimmortal.mobius.network;

import net.geminiimmortal.mobius.item.custom.IActivatableRing;
import net.geminiimmortal.mobius.item.custom.RingOfTheBerserker;
import net.geminiimmortal.mobius.item.custom.RingOfWarding;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Supplier;

public class ActivateRingPacket {

    private ResourceLocation ringRegistryKey;
    public ActivateRingPacket(ResourceLocation ringRegistryKey) {
        this.ringRegistryKey = ringRegistryKey;
    }

    public static void encode(ActivateRingPacket msg, PacketBuffer buf) {
        buf.writeResourceLocation(msg.ringRegistryKey);
    }

    public static ActivateRingPacket decode(PacketBuffer buf) {
        return new ActivateRingPacket(buf.readResourceLocation());
    }

    public static void handle(ActivateRingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null || msg.ringRegistryKey == null) return;

            Item targetItem = ForgeRegistries.ITEMS.getValue(msg.ringRegistryKey);
            if (!(targetItem instanceof IActivatableRing)) return;

            CuriosApi.getCuriosHelper().findEquippedCurio(
                    stack -> stack.getItem() == targetItem,
                    player
            ).ifPresent(triple -> {
                ItemStack foundStack = triple.getRight();
                ((IActivatableRing) targetItem).onKeyActivate(player, foundStack);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
