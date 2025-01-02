package net.geminiimmortal.mobius.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class EctoplasmDamageSource extends DamageSource {
    public static final DamageSource ECTOPLASM = new EctoplasmDamageSource("ectoplasm");

    public EctoplasmDamageSource(String damageType) {
        super(damageType);
    }

    @Override
    public ITextComponent getLocalizedDeathMessage(LivingEntity entity) {
        String key = "death.attack." + this.msgId;
        return new TranslationTextComponent(key, entity.getDisplayName());
    }
}
