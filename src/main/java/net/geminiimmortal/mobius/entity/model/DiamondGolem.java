package net.geminiimmortal.mobius.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.entity.custom.DiamondGolemEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.math.MathHelper;

import static net.minecraft.util.math.MathHelper.lerp;

public class DiamondGolem<S extends IronGolemEntity> extends EntityModel<DiamondGolemEntity> {
    private final ModelRenderer shield;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer arm_left;
    private final ModelRenderer cube_r3;
    private final ModelRenderer head;
    private final ModelRenderer arm_right;
    private final ModelRenderer cube_r4;
    private final ModelRenderer leg_left;
    private final ModelRenderer leg_right;
    private final ModelRenderer Body;
    private final ModelRenderer helmet;

    public DiamondGolem() {
        texWidth = 128;
        texHeight = 128;

        shield = new ModelRenderer(this);
        shield.setPos(8.9167F, -12.5833F, -0.4167F);
        shield.texOffs(0, 0).addBox(-22.0F, -8.0F, -15.0F, 16.0F, 30.0F, 2.0F, 0.0F, false);
        shield.texOffs(36, 23).addBox(-22.0F, 22.0F, -15.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);
        shield.texOffs(36, 23).addBox(-22.0F, -10.0F, -15.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);
        shield.texOffs(36, 23).addBox(-11.0F, -10.0F, -15.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);
        shield.texOffs(36, 23).addBox(-9.0F, -12.0F, -15.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);
        shield.texOffs(36, 23).addBox(-24.0F, -12.0F, -15.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);
        shield.texOffs(0, 44).addBox(-24.0F, 4.0F, -15.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        shield.texOffs(0, 44).addBox(-6.0F, 4.0F, -15.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        shield.texOffs(36, 23).addBox(-24.0F, 24.0F, -15.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(-5.0F, 26.0F, -14.0F);
        shield.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0873F, 0.0F, 0.0F);
        cube_r1.texOffs(36, 23).addBox(-4.0F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(-7.0F, 24.0F, -14.0F);
        shield.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0873F, 0.0F, 0.0F);
        cube_r2.texOffs(36, 23).addBox(-4.0F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);

        arm_left = new ModelRenderer(this);
        arm_left.setPos(8.9167F, -12.5833F, 1.5833F);
        setRotationAngle(arm_left, 0.2618F, 0.0F, 0.0F);
        arm_left.texOffs(22, 69).addBox(-2.6667F, -0.4167F, -3.0833F, 5.0F, 10.0F, 6.0F, 0.0F, false);
        arm_left.texOffs(48, 48).addBox(-3.0F, -5.0F, -4.0F, 7.0F, 5.0F, 8.0F, 0.0F, false);
        arm_left.texOffs(36, 0).addBox(-3.0F, -6.0F, -5.0F, 8.0F, 1.0F, 10.0F, 0.0F, false);
        arm_left.texOffs(0, 46).addBox(2.0F, -9.0F, -5.0F, 5.0F, 3.0F, 10.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(-0.6667F, 9.5833F, 1.9167F);
        arm_left.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.829F, 0.0F, 0.0F);
        cube_r3.texOffs(22, 69).addBox(-2.0F, 2.0F, -3.0F, 5.0F, 10.0F, 6.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(-0.25F, -13.8571F, 1.75F);
        head.texOffs(46, 43).addBox(-2.5F, -0.8929F, -1.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);
        head.texOffs(64, 18).addBox(-4.0F, -8.3929F, -2.5F, 8.0F, 8.0F, 5.0F, 0.0F, false);
        head.texOffs(50, 43).addBox(-1.0F, -5.1429F, -4.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);


        arm_right = new ModelRenderer(this);
        arm_right.setPos(-9.0833F, -12.5833F, 1.5833F);
        setRotationAngle(arm_right, -1.5708F, 0.0F, 0.0F);
        arm_right.texOffs(69, 31).addBox(-2.6667F, -0.4167F, -3.0833F, 5.0F, 10.0F, 6.0F, 0.0F, false);
        arm_right.texOffs(0, 33).addBox(-5.0F, -6.0F, -5.0F, 8.0F, 1.0F, 10.0F, 0.0F, false);
        arm_right.texOffs(26, 43).addBox(-8.0F, -9.0F, -5.0F, 5.0F, 3.0F, 10.0F, 0.0F, false);
        arm_right.texOffs(22, 56).addBox(-4.0F, -5.0F, -4.0F, 7.0F, 5.0F, 8.0F, 0.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-0.6667F, 12.5833F, -0.0833F);
        arm_right.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -1.5708F);
        cube_r4.texOffs(0, 59).addBox(-2.0F, 0.0F, -3.0F, 5.0F, 12.0F, 6.0F, 0.0F, false);

        leg_left = new ModelRenderer(this);
        leg_left.setPos(2.9167F, 3.875F, 0.0833F);
        setRotationAngle(leg_left, 0.3054F, 0.0F, 0.0F);
        leg_left.texOffs(50, 23).addBox(-1.4167F, 18.125F, -2.3333F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        leg_left.texOffs(80, 77).addBox(-1.4167F, 18.125F, -0.8333F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        leg_left.texOffs(70, 47).addBox(-1.1667F, 16.625F, -3.8333F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        leg_left.texOffs(0, 50).addBox(-1.1667F, 13.875F, -0.0833F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        leg_left.texOffs(0, 77).addBox(-1.4167F, 5.125F, -0.8333F, 3.0F, 9.0F, 4.0F, 0.0F, false);
        leg_left.texOffs(36, 0).addBox(-1.4167F, 0.125F, -0.0833F, 3.0F, 5.0F, 2.0F, 0.0F, false);

        leg_right = new ModelRenderer(this);
        leg_right.setPos(-3.0833F, 3.875F, 0.0833F);
        setRotationAngle(leg_right, -0.0873F, 0.0F, 0.0F);
        leg_right.texOffs(0, 39).addBox(-1.4167F, 18.125F, -2.3333F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        leg_right.texOffs(74, 71).addBox(-1.4167F, 18.125F, -0.8333F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        leg_right.texOffs(70, 51).addBox(-1.1667F, 16.625F, -3.8333F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        leg_right.texOffs(46, 47).addBox(-1.1667F, 13.875F, -0.0833F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        leg_right.texOffs(78, 47).addBox(-1.4167F, 5.125F, -0.8333F, 3.0F, 9.0F, 4.0F, 0.0F, false);
        leg_right.texOffs(0, 32).addBox(-1.4167F, 0.125F, -0.0833F, 3.0F, 5.0F, 2.0F, 0.0F, false);

        Body = new ModelRenderer(this);
        Body.setPos(0.0F, 24.0F, 0.0F);
        Body.texOffs(36, 11).addBox(-5.75F, -26.5F, -0.75F, 11.0F, 7.0F, 5.0F, 0.0F, false);
        Body.texOffs(31, 27).addBox(-7.25F, -37.25F, -0.75F, 14.0F, 11.0F, 5.0F, 0.0F, false);
        Body.texOffs(52, 61).addBox(-7.25F, -36.5F, -2.25F, 14.0F, 8.0F, 2.0F, 0.0F, false);

        helmet = new ModelRenderer(this);
        helmet.setPos(0.0F, 24.0F, 0.0F);
        helmet.texOffs(62, 0).addBox(-5.0F, -48.0F, -1.0F, 9.0F, 2.0F, 6.0F, 0.0F, false);
        helmet.texOffs(20, 51).addBox(-1.0F, -46.0F, -2.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        helmet.texOffs(68, 11).addBox(-4.0F, -43.0F, -2.0F, 8.0F, 5.0F, 1.0F, 0.0F, false);
        helmet.texOffs(66, 71).addBox(4.0F, -47.0F, -1.0F, 1.0F, 9.0F, 6.0F, 0.0F, false);
        helmet.texOffs(66, 71).addBox(-5.0F, -47.0F, -1.0F, 1.0F, 9.0F, 6.0F, 0.0F, false);
        helmet.texOffs(44, 71).addBox(-5.0F, -47.0F, 5.0F, 10.0F, 9.0F, 1.0F, 0.0F, false);
        helmet.texOffs(60, 43).addBox(-4.0F, -50.0F, 1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        helmet.texOffs(16, 59).addBox(-6.0F, -50.0F, -4.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        helmet.texOffs(16, 59).addBox(4.0F, -50.0F, -5.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        helmet.texOffs(60, 43).addBox(2.0F, -50.0F, 1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        helmet.texOffs(20, 44).addBox(2.0F, -52.0F, -3.0F, 3.0F, 2.0F, 5.0F, 0.0F, false);
        helmet.texOffs(20, 44).addBox(-5.0F, -52.0F, -3.0F, 3.0F, 2.0F, 5.0F, 0.0F, false);
        helmet.texOffs(20, 44).addBox(-7.0F, -47.0F, -3.0F, 3.0F, 2.0F, 5.0F, 0.0F, false);
        helmet.texOffs(20, 44).addBox(4.0F, -47.0F, -3.0F, 3.0F, 2.0F, 5.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(DiamondGolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        /*helmet.xRot = head.xRot;
        helmet.yRot = head.yRot;
        helmet.zRot = head.zRot;*/

        float progress = entity.getAttackAnimationTick() / (float) entity.getAttackAnimationDuration();  // Assuming you have this timer logic in your entity
        this.leg_right.xRot = MathHelper.cos(limbSwing) * 1.0F * limbSwingAmount;
        this.leg_left.xRot = MathHelper.cos(limbSwing + (float) Math.PI) * 1.0F * limbSwingAmount;

        if (entity instanceof DiamondGolemEntity) {
            DiamondGolemEntity golem = (DiamondGolemEntity) entity;
            if (golem.getAttackAnimationTick() > 0) {
                // Control the movement range for the shield bash
                float bashAngleVertical = lerp(progress, 0.0F, (float) Math.PI / 4);  // Vertical bash (up-down)
                float bashAngleHorizontal = lerp(progress, 0.0F, (float) Math.PI / 6);  // Horizontal bash (side-to-side)

                arm_right.yRot = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
                shield.yRot = arm_right.yRot;  // Make shield follow arm rotation

                // Retract after the bash completes
                if (progress > 0.5F) {
                    float reverseProgress = (progress - 0.5F) * 2.0F;  // Normalize for the retract phase
                    arm_right.xRot = lerp(reverseProgress, -0.7F, 0.2618F);  // Retract arm vertically
                    shield.xRot = lerp(reverseProgress, -0.4F, 0.0F);       // Retract shield vertically
                    arm_right.yRot = lerp(reverseProgress, -bashAngleHorizontal, 0.0F);  // Retract arm horizontally
                    shield.yRot = arm_right.yRot;                             // Retract shield horizontally
                    // Adjust shield position for retraction as well
                }

                /*// Standard head rotation
                head.yRot = netHeadYaw * ((float) Math.PI / 180F);
                head.xRot = headPitch * ((float) Math.PI / 180F);
                */
                // Other limbs remain static
                arm_left.xRot = 0.2186F;
                leg_left.xRot = 0.3054F;
                leg_right.xRot = -0.0873F;
            }
        }
    }




    // Resets all parts to neutral position
/*    private void resetPose() {
        arm_left.xRot = 0.0F;
        shield.xRot = 0.0F;
        Body.xRot = 0.0F;
    }*/


    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        shield.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        arm_left.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        arm_right.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg_left.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg_right.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        Body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        helmet.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
