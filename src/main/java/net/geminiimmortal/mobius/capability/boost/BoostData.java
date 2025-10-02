package net.geminiimmortal.mobius.capability.boost;

public class BoostData implements IBoostData {
    private long lastBoost = 0;
    private boolean ignoreNextFall = false;

    @Override
    public long getLastBoost() {
        return lastBoost;
    }

    @Override
    public void setLastBoost(long time) {
        this.lastBoost = time;
    }

    @Override
    public boolean shouldIgnoreNextFall() {
        return ignoreNextFall;
    }

    @Override
    public void setIgnoreNextFall(boolean value) {
        this.ignoreNextFall = value;
    }
}

