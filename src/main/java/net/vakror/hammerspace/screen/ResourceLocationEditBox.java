package net.vakror.hammerspace.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourceLocationEditBox extends EditBox {
    public ResourceLocationEditBox(Font font, int p_94115_, int p_94116_, int p_94117_, int p_94118_, Component p_94119_) {
        super(font, p_94115_, p_94116_, p_94117_, p_94118_, p_94119_);
    }

    @Override
    public void insertText(@NotNull String string) {
        String illegalCharactersRemoved = string.replaceAll("[^/:._a-z0-9-]", "");
        super.insertText(illegalCharactersRemoved);
    }
}
