package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.ImperialGuardEntity;
import net.geminiimmortal.mobius.entity.custom.SorcererEntity;
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
    private static final int SPAWN_RADIUS = 100;
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
            int y = world.getHeight(Heightmap.Type.WORLD_SURFACE, x, z);
            BlockPos pos = new BlockPos(x, y, z);

            if (world.canSeeSky(pos) && world.getBlockState(pos.below()).isSolidRender(world, pos.below())) {
                SorcererEntity officer = ModEntityTypes.SORCERER.get().create(world);
                if (officer != null) {
                    officer.moveTo(pos, rand.nextFloat() * 360.0F, 0);
                    world.addFreshEntity(officer);

                    for (int i = 0; i < 3; i++) {
                        BlockPos offset = pos.offset(rand.nextInt(6) - 3, 0, rand.nextInt(6) - 3);
                        ImperialGuardEntity soldier = ModEntityTypes.IMPERIAL_GUARD.get().create(world);
                        if (soldier != null) {
                            soldier.setIsPartOfPatrol(true);
                            soldier.moveTo(offset, rand.nextFloat() * 360.0F, 0);
                            world.addFreshEntity(soldier);
                        }
                    }

                    player.sendMessage(new StringTextComponent("An imperial patrol is moving through the area...").withStyle(TextFormatting.DARK_GRAY), player.getUUID());
                    break;
                }
            }
        }
    }
}

