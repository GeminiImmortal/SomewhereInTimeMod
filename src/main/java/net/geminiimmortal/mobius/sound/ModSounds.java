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

    public static final RegistryObject<SoundEvent> BOREALIS_MUSIC =
            registerSoundEvent("borealis");

    public static final RegistryObject<SoundEvent> HEART_GOLEM_HEAL =
            registerSoundEvent("heart_golem_heal");

    public static final RegistryObject<SoundEvent> WINDING_WOODS_AMBIENT_MOOD =
            registerSoundEvent("winding_woods_ambient_mood");

    public static final RegistryObject<SoundEvent> WINDING_WOODS_AMBIENT_LOOP =
            registerSoundEvent("winding_woods_ambient_loop");

    public static final RegistryObject<SoundEvent> FAEDEER_HURT =
            registerSoundEvent("faedeer_hurt");

    public static final RegistryObject<SoundEvent> MOLVAN_ANGRY =
            registerSoundEvent("molvan_angry");

    public static final RegistryObject<SoundEvent> MARCH_OF_THE_ILLAGERS =
            registerSoundEvent("march_of_the_illagers");

    public static final RegistryObject<SoundEvent> MUSIC_DISC_MARCH_OF_THE_ILLAGERS =
            registerSoundEvent("music_disc_march_of_the_illagers");

    public static final RegistryObject<SoundEvent> MONSTER_HUNTER_WORKS =
            registerSoundEvent("monster_hunter_working");

    public static final RegistryObject<SoundEvent> MAGISMITH_WORKS =
            registerSoundEvent("magismith_works");

    public static final RegistryObject<SoundEvent> DASH_IMPACT =
            registerSoundEvent("dash_impact");

    public static final RegistryObject<SoundEvent> ESSENCE_CHANNELER =
            registerSoundEvent("essence_channeler");

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

    public static final RegistryObject<SoundEvent> ASTRAL_CONDUIT =
            registerSoundEvent("astral_conduit");

    public static final RegistryObject<SoundEvent> BONE_WOLF_DEATH =
            registerSoundEvent("bone_wolf_cry");

    public static final RegistryObject<SoundEvent> INFERNAL_BRIAR_AMBIENT =
            registerSoundEvent("infernal_briar_ambient");

    public static final RegistryObject<SoundEvent> ANGLERFISH_DEATH =
            registerSoundEvent("anglerfish_death");

    public static final RegistryObject<SoundEvent> TIER_ONE_PROT_CAST =
            registerSoundEvent("tier_one_prot_staff_cast");

    public static final RegistryObject<SoundEvent> TIER_TWO_PROT_CAST =
            registerSoundEvent("tier_two_prot_staff_cast");

    public static final RegistryObject<SoundEvent> TIER_TWO_LIGHTNING_CAST =
            registerSoundEvent("tier_two_lightning_staff_cast");

    public static final RegistryObject<SoundEvent> TIER_ONE_LIGHTNING_CAST =
            registerSoundEvent("tier_one_lightning_staff_cast");

    public static final RegistryObject<SoundEvent> TIER_ONE_FIRE_CAST =
            registerSoundEvent("tier_one_fire_staff_cast");

    public static final RegistryObject<SoundEvent> GRAVITAS =
            registerSoundEvent("gravitas");

    public static final RegistryObject<SoundEvent> HURRICANE =
            registerSoundEvent("hurricane");

    public static final RegistryObject<SoundEvent> CAPTAIN_HURTS =
            registerSoundEvent("captain_hurts");

    public static final RegistryObject<SoundEvent> CAPTAIN_SCREAMS =
            registerSoundEvent("captain_scream");

    public static final RegistryObject<SoundEvent> CAPTAIN_ULTI =
            registerSoundEvent("captain_ulti");

    public static final RegistryObject<SoundEvent> LIGHTNING_SPELL_FX =
            registerSoundEvent("lightning_spell_fx");

    public static final RegistryObject<SoundEvent> ARCANE_BOLT_FX =
            registerSoundEvent("arcane_bolt_fx");

    public static final RegistryObject<SoundEvent> OBLITERATOR =
            registerSoundEvent("obliterator");

    public static final RegistryObject<SoundEvent> ARCANE_NUKE_FX =
            registerSoundEvent("arcane_nuke_fx");

    public static final RegistryObject<SoundEvent> AOE_TELEGRAPH_FX =
            registerSoundEvent("aoe_telegraph_fx");

    public static final RegistryObject<SoundEvent> ARCANE_BOLT_FX_DEEP =
            registerSoundEvent("arcane_bolt_fx_deep");

    public static final RegistryObject<SoundEvent> BARRIER =
            registerSoundEvent("barrier");

    public static final RegistryObject<SoundEvent> BARRIER_IMPACT =
            registerSoundEvent("barrier_impact");

    public static final RegistryObject<SoundEvent> BARRIER_NEGATE =
            registerSoundEvent("barrier_negate");

    public static final RegistryObject<SoundEvent> SPELL_COLLISION =
            registerSoundEvent("spell_collision");

    public static final RegistryObject<SoundEvent> SPARKS =
            registerSoundEvent("sparks");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(MobiusMod.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
