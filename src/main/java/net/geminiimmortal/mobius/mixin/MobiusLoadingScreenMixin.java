package net.geminiimmortal.mobius.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.DownloadTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DownloadTerrainScreen.class)
public abstract class MobiusLoadingScreenMixin extends Screen {

    protected MobiusLoadingScreenMixin(ITextComponent title) { super(title); }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderCustomLoading(MatrixStack matrix, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        
        if (mc.player.level.dimension().location().equals(ModDimensions.MOBIUS_WORLD.location())) {
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();

            ITextComponent lineOne = new TranslationTextComponent("overlay.mobius.loading_one")
                    .withStyle(TextFormatting.GRAY, TextFormatting.BOLD);
            ITextComponent lineTwo = new TranslationTextComponent("overlay.mobius.loading_two")
                    .withStyle(TextFormatting.GRAY, TextFormatting.BOLD);

            mc.getTextureManager().bind(new ResourceLocation("mobius", "textures/overlay/mobius_loading_screen.png"));
            AbstractGui.blit(matrix, 0, 0, 0, 0, width, height, width, height);

            int text1Width = mc.font.width(lineOne);
            int text2Width = mc.font.width(lineTwo);
            mc.font.draw(matrix, lineOne, (width - text1Width) / 2f, height / 2f - 20, 0xFFFFFF);
            mc.font.draw(matrix, lineTwo, (width - text2Width) / 2f, height / 2f + 4, 0xFFFFFF);

            ci.cancel();
        }
    }
}


