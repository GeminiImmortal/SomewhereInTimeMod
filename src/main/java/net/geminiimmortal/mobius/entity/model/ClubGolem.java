package net.geminiimmortal.mobius.entity.model;// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.entity.custom.ClubGolemEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.math.MathHelper;

public class ClubGolem<S extends IronGolemEntity> extends EntityModel<ClubGolemEntity> {
	private final ModelRenderer head;
	private final ModelRenderer arm_left;
	private final ModelRenderer arm_right;
	private final ModelRenderer leg_left;
	private final ModelRenderer leg_right;
	private final ModelRenderer Body;

	public ClubGolem() {
		texWidth = 128;
		texHeight = 128;

		arm_left = new ModelRenderer(this);
		arm_left.setPos(8.9167F, -12.5833F, 1.5833F);
		arm_left.texOffs(0, 16).addBox(-2.6667F, -0.4167F, -3.0833F, 5.0F, 19.0F, 6.0F, 0.0F, false);
		arm_left.texOffs(32, 53).addBox(-2.9167F, 18.3333F, -3.8333F, 6.0F, 4.0F, 8.0F, 0.0F, false);
		arm_left.texOffs(44, 24).addBox(-3.0F, -5.0F, -4.0F, 7.0F, 5.0F, 8.0F, 0.0F, false);
		arm_left.texOffs(68, 65).addBox(-0.9167F, 20.5833F, -8.5833F, 1.0F, 1.0F, 5.0F, 0.0F, false);
		arm_left.texOffs(26, 62).addBox(1.0833F, 18.5833F, -5.5833F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		arm_left.texOffs(68, 78).addBox(2.0833F, 20.5833F, -6.5833F, 1.0F, 1.0F, 3.0F, 0.0F, false);
		arm_left.texOffs(74, 31).addBox(-1.9167F, 18.5833F, -7.5833F, 1.0F, 1.0F, 4.0F, 0.0F, false);
		arm_left.texOffs(74, 28).addBox(0.0F, -7.0F, -1.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);
		arm_left.texOffs(74, 7).addBox(2.0F, -9.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);
		arm_left.texOffs(38, 11).addBox(5.0F, -12.0F, -1.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
		arm_left.texOffs(38, 0).addBox(-3.0F, -6.0F, -5.0F, 8.0F, 1.0F, 10.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(-0.25F, -13.8571F, 1.75F);
		head.texOffs(44, 37).addBox(-2.5F, -0.8929F, -1.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);
		head.texOffs(0, 62).addBox(-4.0F, -8.3929F, -2.5F, 8.0F, 8.0F, 5.0F, 0.0F, false);
		head.texOffs(76, 71).addBox(-1.0F, -5.1429F, -4.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		head.texOffs(34, 78).addBox(1.25F, -9.8929F, -0.25F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		head.texOffs(42, 78).addBox(-3.25F, -9.8929F, -0.25F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		head.texOffs(74, 11).addBox(2.0F, -14.3929F, -0.25F, 2.0F, 5.0F, 2.0F, 0.0F, false);
		head.texOffs(74, 18).addBox(-4.0F, -14.3929F, -0.25F, 2.0F, 5.0F, 2.0F, 0.0F, false);

		arm_right = new ModelRenderer(this);
		arm_right.setPos(-9.0833F, -12.5833F, 1.5833F);
		arm_right.texOffs(22, 16).addBox(-2.6667F, -0.4167F, -3.0833F, 5.0F, 19.0F, 6.0F, 0.0F, false);
		arm_right.texOffs(60, 53).addBox(-2.9167F, 18.3333F, -3.8333F, 6.0F, 4.0F, 8.0F, 0.0F, false);
		arm_right.texOffs(74, 25).addBox(-4.0F, -7.0F, -1.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);
		arm_right.texOffs(0, 41).addBox(-5.0F, -6.0F, -5.0F, 8.0F, 1.0F, 10.0F, 0.0F, false);
		arm_right.texOffs(68, 49).addBox(-6.0F, -9.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);
		arm_right.texOffs(54, 77).addBox(-6.0F, -15.0F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);
		arm_right.texOffs(60, 78).addBox(-1.9167F, 19.5833F, -6.5833F, 1.0F, 1.0F, 3.0F, 0.0F, false);
		arm_right.texOffs(54, 71).addBox(-0.9167F, 20.5833F, -8.5833F, 1.0F, 1.0F, 5.0F, 0.0F, false);
		arm_right.texOffs(68, 43).addBox(1.0833F, 18.5833F, -8.5833F, 1.0F, 1.0F, 5.0F, 0.0F, false);
		arm_right.texOffs(44, 11).addBox(-4.0F, -5.0F, -4.0F, 7.0F, 5.0F, 8.0F, 0.0F, false);

		leg_left = new ModelRenderer(this);
		leg_left.setPos(2.9167F, 3.875F, 0.0833F);
		leg_left.texOffs(58, 37).addBox(-1.4167F, 18.125F, -2.3333F, 3.0F, 2.0F, 2.0F, 0.0F, false);
		leg_left.texOffs(54, 65).addBox(-1.4167F, 18.125F, -0.8333F, 3.0F, 2.0F, 4.0F, 0.0F, false);
		leg_left.texOffs(26, 78).addBox(-1.1667F, 16.625F, -3.8333F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		leg_left.texOffs(10, 75).addBox(-1.1667F, 13.875F, -0.0833F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		leg_left.texOffs(40, 65).addBox(-1.4167F, 5.125F, -0.8333F, 3.0F, 9.0F, 4.0F, 0.0F, false);
		leg_left.texOffs(66, 71).addBox(-1.4167F, 0.125F, -0.0833F, 3.0F, 5.0F, 2.0F, 0.0F, false);

		leg_right = new ModelRenderer(this);
		leg_right.setPos(-3.0833F, 3.875F, 0.0833F);
		leg_right.texOffs(0, 75).addBox(-1.4167F, 18.125F, -2.3333F, 3.0F, 2.0F, 2.0F, 0.0F, false);
		leg_right.texOffs(68, 37).addBox(-1.4167F, 18.125F, -0.8333F, 3.0F, 2.0F, 4.0F, 0.0F, false);
		leg_right.texOffs(76, 77).addBox(-1.1667F, 16.625F, -3.8333F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		leg_right.texOffs(18, 75).addBox(-1.1667F, 13.875F, -0.0833F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		leg_right.texOffs(26, 65).addBox(-1.4167F, 5.125F, -0.8333F, 3.0F, 9.0F, 4.0F, 0.0F, false);
		leg_right.texOffs(74, 0).addBox(-1.4167F, 0.125F, -0.0833F, 3.0F, 5.0F, 2.0F, 0.0F, false);

		Body = new ModelRenderer(this);
		Body.setPos(0.0F, 24.0F, 0.0F);
		Body.texOffs(36, 41).addBox(-5.75F, -26.5F, -0.75F, 11.0F, 7.0F, 5.0F, 0.0F, false);
		Body.texOffs(0, 0).addBox(-7.25F, -37.25F, -0.75F, 14.0F, 11.0F, 5.0F, 0.0F, false);
		Body.texOffs(0, 52).addBox(-7.25F, -36.5F, -2.25F, 14.0F, 8.0F, 2.0F, 0.0F, false);
	}


	@Override
	public void setupAnim(ClubGolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// Walk animation (legs swinging back and forth)
		this.leg_right.xRot = MathHelper.cos(limbSwing) * 1.0F * limbSwingAmount;
		this.leg_left.xRot = MathHelper.cos(limbSwing + (float) Math.PI) * 1.0F * limbSwingAmount;

		if (entity instanceof ClubGolemEntity) {
			ClubGolemEntity golem = (ClubGolemEntity) entity;

			// Current rotation state of the arms (used for lerping)
			float currentRightArmRotation = this.arm_right.xRot;
			float currentLeftArmRotation = this.arm_left.xRot;

			// Calculate progress of the attack animation (from 0.0 to 1.0)
			float progress = 1.0F - ((float) golem.getAttackAnimationTick() / golem.getAttackAnimationDuration());

			if (golem.getAttackAnimationTick() > 0) {
				float targetAngle = -(2.0F * (float) Math.PI / 3);  // -120 degrees (-2π/3 radians)

				// Lerp from current arm rotation to the attack position (higher than before)
				this.arm_right.xRot = MathHelper.lerp(0.2F, currentRightArmRotation, targetAngle);  // Lerp right arm
				this.arm_left.xRot = MathHelper.lerp(0.2F, currentLeftArmRotation, targetAngle);   // Lerp left arm
			} else {
				// Lerp from current arm rotation back to the resting position (0 degrees)
				this.arm_right.xRot = MathHelper.lerp(0.1F, currentRightArmRotation, 0.0F); // Lerp right arm back to rest
				this.arm_left.xRot = MathHelper.lerp(0.1F, currentLeftArmRotation, 0.0F);  // Lerp left arm back to rest
			}
		}
    }


	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		arm_left.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
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