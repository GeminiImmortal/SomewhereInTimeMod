package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.RebelInstigatorEntity;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
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
public class SkirmishEventHandler {

    private static final int SKIRMISH_INTERVAL_TICKS = 72000; // 1 hour of gameplay
    private static final int SKIRMISH_RADIUS = 70;

    private static final Map<UUID, Long> lastSkirmishTickMap = new HashMap<>();

    @SubscribeEvent
    public static void onServerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayerEntity) || !(event.player.level.dimension().equals(ModDimensions.MOBIUS_WORLD))) return;

        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        ServerWorld world = player.getLevel();
        UUID playerId = player.getUUID();

        long currentTick = world.getGameTime();
        long lastTick = lastSkirmishTickMap.getOrDefault(playerId, 0L);

        if (currentTick - lastTick < SKIRMISH_INTERVAL_TICKS) return;

        BlockPos spawnPos = findValidSpawnPosition(world, player.blockPosition(), SKIRMISH_RADIUS);
        if (spawnPos == null) return;

        // Spawn imperials and rebels
        for (int i = 0; i < 9; i++) {
            spawnEntity(world, ModEntityTypes.REBEL_INSTIGATOR.get(), spawnPos.offset(randomOffset()));
        }

        for (int j = 0; j < 3; j++) {
            spawnEntity(world, ModEntityTypes.IMPERIAL_GUARD.get(), spawnPos.offset(randomOffset()));
        }

        for (int j = 0; j < 2; j++) {
            spawnEntity(world, ModEntityTypes.IMPERIAL_REGULAR.get(), spawnPos.offset(randomOffset()));
        }

        lastSkirmishTickMap.put(playerId, currentTick);

        player.sendMessage(new StringTextComponent("⚔ A skirmish has broken out nearby!").withStyle(TextFormatting.YELLOW), player.getUUID());
    }

    private static BlockPos findValidSpawnPosition(ServerWorld world, BlockPos origin, int radius) {
        for (int attempts = 0; attempts < 50; attempts++) {
            int x = origin.getX() + world.getRandom().nextInt(radius * 2) - radius;
            int z = origin.getZ() + world.getRandom().nextInt(radius * 2) - radius;
            int y = world.getHeight(Heightmap.Type.WORLD_SURFACE, x, z);
            BlockPos pos = new BlockPos(x, y, z);

            // Check sky visibility and solid ground
            if (world.canSeeSky(pos) && world.getBlockState(pos.below()).isSolidRender(world, pos.below())) {
                return pos;
            }
        }
        return null;
    }

    private static void spawnEntity(ServerWorld world, EntityType<? extends MobEntity> type, BlockPos pos) {
        MobEntity entity = type.create(world);
        if (entity != null) {
            if (entity instanceof RebelInstigatorEntity) {
                ((RebelInstigatorEntity) entity).setIsPartOfSkirmish(true);
            }
            entity.moveTo(pos, world.random.nextFloat() * 360.0F, 0);
            world.addFreshEntity(entity);
        }
    }

    private static BlockPos randomOffset() {
        Random rand = new Random();
        int dx = rand.nextInt(10) - 5;
        int dz = rand.nextInt(10) - 5;
        return new BlockPos(dx, 0, dz);
    }
}

