package net.geminiimmortal.mobius.util;

import java.util.HashMap;
import java.util.Map;

public class JournalContent {
    private static final Map<Integer, String> PAGE_TEXT = new HashMap<>();
    static {
        PAGE_TEXT.put(1, "Day 1: The fog is thick and strange...");
        PAGE_TEXT.put(2, "Day 2: I heard whispers in the trees...");
        // and so on...
    }

    public static String getPageText(int pageNumber) {
        return PAGE_TEXT.getOrDefault(pageNumber, "Missing Page.");
    }
}

