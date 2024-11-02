package net.geminiimmortal.mobius.entity.render;


import com.mojang.blaze3d.matrix.MatrixStack;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.HeartGolemEntity;
import net.geminiimmortal.mobius.entity.model.HeartGolem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class HeartGolemRenderer extends MobRenderer<HeartGolemEntity, HeartGolem<HeartGolemEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/heart_golem.png");
//    private static final Logger LOGGER = LogUtils.getLogger();

    public HeartGolemRenderer(EntityRendererManager renderManager) {
        super(renderManager, new HeartGolem<>(), 0.5F); // Adjust the shadow size if needed
    }

    @Override
    public ResourceLocation getTextureLocation(HeartGolemEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(HeartGolemEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }


/*    @Override
    protected void setupRotations(ClubGolemEntity entity, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entity, ageInTicks, rotationYaw, partialTicks);

        // Add custom rotation logic here if needed (for example, based on entity state)
    }*/
}

