package net.geminiimmortal.mobius.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.geminiimmortal.mobius.container.custom.JournalContainer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class JournalScreen extends ContainerScreen<JournalContainer> implements IHasContainer<JournalContainer> {
    public JournalScreen(JournalContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        // set dimensions, etc.
    }

    @Override
    public JournalContainer getMenu() {
        return this.menu; // or `this.container` if you're using older naming
    }



    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        // Draw title or leave empty
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        // Draw GUI background here
    }
}
