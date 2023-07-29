package net.vakror.hammerspace.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.vakror.hammerspace.HammerspaceMod;
import net.vakror.hammerspace.capability.Teleporter;
import net.vakror.hammerspace.capability.TeleporterProvider;
import net.vakror.hammerspace.packet.ModPackets;
import net.vakror.hammerspace.packet.SyncTeleporterDataC2SPacket;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TeleporterScreen extends Screen {
    private EditBox editDimensionId;
    private EditBox editWidth;
    private EditBox editLength;
    private EditBox editHeight;
    private EditBox editTickSpeed;
    private EditBox editDimGravity;
    private EditBox editRandomTickSpeed;
    private CheckWidget checkWidget;
    private final Teleporter teleporter;
    private final InteractionHand hand;
    public static final ResourceLocation BACKGROUND = new ResourceLocation(HammerspaceMod.MOD_ID, "textures/gui/teleporter.png");
    public static final ResourceLocation CHECK_SELECTED = new ResourceLocation(HammerspaceMod.MOD_ID, "textures/gui/check_selected.png");
    public static final ResourceLocation CHECK_UNSELECTED = new ResourceLocation(HammerspaceMod.MOD_ID, "textures/gui/check_unselected.png");

    private final int maxWidth;
    private final int maxHeight;
    private final int maxLength;
    private boolean needsToShowMessage = false;
    private final boolean canChangeDimName;
    private final boolean shouldUpdateSize;
    private boolean valueIsNotLargerThanBefore = false;

    public TeleporterScreen(Teleporter teleporter, InteractionHand hand, int maxWidth, int maxHeight, int maxLength, boolean canChangeDimName, boolean shouldUpdateSize) {
        super(Component.translatable("hammerspace.teleporter_gui_title"));
        this.teleporter = teleporter;
        this.hand = hand;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.maxLength = maxLength;
        this.canChangeDimName = canChangeDimName;
        this.shouldUpdateSize = shouldUpdateSize;
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - 256) / 2;
        int y = (height - 256) / 2;
        checkWidget = new CheckWidget(x + 105, y + 150, 18, 18);
        this.addWidget(checkWidget);

        this.editDimensionId = addResourceLocationEditBox(- 152, 40, 300, 20, teleporter.dimensionId(), canChangeDimName);

        this.editWidth = addIntEditBox(- 152, 80, 80, 20, teleporter.width(), 1, maxWidth, this.shouldUpdateSize, teleporter.width());
        this.editHeight = addIntEditBox(- 72, 80, 80, 20, teleporter.height(),2, maxHeight, shouldUpdateSize, teleporter.height());
        this.editLength = addIntEditBox(8, 80, 80, 20, teleporter.length(), 1, maxLength, shouldUpdateSize, teleporter.length());

        this.editTickSpeed = addIntEditBox(- 152, 120, 80, 20, teleporter.tickSpeed(), 1, 10, false, 0);
        this.editDimGravity = addDoubleEditBox(- 72, 120, 80, 20, teleporter.gravity(), 0.01, 255, false, 0);
        this.editRandomTickSpeed = addDoubleEditBox(8, 120, 80, 20, teleporter.randomTickSpeed(), 0.5, 10, false, 0);

        this.setInitialFocus(editDimensionId);
    }

    public IntegerEditBox addIntEditBox(int x, int y, int width, int height, int initialValue, int min, int max, boolean allowLargerValues, int previous) {
        IntegerEditBox box = new IntegerEditBox(this.font, this.width / 2 + x, y, width, height, Component.empty(), min, max, allowLargerValues, previous);
        box.setMaxLength(15);
        if (initialValue != 0) {
            box.setValue(String.valueOf(initialValue));
        }
        this.addWidget(box);
        return box;
    }

    public DoubleEditBox addDoubleEditBox(int x, int y, int width, int height, double initialValue, double min, double max, boolean allowLargerValues, int previous) {
        DoubleEditBox box = new DoubleEditBox(this.font, this.width / 2 + x, y, width, height, Component.empty(), min, max, allowLargerValues, previous);
        box.setMaxLength(15);
        if (initialValue != 0) {
            box.setValue(String.valueOf(initialValue));
        }
        this.addWidget(box);
        return box;
    }

    public ResourceLocationEditBox addResourceLocationEditBox(int x, int y, int width, int height, String initialValue, boolean editable) {
        ResourceLocationEditBox box = new ResourceLocationEditBox(this.font, this.width / 2 + x, y, width, height, Component.empty());
        box.setMaxLength(128);
        if (initialValue != null) {
            box.setValue(initialValue);
        }
        box.setEditable(editable);
        this.addWidget(box);
        return box;
    }

    @Override
    public void tick() {
        super.tick();

        editDimensionId.tick();
        editWidth.tick();
        editHeight.tick();
        editLength.tick();
        editTickSpeed.tick();
        editDimGravity.tick();
        editRandomTickSpeed.tick();
    }

    @Override
    public void render(@NotNull PoseStack matrices, int mouseX, int mouseY, float partialTick) {
        super.render(matrices, mouseX, mouseY, partialTick);

        int x = (width - 256) / 2;
        int y = (height - 256) / 2;

        setSuggestionIfValueIsEmpty(editDimensionId, "Dimension ID");
        setSuggestionIfValueIsEmpty(editWidth, "Width");
        setSuggestionIfValueIsEmpty(editHeight, "Height");
        setSuggestionIfValueIsEmpty(editLength, "Length");
        setSuggestionIfValueIsEmpty(editTickSpeed, "Tick Speed");
        setSuggestionIfValueIsEmpty(editDimGravity, "Gravity Multiplier");
        setSuggestionIfValueIsEmpty(editRandomTickSpeed, "Random Tick Speed");


        drawString(matrices, this.font, Component.translatable("hammerspace.teleporter_gui.box.message"), this.width / 2 - 153, 30, 10526880);
        drawString(matrices, this.font, Component.translatable("hammerspace.teleporter_gui.dimensions.message"), this.width / 2 - 153, 70, 10526880);

        if (needsToShowMessage) {
            drawString(matrices, this.font, Component.translatable("hammerspace.teleporter_gui.all_boxes_not_filled_error"), x + 115, y + 115, -65536);
        }
        if (valueIsNotLargerThanBefore && shouldUpdateSize) {
            drawString(matrices, this.font, Component.translatable("hammerspace.teleporter_gui.value_not_larger_than_before"), x + 115, y + 115, -65536);
        }

        editDimensionId.render(matrices, mouseX, mouseY, partialTick);
        editWidth.render(matrices, mouseX, mouseY, partialTick);
        editHeight.render(matrices, mouseX, mouseY, partialTick);
        editLength.render(matrices, mouseX, mouseY, partialTick);
        checkWidget.render(matrices, mouseX, mouseY, partialTick);
        editTickSpeed.render(matrices, mouseX, mouseY, partialTick);
        editDimGravity.render(matrices, mouseX, mouseY, partialTick);
        editRandomTickSpeed.render(matrices, mouseX, mouseY, partialTick);
    }

    public void setSuggestionIfValueIsEmpty(EditBox editBox, String suggestion) {
        if (editBox.getValue().equals("")) {
            editBox.setSuggestion(suggestion);
        } else {
            editBox.setSuggestion("");
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (checkWidget != null && checkWidget.isMouseOver(pMouseX, pMouseY)) {
            if (!this.editDimensionId.getValue().equals("") && !this.editWidth.getValue().equals("") && !this.editHeight.getValue().equals("") && !this.editLength.getValue().equals("") && !this.editTickSpeed.getValue().equals("") && !this.editDimensionId.getValue().equals("") && !this.editRandomTickSpeed.getValue().equals("")) {
                if ((this.teleporter.width() > Integer.parseInt(this.editWidth.getValue()) || this.teleporter.height() > Integer.parseInt(this.editHeight.getValue()) || this.teleporter.length() > Integer.parseInt(this.editLength.getValue())) && shouldUpdateSize)  {
                    valueIsNotLargerThanBefore = true;
                } else {
                    Objects.requireNonNull(this.getMinecraft().player).getItemInHand(hand).getCapability(TeleporterProvider.TELEPORTER).ifPresent((teleporter -> teleporter.setDimensionId(this.editDimensionId.getValue())));
                    this.teleporter.setDimensionId(editDimensionId.getValue());
                    this.teleporter.setWidth(Integer.parseInt(this.editWidth.getValue()));
                    this.teleporter.setHeight(Integer.parseInt(this.editHeight.getValue()));
                    this.teleporter.setLength(Integer.parseInt(this.editLength.getValue()));
                    this.teleporter.setTickSpeed(Integer.parseInt(this.editTickSpeed.getValue()));
                    this.teleporter.setGravity(Double.parseDouble(this.editDimGravity.getValue()));
                    this.teleporter.setRandomTickSpeed((int) Float.parseFloat(this.editRandomTickSpeed.getValue()));
                    ModPackets.sendToServer(new SyncTeleporterDataC2SPacket(this.teleporter, this.hand));
                    assert this.minecraft != null;
                    this.minecraft.setScreen(null);
                }
            } else {
                needsToShowMessage = true;
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, 18, 18);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
        return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
    }
}