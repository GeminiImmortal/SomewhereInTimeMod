package net.geminiimmortal.mobius.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.geminiimmortal.mobius.MobiusMod;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MobiusMod.MOD_ID);

    public static final RegistryObject<SoundEvent> MOBIUS_PORTAL_HUM =
            registerSoundEvent("mobius_portal");

    public static final RegistryObject<SoundEvent> HEART_GOLEM_HEAL =
            registerSoundEvent("heart_golem_heal");

    public static final RegistryObject<SoundEvent> WINDING_WOODS_AMBIENT_MOOD =
            registerSoundEvent("winding_woods_ambient_mood");

    public static final RegistryObject<SoundEvent> WINDING_WOODS_AMBIENT_LOOP =
            registerSoundEvent("winding_woods_ambient_loop");

    public static final RegistryObject<SoundEvent> FAEDEER_HURT =
            registerSoundEvent("faedeer_hurt");

    public static final RegistryObject<SoundEvent> MARCH_OF_THE_ILLAGERS =
            registerSoundEvent("march_of_the_illagers");

    public static final RegistryObject<SoundEvent> MUSIC_DISC_MARCH_OF_THE_ILLAGERS =
            registerSoundEvent("music_disc_march_of_the_illagers");

    public static final RegistryObject<SoundEvent> MONSTER_HUNTER_WORKS =
            registerSoundEvent("monster_hunter_working");

    public static final RegistryObject<SoundEvent> MAGISMITH_WORKS =
            registerSoundEvent("magismith_works");

    public static final RegistryObject<SoundEvent> SAGE_WORKS =
            registerSoundEvent("sage_works");

    public static final RegistryObject<SoundEvent> KNIFE_RAIN =
            registerSoundEvent("knife_rain");

    public static final RegistryObject<SoundEvent> BULLYRAG =
            registerSoundEvent("bullyrag");

    public static final RegistryObject<SoundEvent> MUSIC_DISC_BULLYRAG =
            registerSoundEvent("music_disc_bullyrag");

    public static final RegistryObject<SoundEvent> MUSIC_DISC_THE_LADY_RED =
            registerSoundEvent("music_disc_the_lady_red");

    public static final RegistryObject<SoundEvent> THE_LADY_RED =
            registerSoundEvent("the_lady_red");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(MobiusMod.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
