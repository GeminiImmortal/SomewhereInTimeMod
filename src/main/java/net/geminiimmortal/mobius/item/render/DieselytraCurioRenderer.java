package net.geminiimmortal.mobius.item.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class DieselytraCurioRenderer {

    private static final DieselytraModel<LivingEntity> ELYTRA_MODEL = new DieselytraModel<>();
    public static final ResourceLocation TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/item/dieselytra.png");
    public static void renderElytra(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight,
                                    LivingEntity entity, ItemStack stack, float limbSwing,
                                    float limbSwingAmount, float partialTicks, float ageInTicks,
                                    float netHeadYaw, float headPitch) {
        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.0D, 0.125D);
        IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        ELYTRA_MODEL.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ELYTRA_MODEL.renderToBuffer(matrixStack, ivertexbuilder, packedLight,
                OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStack.popPose();
    }
}
