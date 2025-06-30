package net.geminiimmortal.mobius.capability.infamy;

public class Infamy implements IInfamy {
    private int infamy = 0;

    @Override
    public int getInfamy() { return infamy; }
    @Override
    public void setInfamy(int amount) { this.infamy = Math.max(0, amount); }
    @Override
    public void addInfamy(int amount) { this.infamy += amount; }
    @Override
    public void subtractInfamy(int amount) { this.infamy = Math.max(0, this.infamy - amount); }
    @Override
    public InfamyTier getInfamyTier() { return InfamyTier.fromPoints(infamy); }

    private long triggerStart = -1;

    @Override
    public long getInfamyTriggerStart() {
        return triggerStart;
    }

    @Override
    public void setInfamyTriggerStart(long ticks) {
        this.triggerStart = ticks;
    }

}


