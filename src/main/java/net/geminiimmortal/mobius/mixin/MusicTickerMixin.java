package net.geminiimmortal.mobius.mixin;

import net.geminiimmortal.mobius.MusicTickerHook;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.MusicTicker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MusicTicker.class)
public abstract class MusicTickerMixin {

    @ModifyVariable(
            method = "tick",
            at = @At("STORE"),
            ordinal = 0
    )
    private BackgroundMusicSelector injectCustomMusic(BackgroundMusicSelector original) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level != null && mc.player != null &&
                mc.level.dimension().location().equals(ModDimensions.MOBIUS_WORLD.location())) {

            original = new BackgroundMusicSelector(ModSounds.BACKGROUND_MUSIC.get(), 100, 300, true);
        }

        return MusicTickerHook.music(original);
    }
}






