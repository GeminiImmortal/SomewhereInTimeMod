package net.geminiimmortal.mobius.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.container.custom.AstralConduitContainer;
import net.geminiimmortal.mobius.container.custom.SoulForgeContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;


public class AstralConduitScreen extends ContainerScreen<AstralConduitContainer> {
    private final ResourceLocation GUI = new ResourceLocation(MobiusMod.MOD_ID,
            "textures/gui/astral_conduit_gui.png");

    public AstralConduitScreen(AstralConduitContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }


    public void render(MatrixStack matrixStack,float partialTicks, int mouseX, int mouseY) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bind(GUI);
        int posX = (this.width - this.imageWidth) / 2;
        int posY = (this.height - this.imageHeight) / 2;

        blit(matrixStack, posX, posY + 2, 0, 0, this.imageWidth, this.imageHeight);

    }
}