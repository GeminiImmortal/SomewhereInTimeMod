package net.geminiimmortal.mobius.item.custom.patchouli;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.util.ResourceLocation;

public enum LoreEntryType {
    LORE_FRAGMENT_SMUGGLER_CAMP_1("lore/smuggler_camp_1", "lore/lore_smuggler_camp_1"),
    IMPERIAL_DECREE_0("lore/imperial_decree_0", "lore/imperial_decree_0");

    private final ResourceLocation entryId;
    private final ResourceLocation entryAdv;

    LoreEntryType(String entryPath, String entryAdvPath) {
        this.entryId = new ResourceLocation(MobiusMod.MOD_ID, entryPath);
        this.entryAdv = new ResourceLocation(MobiusMod.MOD_ID, entryAdvPath);
    }

    public ResourceLocation getEntryId() {
        return entryId;
    }

    public ResourceLocation getEntryAdv() {
        return entryAdv;
    }
}

