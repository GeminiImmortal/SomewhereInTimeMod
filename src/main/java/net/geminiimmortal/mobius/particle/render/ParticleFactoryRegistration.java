package net.geminiimmortal.mobius.particle.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleFactoryRegistration {
    @SubscribeEvent
    public static void onParticleFactoryRegister(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(
                ModParticles.FAEDEER_PARTICLE.get(),
                FaedeerParticleRenderer::new
        );
    }
}

