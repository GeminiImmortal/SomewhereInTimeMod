package net.geminiimmortal.mobius.spawn;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.*;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.world.gen.Heightmap;

public class ModSpawns {
    public static void setupSpawns() {
            EntitySpawnPlacementRegistry.register(
                    ModEntityTypes.FAECOW.get(),
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    FaecowEntity::canMobSpawn
            );
            EntitySpawnPlacementRegistry.register(
                    ModEntityTypes.GIANT.get(),
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    GiantEntity::canMobSpawn
            );
            EntitySpawnPlacementRegistry.register(
                    ModEntityTypes.ANGLERFISH.get(),
                    EntitySpawnPlacementRegistry.PlacementType.IN_WATER,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    AnglerfishEntity::canMobSpawn
            );
            EntitySpawnPlacementRegistry.register(
                    ModEntityTypes.FAEDEER.get(),
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    FaedeerEntity::canMobSpawn
            );
            EntitySpawnPlacementRegistry.register(
                    ModEntityTypes.BONE_WOLF.get(),
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    BoneWolfEntity::canMobSpawn
            );
            EntitySpawnPlacementRegistry.register(
                    ModEntityTypes.FUYUKAZE.get(),
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    FuyukazeEntity::canMobSpawn
            );
            EntitySpawnPlacementRegistry.register(
                    ModEntityTypes.FOOTMAN.get(),
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    FootmanEntity::canMobSpawn
            );
            EntitySpawnPlacementRegistry.register(
                    ModEntityTypes.IMPERIAL_GUARD.get(),
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    ImperialGuardEntity::canMobSpawn
            );
            EntitySpawnPlacementRegistry.register(
                    ModEntityTypes.BOUNTY_HUNTER.get(),
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    BountyHunterEntity::canMobSpawn
            );
            EntitySpawnPlacementRegistry.register(
                    ModEntityTypes.IMPERIAL_TOWER_GUARD.get(),
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    ImperialTowerRegularEntity::canMobSpawn
            );
            EntitySpawnPlacementRegistry.register(
                    ModEntityTypes.JACKALOPE.get(),
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    JackalopeEntity::canMobSpawn
            );
            EntitySpawnPlacementRegistry.register(
                    ModEntityTypes.INFERNAL_BRIAR.get(),
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    InfernalBriarEntity::canMobSpawn
            );
    }
}
