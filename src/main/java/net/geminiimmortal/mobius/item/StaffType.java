package net.geminiimmortal.mobius.item;

import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.LazyOptional;

public enum StaffType {

    PROTECTION_OBSIDIAN_FAE_LEATHER(20 * 20, Effects.ABSORPTION, 0, 8, 1200, LazyOptional.of(ModSounds.TIER_ONE_PROT_CAST::get)),
    PROTECTION_OBSIDIAN_MOLVAN_STEEL(20 * 20, Effects.ABSORPTION, 1, 16, 1800, LazyOptional.of(ModSounds.TIER_TWO_PROT_CAST::get)),
    LIGHTNING_OBSIDIAN_MOLVAN_STEEL(20 * 20, Effects.MOVEMENT_SPEED, 0, 32, 160, LazyOptional.of(ModSounds.TIER_TWO_LIGHTNING_CAST::get)),
    LIGHTNING_OBSIDIAN_FAE_LEATHER(20 * 20, Effects.MOVEMENT_SPEED, 1, 16, 400, LazyOptional.of(ModSounds.TIER_ONE_LIGHTNING_CAST::get)),
    FIRE_OBSIDIAN_FAE_LEATHER(20 * 20, Effects.FIRE_RESISTANCE, 0, 8, 30, LazyOptional.of(ModSounds.TIER_ONE_FIRE_CAST::get));

    private final int effectDuration;
    private final Effect effect;
    private final int effectLevel;
    private final int manaCost;
    private final int cooldown;
    private final LazyOptional<SoundEvent> sound;

    StaffType(int effectDuration, Effect effect, int effectLevel, int manaCost, int cooldown, LazyOptional<SoundEvent> sound) {
        this.effectDuration = effectDuration;
        this.effect = effect;
        this.effectLevel = effectLevel;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.sound = sound;
    }

    public int getEffectDuration() { return effectDuration; }
    public Effect getEffect() { return effect; }
    public int getEffectLevel() { return effectLevel; }
    public int getManaCost() { return manaCost; }
    public int getCooldown() { return cooldown; }
    public LazyOptional<SoundEvent> getSound() { return sound; }
}

