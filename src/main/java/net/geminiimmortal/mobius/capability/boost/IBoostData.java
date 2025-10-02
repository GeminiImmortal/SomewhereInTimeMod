package net.geminiimmortal.mobius.capability.boost;

public interface IBoostData {
    long getLastBoost();
    void setLastBoost(long time);

    boolean shouldIgnoreNextFall();
    void setIgnoreNextFall(boolean value);
}


