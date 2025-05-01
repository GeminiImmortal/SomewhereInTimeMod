package net.geminiimmortal.mobius.world.dimension;

public class SeedBearer {
    private static final ThreadLocal<Long> SEED = ThreadLocal.withInitial(() -> 0L);
    private static long seed;

    public static void putInSeed(long seedInput) {
        seed = seedInput;
    }

    public static long giveMeSeed() {
        System.out.println("SeedBearer got seed: " + seed);

        return seed;
    }
}



