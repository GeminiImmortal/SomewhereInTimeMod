package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.MarkerEntity;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;

public class MarkerRenderer extends EntityRenderer<MarkerEntity> {
    public MarkerRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public ResourceLocation getTextureLocation(MarkerEntity p_110775_1_) {
        return new ResourceLocation(MobiusMod.MOD_ID);
    }

    @Override
    public boolean shouldRender(MarkerEntity p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
        return false;
    }
}
