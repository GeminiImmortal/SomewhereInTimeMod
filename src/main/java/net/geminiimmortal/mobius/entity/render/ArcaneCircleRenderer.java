package net.geminiimmortal.mobius.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.geminiimmortal.mobius.entity.custom.spell.ArcaneCircleEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ArcaneCircleRenderer extends EntityRenderer<ArcaneCircleEntity> {

    public ArcaneCircleRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(ArcaneCircleEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        // No model, only particles
    }

    @Override
    public ResourceLocation getTextureLocation(ArcaneCircleEntity entity) {
        return new ResourceLocation("mobius", "textures/entity/spell.png"); // Only needed if you later add a model
    }
}
