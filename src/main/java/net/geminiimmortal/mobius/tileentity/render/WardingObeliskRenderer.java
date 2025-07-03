package net.geminiimmortal.mobius.tileentity.render;

import net.geminiimmortal.mobius.tileentity.WardingObeliskTileEntity;
import net.geminiimmortal.mobius.tileentity.model.WardingObeliskModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class WardingObeliskRenderer extends GeoBlockRenderer<WardingObeliskTileEntity> {

    public WardingObeliskRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher, new WardingObeliskModel());
    }
}

