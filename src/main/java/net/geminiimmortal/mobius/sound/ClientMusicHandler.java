package net.geminiimmortal.mobius.sound;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
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
    public static final SoundEvent BOREALIS_MUSIC = new SoundEvent(new ResourceLocation("mobius", "borealis"));
    public static final SoundEvent BULLYRAG = new SoundEvent(new ResourceLocation(MobiusMod.MOD_ID,"bullyrag"));

    private static final List<SoundEvent> CUSTOM_MUSIC_TRACKS = new ArrayList<>();
    private static final Map<SoundEvent, Integer> TRACK_DURATIONS = new HashMap<>();
    private static boolean governor;

    static {
        CUSTOM_MUSIC_TRACKS.add(COLORS_MUSIC);
        CUSTOM_MUSIC_TRACKS.add(DREAM_STATE_MUSIC);
        CUSTOM_MUSIC_TRACKS.add(BOREALIS_MUSIC);
        TRACK_DURATIONS.put(COLORS_MUSIC, 2460); // Duration in ticks (20 ticks = 1 second)
        TRACK_DURATIONS.put(DREAM_STATE_MUSIC, 2520);
        TRACK_DURATIONS.put(BOREALIS_MUSIC, 3080);
    }

    private static final Random RANDOM = new Random();

    private static long trackStartTime = 0; // In ticks
    private static int currentTrackDuration = 0; // In ticks
    private static boolean isPlayingCustomMusic = false;

    public static void setGovernor(boolean alive) {
        governor = alive;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player != null && minecraft.level != null) {
            World world = minecraft.level;
            if (world.dimension().location().equals(CUSTOM_DIMENSION)) {
                stopVanillaMusic(minecraft);
                startCustomMusic(minecraft, world.getGameTime());
            } else {
                stopCustomMusic(minecraft);
            }
        }
    }

    private static void startCustomMusic(Minecraft minecraft, long gameTime) {
        if (governor && (!isPlayingCustomMusic || gameTime - trackStartTime > currentTrackDuration)) {
            stopVanillaMusic(minecraft);
            stopCustomMusic(minecraft);
            minecraft.getSoundManager().play(SimpleSound.forMusic(BULLYRAG));

            trackStartTime = gameTime;
            currentTrackDuration = 2610;
            isPlayingCustomMusic = true;
        } else if (!isPlayingCustomMusic || gameTime - trackStartTime > currentTrackDuration) {
            minecraft.getSoundManager().stop();
            trackStartTime = 0;
            SoundEvent track = CUSTOM_MUSIC_TRACKS.get(RANDOM.nextInt(CUSTOM_MUSIC_TRACKS.size()));
            minecraft.getSoundManager().play(SimpleSound.forMusic(track));

            trackStartTime = gameTime;  // Set the start time for the new track
            currentTrackDuration = TRACK_DURATIONS.getOrDefault(track, 3600);  // Set duration for the track, default 3 minutes (3600 ticks)
            isPlayingCustomMusic = true;
        }

    }

    public static void stopCustomMusic(Minecraft minecraft) {
        if (isPlayingCustomMusic) {
            minecraft.getSoundManager().stop();
            isPlayingCustomMusic = false;
            trackStartTime = 0;
        }
    }

    public static void stopVanillaMusic(Minecraft minecraft) {
        if (isPlayingCustomMusic) {
            minecraft.getMusicManager().stopPlaying();
        }
    }

    public static void pauseCustomMusic(Minecraft minecraft) {
        stopCustomMusic(minecraft);
        stopVanillaMusic(minecraft);
    }

    public static void playCourtWizardBossMusic(Minecraft minecraft) {
        if (isPlayingCustomMusic) {
            minecraft.getSoundManager().stop();
            minecraft.getSoundManager().play(SimpleSound.forMusic(ModSounds.THE_LADY_RED.get()));
        }
    }

    public static void playGovernorBossMusic(Minecraft minecraft, GovernorEntity boss) {
        if (boss.isAlive()) {
            governor = true;
        }
    }
}

