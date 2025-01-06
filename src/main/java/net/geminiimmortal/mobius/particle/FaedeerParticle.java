package net.geminiimmortal.mobius.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;

public class FaedeerParticle extends SpriteTexturedParticle {
    public FaedeerParticle(ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime = 20; // Duration of the particle in ticks
        this.gravity = -0.001F; // Set gravity if you want it to float or fall
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public int getLightColor(float partialTick) {
        return 0xF000F0; // Maximum brightness
    }


    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_LIT;
    }
}

