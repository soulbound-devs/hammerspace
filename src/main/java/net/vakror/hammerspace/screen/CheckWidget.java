package net.vakror.hammerspace.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class CheckWidget extends AbstractWidget {
    public CheckWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
    }

    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int width = this.getWidth();
        int height = this.getHeight();
        if (this.isHovered) {
            graphics.blit(TeleporterScreen.CHECK_SELECTED, this.getX(), this.getY(), 0.0F, 0.0F, width, height, width, height);
        } else {
            graphics.blit(TeleporterScreen.CHECK_UNSELECTED, this.getX(), this.getY(), 0.0F, 0.0F, width, height, width, height);
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput elementOutput) {
    }
}
