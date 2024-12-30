package net.geminiimmortal.mobius.effects;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.effects.custom.LordDecreeEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.RegistryObject;
import net.minecraft.potion.EffectType;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, MobiusMod.MOD_ID);

    public static final RegistryObject<Effect> LORD_DECREE_EFFECT = EFFECTS.register("lord_decree",
            () -> new LordDecreeEffect(EffectType.HARMFUL, 0xFFD700));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}

