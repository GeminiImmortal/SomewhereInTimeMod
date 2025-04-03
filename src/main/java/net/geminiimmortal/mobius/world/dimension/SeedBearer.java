package net.geminiimmortal.mobius.world.dimension;

public class SeedBearer {

    private static long seed = 0;

    public static void putInSeed(long seedInput) {
        seed = seedInput;
    }

    public static long giveMeSeed() {
        System.out.println("SeedBearer got seed: " + seed);

        return seed;
    }
}



