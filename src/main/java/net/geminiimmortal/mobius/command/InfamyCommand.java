package net.geminiimmortal.mobius.command;

import com.mojang.brigadier.CommandDispatcher;
import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.geminiimmortal.mobius.capability.infamy.IInfamy;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class InfamyCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("infamy")
                .requires(source -> source.hasPermission(2)) // Requires OP level 2
                .then(Commands.literal("get")
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(ctx -> {
                                    ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
                                    CommandSource source = ctx.getSource();

                                    target.getCapability(ModCapabilities.INFAMY_CAPABILITY).ifPresent(cap -> {
                                        int infamy = cap.getInfamy();
                                        IInfamy.InfamyTier tier = cap.getInfamyTier();

                                        source.sendSuccess(
                                                new StringTextComponent(String.format(
                                                        "%s has %d infamy (%s tier)",
                                                        target.getName().getString(),
                                                        infamy,
                                                        tier.name()
                                                )).withStyle(TextFormatting.YELLOW),
                                                false
                                        );
                                    });

                                    return 1;
                                })
                        )
                )
        );
    }
}


