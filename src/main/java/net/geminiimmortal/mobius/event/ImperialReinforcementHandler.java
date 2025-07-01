package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.AbstractImperialEntity;
import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.geminiimmortal.mobius.util.TitleUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID)
public class ImperialReinforcementHandler {

    private static final Map<UUID, Integer> leaderCooldowns = new HashMap<>();

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        leaderCooldowns.entrySet().removeIf(entry -> {
            int newValue = entry.getValue() - 1;
            if (newValue <= 0) {
                spawnReinforcements(entry.getKey());
                return true; // remove after spawning
            } else {
                entry.setValue(newValue);
                return false;
            }
        });
    }

    private static void spawnReinforcements(UUID leaderId) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        for (ServerWorld level : server.getAllLevels()) {
            AbstractImperialEntity leader = (AbstractImperialEntity) level.getEntity(leaderId);
            if (leader != null && leader.isAlive()) {
                BlockPos pos = leader.blockPosition();
                for (int i = 0; i < 3; i++) {
                    AbstractImperialEntity reinforcement = new FootmanEntity(ModEntityTypes.FOOTMAN.get(), level);
                    reinforcement.setIsPartOfPatrol(true);

                    double dx = pos.getX() + leader.getRandom().nextInt(5) - 2;
                    double dz = pos.getZ() + leader.getRandom().nextInt(5) - 2;
                    reinforcement.setPos(dx, pos.getY(), dz);
                    level.addFreshEntity(reinforcement);
                }
                List<ServerPlayerEntity> nearby = level.getEntitiesOfClass(ServerPlayerEntity.class,
                        new AxisAlignedBB(leader.blockPosition()).inflate(50), p -> !p.isSpectator());
                for (ServerPlayerEntity player : nearby) {
                    TitleUtils.sendTitle(player, null, "§cImperial reinforcements have arrived!", 10, 40, 10);
                    player.sendMessage(new StringTextComponent("Imperial reinforcements have arrived!").withStyle(TextFormatting.DARK_RED), player.getUUID());
                    player.getLevel().playSound(null, player.blockPosition(), SoundEvents.RAID_HORN, SoundCategory.HOSTILE, 50.0f, 1.05f);              }
            }
        }
    }

    public static void queueReinforcements(AbstractImperialEntity leader) {
        if (leader == null || !leader.isAlive()) return;
        UUID id = leader.getUUID();

        if (!leaderCooldowns.containsKey(id)) {
            leaderCooldowns.put(id, 600); // 30 seconds
            List<ServerPlayerEntity> nearby = leader.level.getEntitiesOfClass(ServerPlayerEntity.class,
                    new AxisAlignedBB(leader.blockPosition()).inflate(50), p -> !p.isSpectator());
            for (ServerPlayerEntity player : nearby) {
                player.sendMessage(new StringTextComponent("Reinforcements will arrive in 30 seconds!").withStyle(TextFormatting.YELLOW), player.getUUID());
            }
        }
    }
}

