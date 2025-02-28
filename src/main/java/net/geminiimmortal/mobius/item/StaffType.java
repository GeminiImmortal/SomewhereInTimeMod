package net.geminiimmortal.mobius.item;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;

public enum StaffType {
    PROTECTION(20 * 20, Effects.ABSORPTION, 3, 8, 2400),  // 20s Absorption III, 100 mana cost
    FIREBALL(10 * 20, Effects.FIRE_RESISTANCE, 1, 4, 200), // 10s Fire Res, 150 mana cost
    LIGHTNING(30 * 20, Effects.MOVEMENT_SPEED, 2, 16, 200); // 30s Speed II, 200 mana cost

    private final int effectDuration;
    private final Effect effect;
    private final int effectLevel;
    private final int manaCost;
    private final int cooldown;

    StaffType(int effectDuration, Effect effect, int effectLevel, int manaCost, int cooldown) {
        this.effectDuration = effectDuration;
        this.effect = effect;
        this.effectLevel = effectLevel;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
    }

    public int getEffectDuration() { return effectDuration; }
    public Effect getEffect() { return effect; }
    public int getEffectLevel() { return effectLevel; }
    public int getManaCost() { return manaCost; }
    public int getCooldown() { return cooldown; }
}

