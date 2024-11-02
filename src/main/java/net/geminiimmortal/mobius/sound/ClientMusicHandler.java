package net.geminiimmortal.mobius.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = "mobius", value = Dist.CLIENT)
public class ClientMusicHandler {

    private static final ResourceLocation CUSTOM_DIMENSION = new ResourceLocation("mobius", "mobius");

    public static final SoundEvent UPSIDE_DOWN_MUSIC = new SoundEvent(new ResourceLocation("mobius", "upside_down"));
    public static final SoundEvent COLORS_MUSIC = new SoundEvent(new ResourceLocation("mobius", "colors"));

    private static final List<SoundEvent> CUSTOM_MUSIC_TRACKS = new ArrayList<>();
    static {
        CUSTOM_MUSIC_TRACKS.add(0,UPSIDE_DOWN_MUSIC);
        CUSTOM_MUSIC_TRACKS.add(1,COLORS_MUSIC);
    }

    private static final int TRACK_SWITCH_DELAY = 6000; // Time between tracks
    private static final Random RANDOM = new Random();

    private static long trackStartTime = 0;


    private static boolean isPlayingCustomMusic = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player != null && minecraft.level != null) {
            World world = minecraft.level;
            if (world.dimension().location().equals(CUSTOM_DIMENSION)) {
                stopVanillaMusic(minecraft);
                startCustomMusic(minecraft);
            } else {
                stopCustomMusic(minecraft);
            }
        }
    }

    private static void startCustomMusic(Minecraft minecraft) {
        if (!isPlayingCustomMusic) {
            SoundEvent track = CUSTOM_MUSIC_TRACKS.get(RANDOM.nextInt(CUSTOM_MUSIC_TRACKS.size()));
            minecraft.getSoundManager().play(SimpleSound.forMusic(track));
            isPlayingCustomMusic = true;
        }
    }

    private static void stopCustomMusic(Minecraft minecraft) {
        if (isPlayingCustomMusic) {
            minecraft.getSoundManager().stop();
            isPlayingCustomMusic = false;
        }
    }

    private static void stopVanillaMusic(Minecraft minecraft) {
        if (isPlayingCustomMusic) {
            minecraft.getMusicManager().stopPlaying();
        }

    }
}
