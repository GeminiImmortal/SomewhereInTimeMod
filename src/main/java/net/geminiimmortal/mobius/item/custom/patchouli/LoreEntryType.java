package net.geminiimmortal.mobius.item.custom.patchouli;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.PatchouliAPI;

public enum LoreEntryType {
    LORE_FRAGMENT_SMUGGLER_CAMP_0("lore/smuggler_camp_0", "lore/lore_smuggler_camp_0"),
    LORE_FRAGMENT_SMUGGLER_CAMP_1("lore/smuggler_camp_1", "lore/lore_smuggler_camp_1");

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

    public void unlock(ServerPlayerEntity player) {
        PatchouliAPI.get().openBookEntry(player, new ResourceLocation(MobiusMod.MOD_ID, "mobius_guide"), entryId, 1);
    }
}

