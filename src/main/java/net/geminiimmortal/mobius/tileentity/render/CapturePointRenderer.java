package net.geminiimmortal.mobius.tileentity.render;

import net.geminiimmortal.mobius.tileentity.CapturePointTileEntity;
import net.geminiimmortal.mobius.tileentity.model.CapturePointModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class CapturePointRenderer extends GeoBlockRenderer<CapturePointTileEntity> {

    public CapturePointRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher, new CapturePointModel());
    }
}

