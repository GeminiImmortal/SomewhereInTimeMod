package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.entity.custom.spell.SpellType;
import net.geminiimmortal.mobius.entity.custom.spell.SpellTypeEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class AbstractImperialMageEntity extends AbstractImperialEntity {
    protected AbstractImperialMageEntity(EntityType<? extends CreatureEntity> entityType, World world) {
        super(entityType, world);
    }

    abstract SpellTypeEntity getCastedSpellEntity();

    abstract SpellType getCastedSpellType();
}
