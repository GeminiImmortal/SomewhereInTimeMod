package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.geminiimmortal.mobius.capability.infamy.IInfamy;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.BountyHunterEntity;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID)
public class InfamyBountyHunterEventHandler {

    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        tickCounter++;
        if (tickCounter < 200) return; // every 10 seconds
        tickCounter = 0;

        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);

        for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
            player.getCapability(ModCapabilities.INFAMY_CAPABILITY).ifPresent(cap -> {
                if (cap.getInfamyTier().ordinal() >= IInfamy.InfamyTier.NOTICED.ordinal()) {
                    long timeActive = player.level.getGameTime() - cap.getInfamyTriggerStart();

                    long targetTicks = getTargetTicksForPlayer(player); // 1–2 hours in ticks
                    if (timeActive >= targetTicks && player.level.canSeeSky(player.blockPosition()) && player.level.dimension().location().equals(ModDimensions.MOBIUS_WORLD.location())) {
                        cap.setInfamyTriggerStart(player.level.getGameTime());
                        triggerBountyHunterEvent(player);
                    }
                }
            });
        }
    }

    private static long getTargetTicksForPlayer(ServerPlayerEntity player) {
        // Use a seed based on UUID so it's random per player but stable
        Random rand = new Random(player.getUUID().hashCode());
        return 72000L + rand.nextInt(72000); // 1–2 hours in ticks
    }

    private static void triggerBountyHunterEvent(ServerPlayerEntity player) {
        player.sendMessage(new StringTextComponent("Bounty hunters have picked up your trail...").withStyle(TextFormatting.DARK_RED), player.getUUID());

        for (int i = 0; i < 3; i++) {
            BlockPos spawnPos = getValidSpawnNear(player);
            BountyHunterEntity hunter = new BountyHunterEntity(ModEntityTypes.BOUNTY_HUNTER.get(), player.level);
            hunter.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            hunter.setTarget(player);
            player.level.addFreshEntity(hunter);
        }
    }

    private static BlockPos getValidSpawnNear(ServerPlayerEntity player) {
        BlockPos base = player.blockPosition();
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            int x = base.getX() + rand.nextInt(16) - 8;
            int z = base.getZ() + rand.nextInt(16) - 8;
            int y = player.level.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos pos = new BlockPos(x, y, z);
            if (player.level.getBlockState(pos.below()).isSolidRender(player.level, pos.below())) {
                return pos;
            }
        }

        return base;
    }
}

