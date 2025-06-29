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
        return MusicTickerHook.resolveMusicSelector(original);
    }
}






