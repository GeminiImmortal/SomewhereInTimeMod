package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.geminiimmortal.mobius.entity.custom.RebelInstigatorEntity;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
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

    private static final int SKIRMISH_INTERVAL_TICKS = 72000; // 1 hour of gameplay time
    private static final int SPAWN_RADIUS = 45;
    private static final Map<UUID, Integer> tickCounters = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        UUID playerId = player.getUUID();
        if (!player.level.dimension().equals(ModDimensions.MOBIUS_WORLD)) return;

        int ticks = tickCounters.getOrDefault(playerId, 0) + 1;
        if (ticks < SKIRMISH_INTERVAL_TICKS) {
            tickCounters.put(playerId, ticks);
            return;
        }

        tickCounters.put(playerId, 0);
        handleSkirmish(player);
    }

    public static void handleSkirmish(ServerPlayerEntity player) {
        ServerWorld world = player.getLevel();
        BlockPos origin = player.blockPosition();
        Random rand = world.random;

        for (int attempts = 0; attempts < 50; attempts++) {
            int x = origin.getX() + rand.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;
            int z = origin.getZ() + rand.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;
            int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos pos = new BlockPos(x, y, z);

            if (world.canSeeSky(pos) && world.getBlockState(pos.below()).isSolidRender(world, pos.below())) {


                for (int i = 0; i < 5; i++) {
                    FootmanEntity soldier = ModEntityTypes.FOOTMAN.get().create(world);
                    if (soldier != null) {
                        soldier.setIsPartOfPatrol(true);
                        soldier.moveTo(pos, rand.nextFloat() * 360.0F, 0);
                        world.addFreshEntity(soldier);
                    }
                }

                for (int i = 0; i < 8; i++) {
                    RebelInstigatorEntity rebel = ModEntityTypes.REBEL_INSTIGATOR.get().create(world);
                    if (rebel != null) {
                        rebel.setIsPartOfSkirmish(true);
                        rebel.moveTo(pos, rand.nextFloat() * 360.0F, 0);
                        world.addFreshEntity(rebel);
                        rebel.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 3));
                        rebel.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 2));
                        rebel.addEffect(new EffectInstance(Effects.REGENERATION, 2));
                    }
                }

                player.sendMessage(new StringTextComponent("A skirmish has broken out nearby!").withStyle(TextFormatting.GOLD), player.getUUID());
                return;
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

        return null;
    }

    public static void forceSpawnFor(ServerPlayerEntity player) {
        tickCounters.put(player.getUUID(), SKIRMISH_INTERVAL_TICKS);
    }
}


