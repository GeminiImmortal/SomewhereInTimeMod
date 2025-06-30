package net.geminiimmortal.mobius.capability.infamy;

public interface IInfamy {
    int getInfamy();
    void setInfamy(int amount);
    void addInfamy(int amount);
    void subtractInfamy(int amount);

    InfamyTier getInfamyTier();

    enum InfamyTier {
        UNKNOWN,
        NOTICED,
        WATCHED,
        NUISANCE,
        CRIMINAL,
        LIBERATOR;

        public static InfamyTier fromPoints(int points) {
            if (points >= 10000) return LIBERATOR;
            if (points >= 100) return CRIMINAL;
            if (points >= 50) return NUISANCE;
            if (points >= 25) return WATCHED;
            if (points >= 10) return NOTICED;
            return UNKNOWN;
        }
    }
}
