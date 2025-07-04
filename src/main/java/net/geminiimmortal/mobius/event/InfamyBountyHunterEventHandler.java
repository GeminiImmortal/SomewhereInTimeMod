package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.geminiimmortal.mobius.capability.infamy.IInfamy;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.BountyHunterEntity;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID)
public class InfamyBountyHunterEventHandler {

    private static final int BOUNTY_INTERVAL_TICKS = 216000;
    private static final int SPAWN_RADIUS = 40;
    private static final Map<UUID, Integer> tickCounters = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayerEntity) || !(event.player.level.dimension().equals(ModDimensions.MOBIUS_WORLD))) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        UUID playerId = player.getUUID();

        int ticks = tickCounters.getOrDefault(playerId, 0) + 1;
        if (ticks < BOUNTY_INTERVAL_TICKS) {
            tickCounters.put(playerId, ticks);
            return;
        }
        tickCounters.put(playerId, 0);

        player.getCapability(ModCapabilities.INFAMY_CAPABILITY).ifPresent(infamy -> {
            if (infamy.getInfamyTier().ordinal() < IInfamy.InfamyTier.NUISANCE.ordinal()) return;

            ServerWorld world = player.getLevel();
            BlockPos origin = player.blockPosition();

            for (int attempts = 0; attempts < 50; attempts++) {
                Random rand = world.random;
                int x = origin.getX() + rand.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;
                int z = origin.getZ() + rand.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;
                int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
                BlockPos pos = new BlockPos(x, y, z);

                if (world.canSeeSky(pos) && world.getBlockState(pos.below()).isSolidRender(world, pos.below())) {
                    for (int i = 0; i < 2; i++) {
                        BountyHunterEntity hunter = ModEntityTypes.BOUNTY_HUNTER.get().create(world);
                        if (hunter != null) {
                            BlockPos offset = pos.offset(rand.nextInt(6) - 3, 0, rand.nextInt(6) - 3);
                            hunter.moveTo(offset, rand.nextFloat() * 360.0F, 0);
                            world.addFreshEntity(hunter);
                        }
                    }

                    player.sendMessage(new StringTextComponent("Bounty hunters are tracking you...").withStyle(TextFormatting.DARK_RED), player.getUUID());
                    break;
                }
            }
        });
    }
    public static void forceSpawnFor(ServerPlayerEntity player) {
        // Bypass tick counter
        UUID id = player.getUUID();
        tickCounters.put(id, BOUNTY_INTERVAL_TICKS);

        // Simulate tick manually
        onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
    }


}