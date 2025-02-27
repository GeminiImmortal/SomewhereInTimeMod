package net.geminiimmortal.mobius.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.container.custom.AstralConduitContainer;
import net.geminiimmortal.mobius.container.custom.EssenceChannelerContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;


public class EssenceChannelerScreen extends ContainerScreen<EssenceChannelerContainer> {
    private final ResourceLocation GUI = new ResourceLocation(MobiusMod.MOD_ID,
            "textures/gui/essence_channeler_gui.png");


    public EssenceChannelerScreen(EssenceChannelerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack,int mouseX, int mouseY, float partialTicks) {
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