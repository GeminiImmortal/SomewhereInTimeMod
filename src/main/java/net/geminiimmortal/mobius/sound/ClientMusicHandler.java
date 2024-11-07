package net.geminiimmortal.mobius.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = "mobius", value = Dist.CLIENT)
public class ClientMusicHandler {

    private static final ResourceLocation CUSTOM_DIMENSION = new ResourceLocation("mobius", "mobius");
    public static final SoundEvent COLORS_MUSIC = new SoundEvent(new ResourceLocation("mobius", "colors"));
    public static final SoundEvent DREAM_STATE_MUSIC = new SoundEvent(new ResourceLocation("mobius", "dream_state"));

    private static final List<SoundEvent> CUSTOM_MUSIC_TRACKS = new ArrayList<>();
    private static final Map<SoundEvent, Integer> TRACK_DURATIONS = new HashMap<>();

    static {
        CUSTOM_MUSIC_TRACKS.add(COLORS_MUSIC);
        CUSTOM_MUSIC_TRACKS.add(DREAM_STATE_MUSIC);
        TRACK_DURATIONS.put(COLORS_MUSIC, 140000); // Duration in milliseconds
        TRACK_DURATIONS.put(DREAM_STATE_MUSIC, 126000);
    }

    private static final Random RANDOM = new Random();

    private static long trackStartTime = 0;
    private static int currentTrackDuration = 0;
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
        long currentTime = System.currentTimeMillis();

        if (!isPlayingCustomMusic || currentTime - trackStartTime > currentTrackDuration) {
            SoundEvent track = CUSTOM_MUSIC_TRACKS.get(RANDOM.nextInt(CUSTOM_MUSIC_TRACKS.size()));
            minecraft.getSoundManager().play(SimpleSound.forMusic(track));

            trackStartTime = currentTime;  // Set the start time for the new track
            currentTrackDuration = TRACK_DURATIONS.getOrDefault(track, 180000);  // Set duration for the track, default 3 mins
            isPlayingCustomMusic = true;
        }
    }

    private static void stopCustomMusic(Minecraft minecraft) {
        if (isPlayingCustomMusic) {
            minecraft.getSoundManager().stop();
            isPlayingCustomMusic = false;
            trackStartTime = 0;
        }
    }

    private static void stopVanillaMusic(Minecraft minecraft) {
        if (isPlayingCustomMusic) {
            minecraft.getMusicManager().stopPlaying();
        }
    }
}

