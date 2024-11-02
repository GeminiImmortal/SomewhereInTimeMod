package net.geminiimmortal.mobius.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.passive.IronGolemEntity;
import net.geminiimmortal.mobius.entity.custom.HeartGolemEntity;
import net.minecraft.util.math.MathHelper;

public class HeartGolem<S extends IronGolemEntity> extends EntityModel<HeartGolemEntity> {
    private final ModelRenderer book;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer satchel;
    private final ModelRenderer hat;
    private final ModelRenderer arm_left;
    private final ModelRenderer cube_r3;
    private final ModelRenderer head;
    private final ModelRenderer arm_right;
    private final ModelRenderer cube_r4;
    private final ModelRenderer leg_left;
    private final ModelRenderer leg_right;
    private final ModelRenderer Body;

    public HeartGolem() {
        texWidth = 128;
        texHeight = 128;

        book = new ModelRenderer(this);
        book.setPos(-9.75F, -15.0F, 1.5F);
        setRotationAngle(book, -0.6981F, 0.0F, 0.0F);
        book.texOffs(0, 53).addBox(-3.0F, 19.0F, -13.0F, 7.0F, 1.0F, 8.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(6.0F, 18.0F, -12.0F);
        book.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, -0.48F);
        cube_r1.texOffs(22, 63).addBox(-4.0F, -1.0F, -1.0F, 5.0F, 1.0F, 8.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(-3.0F, 19.0F, -12.0F);
        book.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, 0.48F);
        cube_r2.texOffs(22, 63).addBox(-4.0F, -1.0F, -1.0F, 5.0F, 1.0F, 8.0F, 0.0F, false);

        satchel = new ModelRenderer(this);
        satchel.setPos(-9.75F, -15.0F, 1.5F);
        satchel.texOffs(56, 81).addBox(3.0F, 2.0F, -5.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        satchel.texOffs(56, 81).addBox(5.0F, 4.0F, -5.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        satchel.texOffs(56, 81).addBox(7.0F, 6.0F, -5.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        satchel.texOffs(56, 81).addBox(9.0F, 8.0F, -5.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        satchel.texOffs(56, 81).addBox(11.0F, 10.0F, -4.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        satchel.texOffs(56, 81).addBox(11.0F, 10.0F, 3.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        satchel.texOffs(56, 81).addBox(9.0F, 8.0F, 3.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        satchel.texOffs(56, 81).addBox(7.0F, 6.0F, 3.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        satchel.texOffs(56, 81).addBox(5.0F, 4.0F, 3.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        satchel.texOffs(56, 81).addBox(3.0F, 2.0F, 3.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        satchel.texOffs(48, 66).addBox(3.0F, 1.0F, -5.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        satchel.texOffs(56, 81).addBox(13.0F, 12.0F, -3.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        satchel.texOffs(56, 81).addBox(13.0F, 12.0F, 2.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        satchel.texOffs(22, 72).addBox(15.0F, 14.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);

        hat = new ModelRenderer(this);
        hat.setPos(8.9167F, -12.5833F, 1.5833F);
        hat.texOffs(0, 16).addBox(-14.0F, -13.0F, -5.0F, 10.0F, 3.0F, 10.0F, 0.0F, false);
        hat.texOffs(62, 40).addBox(-13.0F, -16.0F, -3.0F, 7.0F, 3.0F, 7.0F, 0.0F, false);
        hat.texOffs(70, 0).addBox(-13.0F, -19.0F, 0.0F, 4.0F, 3.0F, 6.0F, 0.0F, false);
        hat.texOffs(72, 34).addBox(-14.0F, -22.0F, 3.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);

        arm_left = new ModelRenderer(this);
        arm_left.setPos(8.9167F, -12.5833F, 1.5833F);
        setRotationAngle(arm_left, 0.2618F, 0.0F, 0.0F);
        arm_left.texOffs(56, 50).addBox(-2.6667F, -0.4167F, -3.0833F, 5.0F, 10.0F, 6.0F, 0.0F, false);
        arm_left.texOffs(0, 40).addBox(-3.0F, -5.0F, -4.0F, 7.0F, 5.0F, 8.0F, 0.0F, false);
        arm_left.texOffs(0, 29).addBox(-3.0F, -6.0F, -5.0F, 8.0F, 1.0F, 10.0F, 0.0F, false);
        arm_left.texOffs(56, 76).addBox(3.0F, -9.0F, -5.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);
        arm_left.texOffs(56, 76).addBox(3.0F, -9.0F, -1.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);
        arm_left.texOffs(56, 76).addBox(3.0F, -9.0F, 3.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);
        arm_left.texOffs(68, 79).addBox(7.0F, -8.0F, -5.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        arm_left.texOffs(68, 79).addBox(7.0F, -8.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        arm_left.texOffs(68, 79).addBox(7.0F, -8.0F, 3.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(-0.6667F, 9.5833F, 1.9167F);
        arm_left.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.829F, 0.0F, 0.0F);
        cube_r3.texOffs(56, 50).addBox(-2.0F, 2.0F, -3.0F, 5.0F, 10.0F, 6.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(-0.25F, -13.8571F, 1.75F);
        head.texOffs(40, 25).addBox(-2.5F, -0.8929F, -1.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);
        head.texOffs(30, 50).addBox(-4.0F, -8.3929F, -2.5F, 8.0F, 8.0F, 5.0F, 0.0F, false);
        head.texOffs(36, 79).addBox(-1.0F, -5.1429F, -4.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        arm_right = new ModelRenderer(this);
        arm_right.setPos(-9.0833F, -12.5833F, 1.5833F);
        setRotationAngle(arm_right, -0.6109F, 0.0F, 0.0F);
        arm_right.texOffs(0, 62).addBox(-2.6667F, -0.4167F, -3.0833F, 5.0F, 10.0F, 6.0F, 0.0F, false);
        arm_right.texOffs(36, 29).addBox(-5.0F, -6.0F, -5.0F, 8.0F, 1.0F, 10.0F, 0.0F, false);
        arm_right.texOffs(0, 78).addBox(-7.0F, -9.0F, -5.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);
        arm_right.texOffs(0, 78).addBox(-7.0F, -9.0F, -1.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);
        arm_right.texOffs(0, 78).addBox(-7.0F, -9.0F, 3.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);
        arm_right.texOffs(68, 79).addBox(-9.0F, -8.0F, 3.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        arm_right.texOffs(68, 79).addBox(-9.0F, -8.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        arm_right.texOffs(68, 79).addBox(-9.0F, -8.0F, -5.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        arm_right.texOffs(40, 12).addBox(-4.0F, -5.0F, -4.0F, 7.0F, 5.0F, 8.0F, 0.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-0.6667F, 9.5833F, 1.9167F);
        arm_right.addChild(cube_r4);
        setRotationAngle(cube_r4, -0.829F, 0.0F, 0.0F);
        cube_r4.texOffs(56, 50).addBox(-2.0F, 2.0F, -3.0F, 5.0F, 10.0F, 6.0F, 0.0F, false);

        leg_left = new ModelRenderer(this);
        leg_left.setPos(2.9167F, 3.875F, 0.0833F);
        leg_left.texOffs(54, 25).addBox(-1.4167F, 18.125F, -2.3333F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        leg_left.texOffs(70, 22).addBox(-1.4167F, 18.125F, -0.8333F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        leg_left.texOffs(76, 79).addBox(-1.1667F, 16.625F, -3.8333F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        leg_left.texOffs(78, 50).addBox(-1.1667F, 13.875F, -0.0833F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        leg_left.texOffs(70, 66).addBox(-1.4167F, 5.125F, -0.8333F, 3.0F, 9.0F, 4.0F, 0.0F, false);
        leg_left.texOffs(36, 72).addBox(-1.4167F, 0.125F, -0.0833F, 3.0F, 5.0F, 2.0F, 0.0F, false);

        leg_right = new ModelRenderer(this);
        leg_right.setPos(-3.0833F, 3.875F, 0.0833F);
        leg_right.texOffs(12, 78).addBox(-1.4167F, 18.125F, -2.3333F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        leg_right.texOffs(72, 28).addBox(-1.4167F, 18.125F, -0.8333F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        leg_right.texOffs(78, 62).addBox(-1.1667F, 16.625F, -3.8333F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        leg_right.texOffs(78, 56).addBox(-1.1667F, 13.875F, -0.0833F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        leg_right.texOffs(70, 9).addBox(-1.4167F, 5.125F, -0.8333F, 3.0F, 9.0F, 4.0F, 0.0F, false);
        leg_right.texOffs(46, 76).addBox(-1.4167F, 0.125F, -0.0833F, 3.0F, 5.0F, 2.0F, 0.0F, false);

        Body = new ModelRenderer(this);
        Body.setPos(0.0F, 24.0F, 0.0F);
        Body.texOffs(38, 0).addBox(-5.75F, -26.5F, -0.75F, 11.0F, 7.0F, 5.0F, 0.0F, false);
        Body.texOffs(0, 0).addBox(-7.25F, -37.25F, -0.75F, 14.0F, 11.0F, 5.0F, 0.0F, false);
        Body.texOffs(30, 40).addBox(-7.25F, -36.5F, -2.25F, 14.0F, 8.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(HeartGolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        // Reset all parts to default position
/*        arm_right.xRot = 0;
        arm_right.yRot = 0;
        arm_right.zRot = 0;

        arm_left.xRot = 0;
        arm_left.yRot = 0;
        arm_left.zRot = 0;

        head.xRot = 0;
        head.yRot = 0;
        head.zRot = 0;

*/
        this.leg_right.xRot = MathHelper.cos(limbSwing) * 1.0F * limbSwingAmount;
        this.leg_left.xRot = MathHelper.cos(limbSwing + (float) Math.PI) * 1.0F * limbSwingAmount;
        // Apply the healing animation - raise the right arm
        //animationHealing(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        HeartGolemEntity golem = entity;



        animationHealing(golem, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);



    }
        // Head follows the player's look direction
    //    head.yRot = netHeadYaw * ((float) Math.PI / 180F);
    //    head.xRot = headPitch * ((float) Math.PI / 180F);



    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        book.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        satchel.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        hat.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
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

    public void animationHealing(HeartGolemEntity golem, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (golem.isHealing()) {
            arm_right.xRot = -1.0F + MathHelper.cos(ageInTicks * 0.2F) * 0.1F; // Lift and pulse effect
            arm_left.xRot = -0.8F + MathHelper.cos((float) (ageInTicks * 0.2F + Math.PI)) * 0.1F; // Slight forward motion
            arm_right.zRot = 0.1F * MathHelper.sin(ageInTicks * 0.1F); // Small rotational pulse

        } else {
            // Return to normal idle animation if not healing
            //    arm_right.xRot = (float) Math.toRadians(-10); // slight idle bend for right arm
            //    arm_left.xRot = (float) Math.toRadians(-10); // slight idle bend for left arm
            //this.arm_right.xRot = -0.6109F;
            arm_right.xRot = MathHelper.cos(ageInTicks * 0.1F) * 0.1F - 0.5F; // Resting position
            arm_left.xRot = MathHelper.cos((float) (ageInTicks * 0.1F + Math.PI)) * 0.1F - 0.5F;

        }
    }
}

