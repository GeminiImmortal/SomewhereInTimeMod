package net.geminiimmortal.mobius.entity.model;

// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.entity.custom.SpadeGolemEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.math.MathHelper;

public class SpadeGolem<S extends IronGolemEntity> extends EntityModel<SpadeGolemEntity> {
    private final ModelRenderer arm_left;
    private final ModelRenderer cube_r1;
    private final ModelRenderer crossbow_left;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer head;
    private final ModelRenderer mask;
    private final ModelRenderer arm_right;
    private final ModelRenderer cube_r7;
    private final ModelRenderer crossbow_right;
    private final ModelRenderer cube_r8;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer cube_r11;
    private final ModelRenderer cube_r12;
    private final ModelRenderer leg_left;
    private final ModelRenderer leg_right;
    private final ModelRenderer Body;
    private final ModelRenderer quiver;

    public SpadeGolem() {
        texWidth = 128;
        texHeight = 128;

        arm_left = new ModelRenderer(this);
        arm_left.setPos(8.9167F, -12.5833F, 1.5833F);
        setRotationAngle(arm_left, 0.2618F, 0.0F, 0.0F);
        arm_left.texOffs(0, 51).addBox(-2.6667F, -0.4167F, -3.0833F, 5.0F, 10.0F, 6.0F, 0.0F, false);
        arm_left.texOffs(36, 28).addBox(-3.0F, -5.0F, -4.0F, 7.0F, 5.0F, 8.0F, 0.0F, false);
        arm_left.texOffs(0, 16).addBox(-3.0F, -6.0F, -5.0F, 8.0F, 1.0F, 10.0F, 0.0F, false);
        arm_left.texOffs(58, 60).addBox(0.0F, -9.0F, -5.0F, 7.0F, 3.0F, 2.0F, 0.0F, false);
        arm_left.texOffs(58, 60).addBox(0.0F, -9.0F, -1.0F, 7.0F, 3.0F, 2.0F, 0.0F, false);
        arm_left.texOffs(58, 60).addBox(0.0F, -9.0F, 3.0F, 7.0F, 3.0F, 2.0F, 0.0F, false);
        arm_left.texOffs(70, 47).addBox(7.0F, -11.0F, -5.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        arm_left.texOffs(70, 47).addBox(7.0F, -11.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        arm_left.texOffs(70, 47).addBox(7.0F, -11.0F, 3.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(-0.6667F, 9.5833F, 1.9167F);
        arm_left.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.829F, 0.0F, 0.0F);
        cube_r1.texOffs(0, 51).addBox(-2.0F, 2.0F, -3.0F, 5.0F, 10.0F, 6.0F, 0.0F, false);

        crossbow_left = new ModelRenderer(this);
        crossbow_left.setPos(-8.9167F, 35.5833F, -5.5833F);
        arm_left.addChild(crossbow_left);


        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(11.0F, -21.0F, -3.0F);
        crossbow_left.addChild(cube_r2);
        setRotationAngle(cube_r2, -2.2253F, 0.0F, 0.0F);
        cube_r2.texOffs(38, 70).addBox(0.0F, -7.0F, -1.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(11.0F, -15.0F, -1.0F);
        crossbow_left.addChild(cube_r3);
        setRotationAngle(cube_r3, -1.0472F, 0.0F, 0.0F);
        cube_r3.texOffs(0, 73).addBox(0.0F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(11.0F, -17.0F, -2.0F);
        crossbow_left.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.2182F, 0.0F, 0.0F);
        cube_r4.texOffs(0, 73).addBox(0.0F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(12.0F, -14.0F, -3.0F);
        crossbow_left.addChild(cube_r5);
        setRotationAngle(cube_r5, -0.6109F, 0.0F, 0.0F);
        cube_r5.texOffs(38, 70).addBox(0.0F, -7.0F, -1.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setPos(11.0F, -16.0F, -2.0F);
        crossbow_left.addChild(cube_r6);
        setRotationAngle(cube_r6, -0.6109F, 0.0F, 0.0F);
        cube_r6.texOffs(14, 67).addBox(0.0F, -10.0F, -1.0F, 1.0F, 10.0F, 2.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(-0.25F, -13.8571F, 1.75F);
        head.texOffs(66, 37).addBox(-2.5F, -0.8929F, -1.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);
        head.texOffs(30, 41).addBox(-4.0F, -8.3929F, -2.5F, 8.0F, 8.0F, 5.0F, 0.0F, false);

        mask = new ModelRenderer(this);
        mask.setPos(0.25F, 37.8571F, -1.75F);
        head.addChild(mask);
        mask.texOffs(44, 54).addBox(-5.0F, -43.0F, -2.0F, 10.0F, 5.0F, 1.0F, 0.0F, false);
        mask.texOffs(44, 54).addBox(-5.0F, -43.0F, 4.0F, 10.0F, 5.0F, 1.0F, 0.0F, false);
        mask.texOffs(58, 65).addBox(4.0F, -43.0F, -1.0F, 1.0F, 5.0F, 5.0F, 0.0F, false);
        mask.texOffs(58, 65).addBox(-5.0F, -43.0F, -1.0F, 1.0F, 5.0F, 5.0F, 0.0F, false);

        arm_right = new ModelRenderer(this);
        arm_right.setPos(-9.0833F, -12.5833F, 1.5833F);
        setRotationAngle(arm_right, 0.2182F, 0.0F, 0.0F);
        arm_right.texOffs(22, 54).addBox(-2.6667F, -0.4167F, -3.0833F, 5.0F, 10.0F, 6.0F, 0.0F, false);
        arm_right.texOffs(0, 27).addBox(-5.0F, -6.0F, -5.0F, 8.0F, 1.0F, 10.0F, 0.0F, false);
        arm_right.texOffs(64, 10).addBox(-7.0F, -9.0F, -5.0F, 7.0F, 3.0F, 2.0F, 0.0F, false);
        arm_right.texOffs(64, 10).addBox(-7.0F, -9.0F, -1.0F, 7.0F, 3.0F, 2.0F, 0.0F, false);
        arm_right.texOffs(64, 10).addBox(-7.0F, -9.0F, 3.0F, 7.0F, 3.0F, 2.0F, 0.0F, false);
        arm_right.texOffs(70, 47).addBox(-9.0F, -11.0F, 3.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        arm_right.texOffs(70, 47).addBox(-9.0F, -11.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        arm_right.texOffs(70, 47).addBox(-9.0F, -11.0F, -5.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        arm_right.texOffs(0, 38).addBox(-4.0F, -5.0F, -4.0F, 7.0F, 5.0F, 8.0F, 0.0F, false);

        cube_r7 = new ModelRenderer(this);
        cube_r7.setPos(-0.6667F, 9.5833F, 1.9167F);
        arm_right.addChild(cube_r7);
        setRotationAngle(cube_r7, -0.829F, 0.0F, 0.0F);
        cube_r7.texOffs(0, 51).addBox(-2.0F, 2.0F, -3.0F, 5.0F, 10.0F, 6.0F, 0.0F, false);

        crossbow_right = new ModelRenderer(this);
        crossbow_right.setPos(-14.9167F, 34.5833F, -5.5833F);
        arm_right.addChild(crossbow_right);


        cube_r8 = new ModelRenderer(this);
        cube_r8.setPos(11.0F, -21.0F, -3.0F);
        crossbow_right.addChild(cube_r8);
        setRotationAngle(cube_r8, -2.2253F, 0.0F, 0.0F);
        cube_r8.texOffs(38, 70).addBox(0.0F, -7.0F, -1.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        cube_r9 = new ModelRenderer(this);
        cube_r9.setPos(11.0F, -15.0F, -1.0F);
        crossbow_right.addChild(cube_r9);
        setRotationAngle(cube_r9, -1.0472F, 0.0F, 0.0F);
        cube_r9.texOffs(0, 73).addBox(0.0F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        cube_r10 = new ModelRenderer(this);
        cube_r10.setPos(11.0F, -17.0F, -2.0F);
        crossbow_right.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.2182F, 0.0F, 0.0F);
        cube_r10.texOffs(0, 73).addBox(0.0F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        cube_r11 = new ModelRenderer(this);
        cube_r11.setPos(10.0F, -14.0F, -3.0F);
        crossbow_right.addChild(cube_r11);
        setRotationAngle(cube_r11, -0.6109F, 0.0F, 0.0F);
        cube_r11.texOffs(38, 70).addBox(0.0F, -7.0F, -1.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        cube_r12 = new ModelRenderer(this);
        cube_r12.setPos(11.0F, -16.0F, -2.0F);
        crossbow_right.addChild(cube_r12);
        setRotationAngle(cube_r12, -0.6109F, 0.0F, 0.0F);
        cube_r12.texOffs(14, 67).addBox(0.0F, -10.0F, -1.0F, 1.0F, 10.0F, 2.0F, 0.0F, false);

        leg_left = new ModelRenderer(this);
        leg_left.setPos(2.9167F, 3.875F, 0.0833F);
        leg_left.texOffs(68, 22).addBox(-1.4167F, 18.125F, -2.3333F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        leg_left.texOffs(66, 54).addBox(-1.4167F, 18.125F, -0.8333F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        leg_left.texOffs(70, 69).addBox(-1.1667F, 16.625F, -3.8333F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        leg_left.texOffs(30, 70).addBox(-1.1667F, 13.875F, -0.0833F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        leg_left.texOffs(44, 60).addBox(-1.4167F, 5.125F, -0.8333F, 3.0F, 9.0F, 4.0F, 0.0F, false);
        leg_left.texOffs(68, 15).addBox(-1.4167F, 0.125F, -0.0833F, 3.0F, 5.0F, 2.0F, 0.0F, false);

        leg_right = new ModelRenderer(this);
        leg_right.setPos(-3.0833F, 3.875F, 0.0833F);
        leg_right.texOffs(20, 70).addBox(-1.4167F, 18.125F, -2.3333F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        leg_right.texOffs(0, 67).addBox(-1.4167F, 18.125F, -0.8333F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        leg_right.texOffs(70, 65).addBox(-1.1667F, 16.625F, -3.8333F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        leg_right.texOffs(70, 41).addBox(-1.1667F, 13.875F, -0.0833F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        leg_right.texOffs(56, 41).addBox(-1.4167F, 5.125F, -0.8333F, 3.0F, 9.0F, 4.0F, 0.0F, false);
        leg_right.texOffs(70, 0).addBox(-1.4167F, 0.125F, -0.0833F, 3.0F, 5.0F, 2.0F, 0.0F, false);

        Body = new ModelRenderer(this);
        Body.setPos(0.0F, 24.0F, 0.0F);
        Body.texOffs(36, 16).addBox(-5.75F, -26.5F, -0.75F, 11.0F, 7.0F, 5.0F, 0.0F, false);
        Body.texOffs(0, 0).addBox(-7.25F, -37.25F, -0.75F, 14.0F, 11.0F, 5.0F, 0.0F, false);
        Body.texOffs(38, 0).addBox(-7.25F, -36.5F, -2.25F, 14.0F, 8.0F, 2.0F, 0.0F, false);

        quiver = new ModelRenderer(this);
        quiver.setPos(0.0F, 0.0F, 0.0F);
        Body.addChild(quiver);
        quiver.texOffs(38, 10).addBox(-6.0F, -25.0F, -2.0F, 12.0F, 4.0F, 1.0F, 0.0F, false);
        quiver.texOffs(38, 10).addBox(-6.0F, -25.0F, 4.0F, 12.0F, 4.0F, 1.0F, 0.0F, false);
        quiver.texOffs(66, 28).addBox(5.0F, -25.0F, -1.0F, 1.0F, 4.0F, 5.0F, 0.0F, false);
        quiver.texOffs(66, 28).addBox(-6.0F, -25.0F, -1.0F, 1.0F, 4.0F, 5.0F, 0.0F, false);
        quiver.texOffs(30, 38).addBox(4.0F, -26.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        quiver.texOffs(30, 38).addBox(-5.0F, -26.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        quiver.texOffs(30, 38).addBox(-3.0F, -26.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        quiver.texOffs(30, 38).addBox(2.0F, -26.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        quiver.texOffs(30, 38).addBox(0.0F, -26.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(SpadeGolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.leg_right.xRot = MathHelper.cos(limbSwing) * 1.0F * limbSwingAmount;
        this.leg_left.xRot = MathHelper.cos(limbSwing + (float) Math.PI) * 1.0F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        arm_left.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        arm_right.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg_left.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg_right.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        Body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
