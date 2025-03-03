package net.geminiimmortal.mobius.item;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public enum StaffType {

    PROTECTION_OBSIDIAN_FAE_LEATHER(20 * 20, Effects.ABSORPTION, 0, 8, 1200, new SoundEvent(new ResourceLocation(MobiusMod.MOD_ID, "tier_one_prot_staff_cast"))),
    PROTECTION_OBSIDIAN_MOLVAN_STEEL(20 * 20, Effects.ABSORPTION, 1, 16, 1800, new SoundEvent(new ResourceLocation(MobiusMod.MOD_ID, "tier_two_prot_staff_cast")));

    private final int effectDuration;
    private final Effect effect;
    private final int effectLevel;
    private final int manaCost;
    private final int cooldown;
    private final SoundEvent sound;

    StaffType(int effectDuration, Effect effect, int effectLevel, int manaCost, int cooldown, SoundEvent sound) {
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
    public SoundEvent getSound() { return sound; }
}

