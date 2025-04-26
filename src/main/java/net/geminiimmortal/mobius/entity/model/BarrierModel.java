package net.geminiimmortal.mobius.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.entity.custom.BarrierEntity;
import net.geminiimmortal.mobius.entity.render.BarrierEntityRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class BarrierModel<T extends BarrierEntity> extends EntityModel<T> {

    private final ModelRenderer wall;

    public BarrierModel() {
        this.texWidth = 128;
        this.texHeight = 128;

        this.wall = new ModelRenderer(this);
        this.wall.setPos(0.0F, 0.0F, 0.0F);
        this.wall.texOffs(0, 0).addBox(-8.0F, 0.0F, -1.0F, 16, 16, 2); // (Width 16, Height 16, Depth 2)
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // No animation needed
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        wall.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}



