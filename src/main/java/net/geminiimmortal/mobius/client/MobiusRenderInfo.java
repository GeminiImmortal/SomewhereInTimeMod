package net.geminiimmortal.mobius.client;

import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.ISkyRenderHandler;

import javax.annotation.Nullable;

public class MobiusRenderInfo extends DimensionRenderInfo {
    public MobiusRenderInfo(float p_i241259_1_, boolean p_i241259_2_, FogType p_i241259_3_, boolean p_i241259_4_, boolean p_i241259_5_) {
        super(p_i241259_1_, p_i241259_2_, p_i241259_3_, p_i241259_4_, p_i241259_5_);
    }

    public Vector3d getBrightnessDependentFogColor(Vector3d p_230494_1_, float p_230494_2_) {
        return p_230494_1_.multiply((double)(p_230494_2_ * 0.94F + 0.06F), (double)(p_230494_2_ * 0.94F + 0.06F), (double)(p_230494_2_ * 0.91F + 0.09F));
    }

    @Nullable
    @Override
    public ISkyRenderHandler getSkyRenderHandler() {
        return new MobiusSkyRenderer();
    }

    @Override
    public boolean isFoggyAt(int p_230493_1_, int p_230493_2_) {
        return false;
    }
}
