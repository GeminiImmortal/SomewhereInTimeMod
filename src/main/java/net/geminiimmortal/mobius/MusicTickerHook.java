package net.geminiimmortal.mobius;

import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.BackgroundMusicTracks;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression"})
public class MusicTickerHook {
    public static BackgroundMusicSelector music(BackgroundMusicSelector music) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null && (music == BackgroundMusicTracks.CREATIVE || music == BackgroundMusicTracks.UNDER_WATER) && Minecraft.getInstance().level.dimension().getRegistryName().toString().equals(ModDimensions.MOBIUS_WORLD.getRegistryName().toString()))
            return Minecraft.getInstance().level.getBiomeManager().getNoiseBiomeAtPosition(Minecraft.getInstance().player.blockPosition()).getBackgroundMusic().orElse(BackgroundMusicTracks.GAME);
        return music;
    }
}
