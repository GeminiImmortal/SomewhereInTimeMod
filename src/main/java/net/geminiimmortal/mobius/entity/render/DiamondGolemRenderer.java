package net.geminiimmortal.mobius.entity.render;


import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.DiamondGolemEntity;
import net.geminiimmortal.mobius.entity.model.DiamondGolem;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class DiamondGolemRenderer extends MobRenderer<DiamondGolemEntity, DiamondGolem<DiamondGolemEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/diamond_golem.png");
//    private static final Logger LOGGER = LogUtils.getLogger();

    public DiamondGolemRenderer(EntityRendererManager renderManager) {
        super(renderManager, new DiamondGolem<>(), 0.5F); // Adjust the shadow size if needed
    }

    @Override
    public ResourceLocation getTextureLocation(DiamondGolemEntity entity) {
        return TEXTURE;
    }

/*    @Override
    protected void setupRotations(DiamondGolemEntity entity, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entity, ageInTicks, rotationYaw, partialTicks);

        // Add custom rotation logic here if needed (for example, based on entity state)
    }*/
}

