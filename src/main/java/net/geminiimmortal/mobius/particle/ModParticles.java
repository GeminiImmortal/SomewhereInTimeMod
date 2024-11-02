package net.geminiimmortal.mobius.particle;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MobiusMod.MOD_ID);

    public static final RegistryObject<BasicParticleType> FAEDEER_PARTICLE = PARTICLES.register("faedeer_particle",
            () -> new BasicParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }

}
