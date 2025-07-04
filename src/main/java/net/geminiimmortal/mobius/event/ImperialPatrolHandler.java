package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.geminiimmortal.mobius.entity.custom.ImperialSergeantEntity;
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
public class ImperialPatrolHandler {

    private static final int PATROL_INTERVAL_TICKS = 144000;
    private static final int SPAWN_RADIUS = 60;
    private static final Map<UUID, Integer> tickCounters = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayerEntity) || !(event.player.level.dimension().equals(ModDimensions.MOBIUS_WORLD))) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        UUID playerId = player.getUUID();

        int ticks = tickCounters.getOrDefault(playerId, 0) + 1;
        if (ticks < PATROL_INTERVAL_TICKS) {
            tickCounters.put(playerId, ticks);
            return;
        }
        tickCounters.put(playerId, 0);

        ServerWorld world = player.getLevel();
        BlockPos origin = player.blockPosition();

        for (int attempts = 0; attempts < 50; attempts++) {
            Random rand = world.random;
            int x = origin.getX() + rand.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;
            int z = origin.getZ() + rand.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;
            int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos pos = new BlockPos(x, y, z);

            if (world.canSeeSky(pos) && world.getBlockState(pos.below()).isSolidRender(world, pos.below())) {
                ImperialSergeantEntity officer = ModEntityTypes.IMPERIAL_SERGEANT.get().create(world);
                if (officer != null) {
                    officer.setIsPartOfPatrol(true);
                    officer.moveTo(pos, rand.nextFloat() * 360.0F, 0);
                    world.addFreshEntity(officer);

                    for (int i = 0; i < 3; i++) {
                        FootmanEntity soldier = ModEntityTypes.FOOTMAN.get().create(world);
                        if (soldier != null) {
                            soldier.setIsPartOfPatrol(true);
                            soldier.moveTo(officer.blockPosition(), rand.nextFloat() * 360.0F, 0);
                            world.addFreshEntity(soldier);
                        }
                    }

                    player.sendMessage(new StringTextComponent("An imperial patrol is moving through the area...").withStyle(TextFormatting.DARK_GRAY), player.getUUID());
                    break;
                }
            }
        }
    }

    private static BlockPos findSafeSpawn(ServerWorld world, BlockPos center, int radius) {
        Random rand = world.random;

        for (int attempts = 0; attempts < 30; attempts++) {
            int dx = rand.nextInt(radius * 2) - radius;
            int dz = rand.nextInt(radius * 2) - radius;
            int x = center.getX() + dx;
            int z = center.getZ() + dz;
            int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos pos = new BlockPos(x, y, z);

            if (!world.canSeeSky(pos)) continue;
            if (!world.getBlockState(pos.below()).isSolidRender(world, pos.below())) continue;
            if (!world.isEmptyBlock(pos)) continue;
            if (!world.isEmptyBlock(pos.above())) continue;

            return pos;
        }

        return null; // No safe position found
    }


    public static void forceSpawnFor(ServerPlayerEntity player) {
        UUID id = player.getUUID();
        tickCounters.put(id, PATROL_INTERVAL_TICKS);
        onPlayerTick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
    }

}

