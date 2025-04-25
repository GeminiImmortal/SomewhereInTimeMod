package net.geminiimmortal.mobius.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.geminiimmortal.mobius.entity.custom.BeamEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;

public class BeamRenderer extends EntityRenderer<BeamEntity> {
    public BeamRenderer(EntityRendererManager context) {
        super(context);
    }

    @Override
    public void render(BeamEntity entity, float entityYaw, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer buffer, int packedLight) {
        // You can render a cube, line, or glowing texture here
        // For now, let’s just spawn particles visually (redundant but ensures visibility)
        Minecraft.getInstance().level.addParticle(ParticleTypes.CRIT, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
    }

    @Override
    public ResourceLocation getTextureLocation(BeamEntity entity) {
        return new ResourceLocation("minecraft", "textures/entity/end_crystal/end_crystal.png"); // Placeholder
    }
}

