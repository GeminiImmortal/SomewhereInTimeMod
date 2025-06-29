package net.geminiimmortal.mobius;

import net.geminiimmortal.mobius.sound.ModSounds;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.util.SoundEvent;

public class MusicTickerHook {
    private static BackgroundMusicSelector override = null;

    public static BackgroundMusicSelector resolveMusicSelector(BackgroundMusicSelector original) {
        // 1. Override takes highest priority
        if (override != null) return override;

        Minecraft mc = Minecraft.getInstance();

        // 2. Check if in mod dimension
        if (mc.level != null && mc.player != null &&
                mc.level.dimension().equals(ModDimensions.MOBIUS_WORLD)) {

            // Use custom music for mod dimension
            return new BackgroundMusicSelector(ModSounds.BACKGROUND_MUSIC.get(), 20, 100, true);
        }

        // 3. Fallback to vanilla behavior
        return original;
    }

    /**
     * Sets a persistent music override that will be played instead of normal music
     * @param selector Music selector to play, or null to clear override
     */
    public static void setMusicOverride(BackgroundMusicSelector selector) {
        override = selector;
    }

    /**
     * Clears any active music override
     */
    public static void clearMusicOverride() {
        override = null;
    }

    /**
     * Special selector that clears itself after being used once
     */
    private static class AutoClearingSelector extends BackgroundMusicSelector {
        public AutoClearingSelector(SoundEvent event, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
            super(event, minDelay, maxDelay, replaceCurrentMusic);
        }

        @Override
        public boolean replaceCurrentMusic() {
            clearMusicOverride();
            return super.replaceCurrentMusic();
        }
    }
}
