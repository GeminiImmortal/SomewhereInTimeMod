package net.geminiimmortal.mobius.item.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.item.custom.Dieselytra;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class DieselytraLayer<T extends LivingEntity, M extends BipedModel<T>> extends LayerRenderer<T, M> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/item/dieselytra.png");
    private final DieselytraModel<T> model = new DieselytraModel<>();

    public DieselytraLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight,
                       T entity, float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack chest = entity.getItemBySlot(net.minecraft.inventory.EquipmentSlotType.CHEST);

        if (chest.getItem() instanceof Dieselytra) {
            matrixStack.pushPose();

            // Position relative to player body
            this.getParentModel().body.translateAndRotate(matrixStack);
            matrixStack.translate(0.0D, 0.0D, 0.125D);

            // Get buffer with texture
            IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

            // Render the model
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLight,
                    OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

            matrixStack.popPose();
        }
    }
}


