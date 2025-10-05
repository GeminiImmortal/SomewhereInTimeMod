package net.geminiimmortal.mobius.render.overlay;

public class GovernorBossBarParticleRenderer {
    public float x, y;
    public float vx, vy;
    public int lifetime;

    public GovernorBossBarParticleRenderer(float x, float y, float vx, float vy, int lifetime) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.lifetime = lifetime;
    }

    public boolean tick() {
        x += vx;
        y += vy;
        return --lifetime > 0;
    }
}
