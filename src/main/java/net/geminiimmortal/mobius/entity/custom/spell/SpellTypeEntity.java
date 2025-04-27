package net.geminiimmortal.mobius.entity.custom.spell;

public interface SpellTypeEntity {
    void onCollideWith(net.geminiimmortal.mobius.entity.custom.SpellEntity other);
    SpellType getSpellType();
}
