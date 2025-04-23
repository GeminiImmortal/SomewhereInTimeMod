package net.geminiimmortal.mobius.util;

import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.network.play.server.STitlePacket.Type;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;

public class TitleUtils {
    public static void sendTitle(ServerPlayerEntity player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        // Set timing
        player.connection.send(new STitlePacket(fadeIn, stay, fadeOut));

        // Send title
        if (title != null && !title.isEmpty()) {
            player.connection.send(new STitlePacket(Type.TITLE, new StringTextComponent(TextFormatting.GOLD + title)));
        }

        // Send subtitle
        if (subtitle != null && !subtitle.isEmpty()) {
            player.connection.send(new STitlePacket(Type.SUBTITLE, new StringTextComponent(subtitle)));
        }
    }
}

