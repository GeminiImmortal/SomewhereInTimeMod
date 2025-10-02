package net.geminiimmortal.mobius.item.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.geminiimmortal.mobius.network.ParticlePacket;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.fml.network.PacketDistributor;

public class DieselytraModel<T extends LivingEntity> extends EntityModel<T> {
    private final ModelRenderer torso;
    private final ModelRenderer mechanical_wing_left;
    private final ModelRenderer mechanical_wing_right;
    private final ModelRenderer engine_left;
    private final ModelRenderer engine_right;

    public DieselytraModel() {
        texWidth = 64;
        texHeight = 64;

        torso = new ModelRenderer(this);
        torso.setPos(0F, 0F, 0F);

        // ----- Left wing -----
        mechanical_wing_left = new ModelRenderer(this);
        mechanical_wing_left.setPos(0.0F, 0.0F, 0.0F);
        torso.addChild(mechanical_wing_left);

        mechanical_wing_left.texOffs(7, 8).addBox(-3.5F, -6.0F, -14.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        mechanical_wing_left.texOffs(1, 6).addBox(-5.5F, -6.0F, -13.0F, 3.5F, 5.0F, 1.0F, 0.0F, false);
        mechanical_wing_left.texOffs(7, 0).addBox(-11.5F, -15.5F, -13.0F, 3.5F, 3.0F, 1.0F, 0.0F, false);
        mechanical_wing_left.texOffs(0, 0).addBox(-11.5F, -12.5F, -13.0F, 6.0F, 11.5F, 1.5F, 0.0F, false);


        mechanical_wing_left.texOffs(4, 1).addBox(-13.0F, -20.0F, -13.0F, 1.5F, 16.0F, 1.0F, 0.0F, false);
        mechanical_wing_left.texOffs(3, 5).addBox(-14.5F, -23.0F, -13.0F, 1.5F, 15.5F, 1.0F, 0.0F, false);
        mechanical_wing_left.texOffs(7, 4).addBox(-8.5F, -1.0F, -13.0F, 3.0F, 1.5F, 1.0F, 0.0F, false);

        // ----- Right wing -----
        mechanical_wing_right = new ModelRenderer(this);
        mechanical_wing_right.setPos(0.0F, 0.0F, 0.0F);
        torso.addChild(mechanical_wing_right);

        mechanical_wing_right.texOffs(8, 8).addBox(2.5F, -6.0F, -14.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        mechanical_wing_right.texOffs(6, 3).addBox(2.0F, -6.0F, -13.0F, 3.5F, 5.0F, 1.0F, 0.0F, false);
        mechanical_wing_right.texOffs(7, 6).addBox(8.0F, -15.5F, -13.0F, 3.5F, 3.0F, 1.0F, 0.0F, false);
        mechanical_wing_right.texOffs(0, 3).addBox(5.5F, -12.5F, -13.0F, 6.0F, 11.5F, 1.5F, 0.0F, false);

        mechanical_wing_right.texOffs(5, 1).addBox(11.5F, -20.0F, -13.0F, 1.5F, 16.0F, 1.0F, 0.0F, false);
        mechanical_wing_right.texOffs(4, 5).addBox(13.0F, -23.0F, -13.0F, 1.5F, 15.5F, 1.0F, 0.0F, false);
        mechanical_wing_right.texOffs(6, 7).addBox(5.5F, -1.0F, -13.0F, 3.0F, 1.5F, 1.0F, 0.0F, false);


        engine_left = new ModelRenderer(this);
        engine_left.setPos(0.5F - 8.0F, 20.5F - 24.0F, 13.75F - 24.0F); // pivot at cube center
        mechanical_wing_left.addChild(engine_left);

        engine_left.texOffs(4, 1).addBox(-3.0F, -3.0F, -1.25F, 6.0F, 6.0F, 2.5F, 0.0F, false);


        engine_right = new ModelRenderer(this);
        engine_right.setPos(15.5F - 8.0F, 20.5F - 24.0F, 13.75F - 24.0F); // pivot at cube center
        mechanical_wing_right.addChild(engine_right);

        engine_right.texOffs(4, 0).addBox(-3.0F, -3.0F, -1.25F, 6.0F, 6.0F, 2.5F, 0.0F, false);


        this.mechanical_wing_left.yRot = (float) Math.toRadians(0);
        this.mechanical_wing_right.yRot = (float) Math.toRadians(0);
        this.mechanical_wing_left.zRot = 0F;
        this.mechanical_wing_right.zRot = 0F;
        this.mechanical_wing_left.xRot = 180F;
        this.mechanical_wing_right.xRot = 180F;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        // Simple Elytra-like open/close behavior
        this.torso.setPos(0F, 4F, -3F);
        this.mechanical_wing_right.setPos(0F, 4F, -3F);
        this.mechanical_wing_left.setPos(0F, 4F, -3F);
        if (entity.isFallFlying()) {
            this.mechanical_wing_left.zRot = (float) Math.toRadians(5);
            this.mechanical_wing_right.zRot = (float) Math.toRadians(-5);
            this.mechanical_wing_left.yRot = (float) Math.toRadians(-45);
            this.mechanical_wing_right.yRot = (float) Math.toRadians(45);
            if (entity.isFallFlying()) {
                double speed = entity.getDeltaMovement().length(); // player velocity
                float spinSpeed = (float) (ageInTicks * 0.3F * speed * 2.0F);
                this.engine_left.zRot = spinSpeed;
                this.engine_right.zRot = -spinSpeed;
                ParticlePacket packetL = new ParticlePacket(this.engine_left.x, this.engine_left.y, this.engine_left.z, "smoke");
                ParticlePacket packetFlameL = new ParticlePacket(this.engine_left.x, this.engine_left.y, this.engine_left.z, "flame");
                ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), packetL);
                ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), packetL);
                ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), packetL);
                ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), packetFlameL);

                ParticlePacket packetR = new ParticlePacket(this.engine_right.x, this.engine_right.y, this.engine_right.z, "smoke");
                ParticlePacket packetFlameR = new ParticlePacket(this.engine_right.x, this.engine_right.y, this.engine_right.z, "flame");
                ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), packetR);
                ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), packetR);
                ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), packetR);
                ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), packetFlameR);

            } else {
                this.engine_left.zRot = 0F;
                this.engine_right.zRot = 0F;
            }
        } else if (entity.isCrouching()) {
            this.mechanical_wing_left.yRot = (float) Math.toRadians(-30);
            this.mechanical_wing_right.yRot = (float) Math.toRadians(30);
        } else {
            this.mechanical_wing_left.yRot = (float) Math.toRadians(0);
            this.mechanical_wing_right.yRot = (float) Math.toRadians(0);
            this.mechanical_wing_left.zRot = 0F;
            this.mechanical_wing_right.zRot = 0F;
            this.mechanical_wing_left.xRot = 180F;
            this.mechanical_wing_right.xRot = 180F;
        }
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        torso.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

