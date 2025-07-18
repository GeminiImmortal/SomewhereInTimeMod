package net.geminiimmortal.mobius.world.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.geminiimmortal.mobius.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;

public class ModBloodstoneGemOreFeatureConfig implements IFeatureConfig {
    public static final Codec<OreFeatureConfig> CODEC = RecordCodecBuilder.create((p_236568_0_) -> {
        return p_236568_0_.group(RuleTest.CODEC.fieldOf("target").forGetter((p_236570_0_) -> {
            return p_236570_0_.target;
        }), BlockState.CODEC.fieldOf("state").forGetter((p_236569_0_) -> {
            return p_236569_0_.state;
        }), Codec.intRange(0, 64).fieldOf("size").forGetter((p_236567_0_) -> {
            return p_236567_0_.size;
        })).apply(p_236568_0_, OreFeatureConfig::new);
    });
    public final RuleTest target;
    public final int size;
    public final BlockState state;


    public ModBloodstoneGemOreFeatureConfig(RuleTest p_i241989_1_, BlockState p_i241989_2_, int p_i241989_3_) {
        super();
        this.size = p_i241989_3_;
        this.state = p_i241989_2_;
        this.target = p_i241989_1_;
    }





    public static final class FillerBlockType {
        public static final RuleTest IS_BLOODSTONE;

        static {
            IS_BLOODSTONE = new BlockMatchRuleTest(ModBlocks.BLOODSTONE.get());
        }
    }
}

