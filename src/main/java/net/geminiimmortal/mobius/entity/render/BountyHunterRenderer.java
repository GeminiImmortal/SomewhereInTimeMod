package net.geminiimmortal.mobius.entity.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.BountyHunterEntity;
import net.geminiimmortal.mobius.entity.custom.FootmanEntity;
import net.geminiimmortal.mobius.entity.model.BountyHunterModel;
import net.geminiimmortal.mobius.entity.model.FootmanModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BountyHunterRenderer extends GeoEntityRenderer<BountyHunterEntity> {
    public BountyHunterRenderer(EntityRendererManager renderManager) {
        super(renderManager, new BountyHunterModel());
    }

    @Override
    public ResourceLocation getTextureLocation(BountyHunterEntity entity) {
        return new ResourceLocation(MobiusMod.MOD_ID, "textures/entity/bounty_hunter.png");
    }
}

