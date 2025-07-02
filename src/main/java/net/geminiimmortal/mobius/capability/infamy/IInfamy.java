package net.geminiimmortal.mobius.capability.infamy;

public interface IInfamy {
    int getInfamy();
    void setInfamy(int amount);
    void addInfamy(int amount);
    void subtractInfamy(int amount);

    long getInfamyTriggerStart(); // epoch or ticks
    void setInfamyTriggerStart(long ticks);

    long getLastPatrolCheck();
    void setLastPatrolCheck(long ticks);



    InfamyTier getInfamyTier();

    enum InfamyTier {
        UNKNOWN,
        NOTICED,
        NUISANCE,
        CRIMINAL,
        MENACE,
        LIBERATOR;

        public static InfamyTier fromPoints(int points) {
            if (points >= 25000) return MENACE;
            if (points >= 10000) return CRIMINAL;
            if (points >= 2500) return NUISANCE;
            if (points >= 100) return NOTICED;
            return UNKNOWN;
        }
    }
}
