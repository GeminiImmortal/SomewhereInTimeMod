package net.geminiimmortal.mobius.event;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.BoneWolfEntity;
import net.geminiimmortal.mobius.entity.custom.GiantEntity;
import net.geminiimmortal.mobius.entity.custom.InfernalBriarEntity;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID)
public class ModWorldEvents {

    @SubscribeEvent
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        IWorld world = event.getWorld();
        if (!(world instanceof ServerWorld)) return;
        ServerWorld serverWorld = (ServerWorld) world;

        if (serverWorld.dimension() == ModDimensions.MOBIUS_WORLD) {
            if (world.getLevelData() instanceof DerivedWorldInfo) {
                DerivedWorldInfo dwi = (DerivedWorldInfo) world.getLevelData();

                dwi.wrapped.setDayTime(1000L);
                dwi.wrapped.setRaining(false);
                dwi.wrapped.setThundering(false);

                serverWorld.setWeatherParameters(0, 0, false, false);
                serverWorld.getChunkSource().chunkMap.tick(() -> {
                    serverWorld.getChunkSource().getLightEngine().checkBlock(BlockPos.ZERO);
                    return false;
                });

                serverWorld.getEntities().filter(e -> e instanceof BoneWolfEntity).forEach(Entity::remove);
                serverWorld.getEntities().filter(e -> e instanceof GiantEntity).forEach(Entity::remove);
                serverWorld.getEntities().filter(e -> e instanceof InfernalBriarEntity).forEach(Entity::remove);
            }
        }
    }
}


