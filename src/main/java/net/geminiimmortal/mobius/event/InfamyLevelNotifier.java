package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.geminiimmortal.mobius.capability.infamy.IInfamy;
import net.geminiimmortal.mobius.item.custom.patchouli.LoreEntry;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.geminiimmortal.mobius.util.InfamyHelper;
import net.geminiimmortal.mobius.util.TitleUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID)
public class InfamyLevelNotifier {

    private static final Map<UUID, IInfamy.InfamyTier> previousTiers = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayerEntity)) return;

        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        UUID playerId = player.getUUID();

        player.getCapability(ModCapabilities.INFAMY_CAPABILITY).ifPresent(infamy -> {
            IInfamy.InfamyTier currentTier = InfamyHelper.get(player).getInfamyTier();
            IInfamy.InfamyTier previousTier = previousTiers.get(playerId);

            if (previousTier == null) {
                previousTiers.put(playerId, currentTier);
                return;
            }

            if (currentTier.ordinal() > previousTier.ordinal()) {
                triggerNotification(player, currentTier);
                previousTiers.put(playerId, currentTier);
            } else if (currentTier.ordinal() < previousTier.ordinal()) {
                previousTiers.put(playerId, currentTier); // downgrade silently
            }
        });
    }

    private static void triggerNotification(ServerPlayerEntity player, IInfamy.InfamyTier newTier) {
        // Play sound
        player.getLevel().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                ModSounds.INFAMY_RANK_UP_NORMAL.get(),
                SoundCategory.PLAYERS,
                1.0F,
                1.0F
        );

        // Send title
        TitleUtils.sendTitle(player,
                newTier.name(),
                "Imperial Infamy Increased",
                10, 60, 10,
                getColor(newTier)
        );

        // Grant advancement
        LoreEntry.grantAdvancementIfPossible(player, new ResourceLocation(MobiusMod.MOD_ID, "infamy/" + newTier.name().toLowerCase()));
    }

    private static TextFormatting getColor(IInfamy.InfamyTier tier) {
        switch (tier) {
            case MENACE: return TextFormatting.DARK_RED;
            case CRIMINAL: return TextFormatting.RED;
            case NOTICED: return TextFormatting.DARK_GREEN;
            case NUISANCE: return TextFormatting.YELLOW;
            default: return TextFormatting.GRAY;
        }
    }
}


