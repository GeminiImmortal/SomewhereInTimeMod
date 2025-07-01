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
            if (points >= 250) return MENACE;
            if (points >= 100) return CRIMINAL;
            if (points >= 25) return NUISANCE;
            if (points >= 10) return NOTICED;
            return UNKNOWN;
        }
    }
}
