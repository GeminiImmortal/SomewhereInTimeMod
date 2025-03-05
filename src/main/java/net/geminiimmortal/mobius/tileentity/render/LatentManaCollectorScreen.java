package net.geminiimmortal.mobius.tileentity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.geminiimmortal.mobius.container.custom.LatentManaCollectorContainer;
import net.geminiimmortal.mobius.item.custom.ManaVial;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class LatentManaCollectorScreen extends ContainerScreen<LatentManaCollectorContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("mobius", "textures/gui/latent_mana_collector_gui.png");

    public LatentManaCollectorScreen(LatentManaCollectorContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);

        ItemStack flask = this.getMenu().getSlot(0).getItem();
        if (!flask.isEmpty() && flask.getItem() instanceof ManaVial) {
            int mana = ((ManaVial) flask.getItem()).getStoredMana(flask);
            this.font.draw(matrixStack, "Mana: " + mana, this.leftPos + 8, this.topPos + 50, 0xFFFFFF);
        }
    }


    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bind(GUI_TEXTURE);
        int posX = (this.width - this.imageWidth) / 2;
        int posY = (this.height - this.imageHeight) / 2;

        blit(matrixStack, posX, posY - 1, 0, 0, this.imageWidth, this.imageHeight);

    }
}

