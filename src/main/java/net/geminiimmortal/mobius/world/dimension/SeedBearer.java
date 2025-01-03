package net.geminiimmortal.mobius.world.dimension;

public class SeedBearer {
    private static long seed;
    private static boolean isSeedSet = false;

    public static synchronized void putInSeed(long newSeed) {
        if (!isSeedSet) {
            seed = newSeed;
            isSeedSet = true;
        } else {
            System.out.println("Seed already set; ignored new seed: " + newSeed);
        }
    }

    public static synchronized long giveMeSeed() {
        return seed;
    }
}


