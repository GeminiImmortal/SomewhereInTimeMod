package net.geminiimmortal.mobius.world.dimension;

// SeedBearer.java
public class SeedBearer {
    private static final ThreadLocal<Long> DIMENSION_SEED = new ThreadLocal<>();
    private static volatile long GLOBAL_SEED = 0;

    public static void setSeed(long seed) {
        GLOBAL_SEED = seed;
        DIMENSION_SEED.set(seed); // Set initial value
    }

    public static long getSeed() {
        Long seed = DIMENSION_SEED.get();
        return seed != null ? seed : GLOBAL_SEED;
    }
}


