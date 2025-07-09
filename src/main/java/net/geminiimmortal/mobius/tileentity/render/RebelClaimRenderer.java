package net.geminiimmortal.mobius.tileentity.render;

import net.geminiimmortal.mobius.tileentity.RebelClaimTileEntity;
import net.geminiimmortal.mobius.tileentity.model.RebelClaimModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class RebelClaimRenderer extends GeoBlockRenderer<RebelClaimTileEntity> {

    public RebelClaimRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher, new RebelClaimModel());
    }
}

