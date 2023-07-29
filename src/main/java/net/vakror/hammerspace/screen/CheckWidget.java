package net.vakror.hammerspace.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class CheckWidget extends AbstractWidget {
    public CheckWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
    }

    @Override
    public void render(@NotNull PoseStack matrices, int mouseX, int mouseY, float partialTick) {
        int width = this.getWidth();
        int height = this.getHeight();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        if (this.isHovered) {
            RenderSystem.setShaderTexture(0, TeleporterScreen.CHECK_SELECTED);
            blit(matrices, this.x, this.y, 0.0F, 0.0F, width, height, width, height);
        } else {
            RenderSystem.setShaderTexture(0, TeleporterScreen.CHECK_UNSELECTED);
            blit(matrices, x, y, 0.0F, 0.0F, width, height, width, height);
        }
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
    }
}
