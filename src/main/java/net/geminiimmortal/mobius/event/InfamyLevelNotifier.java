package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.geminiimmortal.mobius.capability.infamy.IInfamy;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.geminiimmortal.mobius.util.InfamyHelper;
import net.geminiimmortal.mobius.util.TitleUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InfamyLevelNotifier {

    private static final Map<UUID, IInfamy.InfamyTier> previousTiers = new HashMap<>();

    public static void tick(ServerPlayerEntity player) {
        player.getCapability(ModCapabilities.INFAMY_CAPABILITY).ifPresent(infamy -> {
            IInfamy.InfamyTier currentTier = InfamyHelper.get(player).getInfamyTier();
            IInfamy.InfamyTier previousTier = previousTiers.get(player.getUUID());

            if (previousTier == null) {
                previousTiers.put(player.getUUID(), currentTier);
                return;
            }

            if (currentTier.ordinal() > previousTier.ordinal()) {
                triggerNotification(player, currentTier);
                previousTiers.put(player.getUUID(), currentTier);
            } else if (currentTier.ordinal() < previousTier.ordinal()) {
                previousTiers.put(player.getUUID(), currentTier); // downgrade silently
            }
        });
    }

    private static void triggerNotification(ServerPlayerEntity player, IInfamy.InfamyTier newTier) {
        // 🔊 Play sound
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

        // 📝 Send title
        TitleUtils.sendTitle(player,
                newTier.name(),
                "Imperial Infamy Increased",
                10, 60, 10, getColor(newTier)
        );
    }

    private static TextFormatting getColor(IInfamy.InfamyTier tier) {
        if (tier.equals(IInfamy.InfamyTier.MENACE)) {
            return TextFormatting.DARK_RED;
        }
        if (tier.equals(IInfamy.InfamyTier.CRIMINAL)) {
            return TextFormatting.RED;
        }
        if (tier.equals(IInfamy.InfamyTier.NOTICED)) {
            return TextFormatting.DARK_GREEN;
        }
        if (tier.equals(IInfamy.InfamyTier.NUISANCE)) {
            return TextFormatting.YELLOW;
        }
        return TextFormatting.GRAY;
    }
}

