package net.geminiimmortal.mobius.tileentity.render;

import net.geminiimmortal.mobius.tileentity.JumpPadTileEntity;
import net.geminiimmortal.mobius.tileentity.model.JumpPadModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class JumpPadRenderer extends GeoBlockRenderer<JumpPadTileEntity> {

    public JumpPadRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher, new JumpPadModel());
    }
}

