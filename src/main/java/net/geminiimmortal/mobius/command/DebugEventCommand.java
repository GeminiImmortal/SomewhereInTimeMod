package net.geminiimmortal.mobius.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.geminiimmortal.mobius.event.ImperialPatrolHandler;
import net.geminiimmortal.mobius.event.InfamyBountyHunterEventHandler;
import net.geminiimmortal.mobius.event.SkirmishEventHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class DebugEventCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("debugevent")
                .requires(source -> source.hasPermission(2)) // OP level
                .then(Commands.argument("type", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            builder.suggest("bounty");
                            builder.suggest("patrol");
                            builder.suggest("skirmish");
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            String type = StringArgumentType.getString(ctx, "type");
                            CommandSource source = ctx.getSource();

                            if (!(source.getEntity() instanceof ServerPlayerEntity)) {
                                source.sendFailure(new StringTextComponent("Must be run by a player."));
                                return 0;
                            }

                            ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();

                            switch (type.toLowerCase()) {
                                case "bounty":
                                    InfamyBountyHunterEventHandler.forceSpawnFor(player);
                                    source.sendSuccess(new StringTextComponent("Bounty hunter event triggered."), true);
                                    break;
                                case "patrol":
                                    ImperialPatrolHandler.forceSpawnFor(player);
                                    source.sendSuccess(new StringTextComponent("Imperial patrol event triggered."), true);
                                    break;
                                case "skirmish":
                                    SkirmishEventHandler.forceSpawnFor(player);
                                    source.sendSuccess(new StringTextComponent("Skirmish event triggered."), true);
                                    break;
                                default:
                                    source.sendFailure(new StringTextComponent("Unknown event type: " + type));
                                    return 0;
                            }

                            return 1;
                        })
                )
        );
    }
}

