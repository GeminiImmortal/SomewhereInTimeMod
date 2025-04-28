package net.geminiimmortal.mobius.entity.custom.spell;

public interface SpellTypeEntity {
    void onCollideWith(SpellTypeEntity other);
    SpellType getSpellType();
}
