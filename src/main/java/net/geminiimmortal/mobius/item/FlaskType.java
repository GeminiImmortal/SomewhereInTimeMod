package net.geminiimmortal.mobius.item;

import net.minecraft.item.Rarity;

public enum FlaskType {

    MANA_VIAL(1024, Rarity.COMMON),
    MANA_FLASK(2048, Rarity.UNCOMMON),
    MANA_ALEMBIC(4096, Rarity.RARE),
    GAIA_FLASK(16384, Rarity.EPIC);

    private final int manaCapacity;
    private final Rarity rarity;

    FlaskType(int manaCapacity, Rarity rarity) {
        this.manaCapacity = manaCapacity;
        this.rarity = rarity;
    }

    public int getManaCapacity() { return manaCapacity; }
    public Rarity getRarity() { return rarity; }
}

