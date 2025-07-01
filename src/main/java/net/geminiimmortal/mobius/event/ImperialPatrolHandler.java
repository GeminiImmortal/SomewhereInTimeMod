package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.AbstractImperialEntity;
import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.geminiimmortal.mobius.entity.custom.SorcererEntity;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID)
public class ImperialPatrolHandler {

    private static final Map<UUID, Long> lastPatrolTimes = new HashMap<>();
    private static final long MIN_TICKS = 36000; // ~30 minutes
    private static final long MAX_TICKS = 144000; // ~2 hours

    private static final Random rand = new Random();

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);

        for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
            if (!shouldConsiderForPatrol(player)) continue;

            player.getCapability(ModCapabilities.INFAMY_CAPABILITY).ifPresent(cap -> {
                long currentTime = player.level.getGameTime();
                long lastCheck = cap.getLastPatrolCheck();

                long interval = getPlayerInterval(player.getUUID());

                if (currentTime - lastCheck >= interval) {
                    if (trySpawnPatrolNear(player)) {
                        cap.setLastPatrolCheck(currentTime);
                    }
                }
            });
        }
    }

    private static boolean shouldConsiderForPatrol(ServerPlayerEntity player) {
        return !player.isSpectator() &&
                player.level.dimension().location().equals(ModDimensions.MOBIUS_WORLD.location()) &&
                player.level.getDifficulty() != Difficulty.PEACEFUL &&
                player.isAlive() &&
                player.level.canSeeSky(player.blockPosition());
    }

    private static long getPlayerInterval(UUID uuid) {
        // Per-player randomized patrol interval between 10–15 min
        Random seeded = new Random(uuid.hashCode());
        return MIN_TICKS + seeded.nextInt((int)(MAX_TICKS - MIN_TICKS));
    }

    private static boolean trySpawnPatrolNear(ServerPlayerEntity player) {
        BlockPos base = player.blockPosition();
        World world = player.level;

        for (int i = 0; i < 10; i++) {
            int dx = rand.nextInt(64) - 24;
            int dz = rand.nextInt(64) - 24;
            BlockPos spawnPos = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, base.offset(dx, 0, dz));

            if (!world.canSeeSky(spawnPos)) continue;
            if (!world.getBlockState(spawnPos.below()).isSolidRender(world, spawnPos.below())) continue;

            spawnPatrol(world, spawnPos);
            return true;
        }

        return false;
    }

    private static void spawnPatrol(World world, BlockPos pos) {
        int count = 2 + rand.nextInt(3); // 2–4 guards
        AbstractImperialEntity leader = null;
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                leader = pickRandomClasses(world);
            }
            leader.setIsPartOfPatrol(true);
            FootmanEntity guard = new FootmanEntity(ModEntityTypes.FOOTMAN.get(), world);
            guard.setIsPartOfPatrol(true);
            double x = pos.getX() + rand.nextInt(5) - 2;
            double z = pos.getZ() + rand.nextInt(5) - 2;
            leader.setPos(x, pos.getY(), z);
            leader.finalizeSpawn((ServerWorld) world, world.getCurrentDifficultyAt(pos), SpawnReason.EVENT, null, null);
            guard.setPos(x, pos.getY(), z);
            guard.finalizeSpawn((ServerWorld) world, world.getCurrentDifficultyAt(pos), SpawnReason.EVENT, null, null);
            world.addFreshEntity(guard);
            world.addFreshEntity(leader);
        }

        // Optional: message nearby players
        List<ServerPlayerEntity> nearby = world.getEntitiesOfClass(ServerPlayerEntity.class,
                new AxisAlignedBB(pos).inflate(24), p -> !p.isSpectator());
        for (ServerPlayerEntity player : nearby) {
            player.sendMessage(new StringTextComponent("An Imperial patrol is moving through the area...").withStyle(TextFormatting.DARK_GRAY), player.getUUID());
        }
    }

    private static AbstractImperialEntity pickRandomClasses(World world) {
        Random rand = new Random();
        int officerIndex = rand.nextInt(2);
        officerIndex = 0; //TODO: REMOVE THIS WHEN DRAGOON IS IMPLEMENTED
        if (officerIndex == 0) {
            return new SorcererEntity(ModEntityTypes.SORCERER.get(), world);
        }
        return new FootmanEntity(ModEntityTypes.FOOTMAN.get(), world);
    }
}

