package net.geminiimmortal.mobius.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.container.custom.SoulForgeContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;


public class SoulForgeScreen extends ContainerScreen<SoulForgeContainer> {
    private final ResourceLocation GUI = new ResourceLocation(MobiusMod.MOD_ID,
            "textures/gui/soul_forge_gui.png");

    public SoulForgeScreen(SoulForgeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
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
        this.minecraft.getTextureManager().bind(GUI);
        int i = this.getGuiLeft();
        int j = this.getGuiTop() + 2;
        this.blit(matrixStack, i, j, 0, 0, this.getXSize(), this.getYSize());


            int arrowProgress = 0;
            this.blit(matrixStack, x + 79, y + 34, 176, 0, arrowProgress + 1, 16);

    }

   /* private int getArrowProgress() {
        int progress = So;
        int maxProgress = SoulForgeTileEntity.getMaxProgress();
        return progress > 0 && maxProgress > 0 ? (progress * 24) / maxProgress : 0; // Assuming the arrow is 24 pixels wide
    }*/
}