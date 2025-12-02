package net.geminiimmortal.mobius.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.GrimcrowCaptainBossEntity;
import net.geminiimmortal.mobius.entity.model.GrimcrowCaptainModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;
import java.util.Optional;

public class GrimcrowCaptainRenderer extends GeoEntityRenderer<GrimcrowCaptainBossEntity> {
    public GrimcrowCaptainRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GrimcrowCaptainModel());
    }

    ResourceLocation modelLocation = new ResourceLocation(MobiusMod.MOD_ID, "geo/grimcrow_captain.geo.json");



    @Override
    public ResourceLocation getTextureLocation(GrimcrowCaptainBossEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/grimcrow_captain_boss.png");
    }

    @Override
    public void render(GeoModel model, GrimcrowCaptainBossEntity animatable, float partialTicks, RenderType type,
                       MatrixStack matrixStackIn, @Nullable IRenderTypeBuffer renderTypeBuffer,
                       @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn,
                       float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer,
                vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);

        Optional<GeoBone> engineLeft = model.getBone("engine_left");
        Optional<GeoBone> engineRight = model.getBone("engine_right");

        if (engineLeft.isPresent() && engineRight.isPresent() && animatable.level.isClientSide && !animatable.isOnGround()) {
            // Entity’s world position
            double baseX = animatable.getX();
            double baseY = animatable.getY();
            double baseZ = animatable.getZ();

            // Bone positions are relative to model origin — convert to world space
            GeoBone left = engineLeft.get();
            GeoBone right = engineRight.get();

            double lx = baseX + left.getPositionX(); // divide by 16 since GeoLib units are usually pixels
            double ly = baseY + left.getPositionY() + 3.25;
            double lz = baseZ + left.getPositionZ();

            double rx = baseX + right.getPositionX();
            double ry = baseY + right.getPositionY() + 3.25;
            double rz = baseZ + right.getPositionZ();

            for (int i = 0; i < 4; i++) {
                animatable.level.addParticle(ParticleTypes.SMOKE, lx, ly, lz, 0, -0.1, 0);
                animatable.level.addParticle(ParticleTypes.SMOKE, rx, ry, rz, 0, -0.1, 0);
            }
        }
    }

}

