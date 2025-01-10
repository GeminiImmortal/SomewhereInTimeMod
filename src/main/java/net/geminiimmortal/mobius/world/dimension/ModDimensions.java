package net.geminiimmortal.mobius.world.dimension;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ModDimensions {
        public static final RegistryKey<DimensionType> MOBIUS_DIMENSION_TYPE = RegistryKey.create(Registry.DIMENSION_TYPE_REGISTRY, name("mobius_type"));
        public static final RegistryKey<World> MOBIUS_WORLD = RegistryKey.create(Registry.DIMENSION_REGISTRY, name("mobius"));

        public static ResourceLocation name(String name) {
                return new ResourceLocation(MobiusMod.MOD_ID + ":" + name);
        }
}



