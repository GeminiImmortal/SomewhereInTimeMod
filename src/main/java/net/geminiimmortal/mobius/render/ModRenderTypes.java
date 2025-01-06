package net.geminiimmortal.mobius.render;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ModRenderTypes extends RenderType {



    public static final RenderType GLOWING = create("glowing",
            DefaultVertexFormats.BLOCK, GL11.GL_QUADS, 256, true, true,
            State.builder().setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .setTransparencyState(RenderState.ADDITIVE_TRANSPARENCY)
                    .setTextureState(TextureState.BLOCK_SHEET)
                    .setCullState(NO_CULL)
                    .setDepthTestState(RenderState.LEQUAL_DEPTH_TEST)
                    .createCompositeState(true));

    private ModRenderTypes(String name, VertexFormat format, int glMode, int bufferSize, boolean useDelegate, boolean needsSorting, Runnable setupTask, Runnable clearTask) {
        super(name, format, glMode, bufferSize, useDelegate, needsSorting, setupTask, clearTask);
    }
}
