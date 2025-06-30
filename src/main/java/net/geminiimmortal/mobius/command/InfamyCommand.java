package net.geminiimmortal.mobius.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
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
                .requires(source -> source.hasPermission(2)) // OP level 2 required
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
                .then(Commands.literal("set")
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                            CommandSource source = ctx.getSource();

                                            target.getCapability(ModCapabilities.INFAMY_CAPABILITY).ifPresent(cap -> {
                                                cap.setInfamy(amount);

                                                // Optional: reset bounty timer if needed
                                                cap.setInfamyTriggerStart(target.level.getGameTime());

                                                source.sendSuccess(
                                                        new StringTextComponent(String.format(
                                                                "Set %s's infamy to %d (%s tier)",
                                                                target.getName().getString(),
                                                                amount,
                                                                cap.getInfamyTier().name()
                                                        )).withStyle(TextFormatting.GREEN),
                                                        false
                                                );
                                            });

                                            return 1;
                                        })
                                )
                        )
                )
        );
    }
}



