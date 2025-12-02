package net.geminiimmortal.mobius.world.worldgen.structure.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.ArrayList;
import java.util.List;

public class FlyingStructureConfig implements IFeatureConfig {

    public static final Codec<FlyingStructureConfig> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.INT.fieldOf("min_clearance").forGetter(cfg -> cfg.minClearance),
                    Codec.INT.fieldOf("max_attempts").forGetter(cfg -> cfg.maxAttempts),
                    Codec.INT.optionalFieldOf("avoid_radius", 64).forGetter(cfg -> cfg.avoidRadius)
            ).apply(instance, FlyingStructureConfig::new)
    );

    public final int minClearance;
    public final int maxAttempts;
    public final int avoidRadius;
    public List<ResourceLocation> avoidStructures = new ArrayList<>();

    public FlyingStructureConfig(int minClearance, int maxAttempts, int avoidRadius) {
        this.minClearance = minClearance;
        this.maxAttempts = maxAttempts;
        this.avoidRadius = avoidRadius;
        avoidStructures.add(new ResourceLocation(MobiusMod.MOD_ID, "grimcrow_flagship"));
    }
}


