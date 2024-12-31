package net.geminiimmortal.mobius.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CloneShatterDamageSource extends DamageSource {
    public static final DamageSource CLONE_SHATTER = new CloneShatterDamageSource("clone_shatter");

    public CloneShatterDamageSource(String damageType) {
        super(damageType);
    }

    @Override
    public ITextComponent getLocalizedDeathMessage(LivingEntity entity) {
        String key = "death.attack." + this.msgId; // Uses the default translation key pattern
        return new TranslationTextComponent(key, entity.getDisplayName());
    }
}
