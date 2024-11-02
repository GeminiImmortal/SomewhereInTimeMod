package net.geminiimmortal.mobius.entity.render;


import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.ClubGolemEntity;
import net.geminiimmortal.mobius.entity.model.ClubGolem;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class ClubGolemRenderer extends MobRenderer<ClubGolemEntity, ClubGolem<ClubGolemEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/club_golem.png");
//    private static final Logger LOGGER = LogUtils.getLogger();

    public ClubGolemRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ClubGolem<>(), 0.5F); // Adjust the shadow size if needed
    }

    @Override
    public ResourceLocation getTextureLocation(ClubGolemEntity entity) {
        return TEXTURE;
    }

/*    @Override
    protected void setupRotations(ClubGolemEntity entity, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entity, ageInTicks, rotationYaw, partialTicks);

        // Add custom rotation logic here if needed (for example, based on entity state)
    }*/
}

