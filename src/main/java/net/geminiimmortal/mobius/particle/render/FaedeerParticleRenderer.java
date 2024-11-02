package net.geminiimmortal.mobius.particle.render;

import net.geminiimmortal.mobius.particle.FaedeerParticle;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

public class FaedeerParticleRenderer implements IParticleFactory<BasicParticleType> {
    private final IAnimatedSprite spriteSet;

    public FaedeerParticleRenderer(IAnimatedSprite spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(BasicParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        FaedeerParticle particle = new FaedeerParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
        particle.pickSprite(this.spriteSet);
        return particle;
    }
}

