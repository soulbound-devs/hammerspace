package net.vakror.hammerspace.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class IntegerEditBox extends EditBox {
    int max;
    int min;
    boolean onlyAllowLargerValues;
    final int previousValue;

    public IntegerEditBox(Font font, int p_94115_, int p_94116_, int p_94117_, int p_94118_, Component p_94119_, int min, int max, boolean onlyAllowLargerValues, int previousValue) {
        super(font, p_94115_, p_94116_, p_94117_, p_94118_, p_94119_);
        this.max = max;
        this.min = min;
        this.onlyAllowLargerValues = onlyAllowLargerValues;
        this.previousValue = previousValue;
    }

    @Override
    public void insertText(@NotNull String string) {
        String illegalCharactersRemoved = string.replaceAll("[^0-9]", "");
        if (!(getValue() + illegalCharactersRemoved).equals("")) {
            if (Integer.parseInt(getValue() + illegalCharactersRemoved) > max) {
                setValue(String.valueOf(max));
                return;
            } else if (Integer.parseInt(getValue() + illegalCharactersRemoved) < min) {
                setValue(String.valueOf(min));
                return;
            } else if (onlyAllowLargerValues && (Integer.parseInt(getValue() + illegalCharactersRemoved) < previousValue)) {
                setValue(String.valueOf(previousValue));
                return;
            }
        }
        super.insertText(illegalCharactersRemoved);
    }
}