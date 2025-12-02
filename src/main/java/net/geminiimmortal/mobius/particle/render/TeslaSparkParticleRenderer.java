package net.geminiimmortal.mobius.particle.render;

import net.geminiimmortal.mobius.particle.FaedeerParticle;
import net.geminiimmortal.mobius.particle.TeslaSparkParticle;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

public class TeslaSparkParticleRenderer implements IParticleFactory<BasicParticleType> {
    private final IAnimatedSprite spriteSet;

    public TeslaSparkParticleRenderer(IAnimatedSprite spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(BasicParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        TeslaSparkParticle particle = new TeslaSparkParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
        particle.pickSprite(this.spriteSet);
        return particle;
    }
}

