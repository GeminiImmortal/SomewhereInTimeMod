package net.geminiimmortal.mobius.faction;

public interface IRankedImperial {
    Rank getRank();

    enum Rank {
        GRUNT,
        REGULAR,
        OFFICER,
        COMMANDER,
        ELITE,
        CHAMPION
    }
}
