package net.vakror.hammerspace.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.vakror.hammerspace.HammerspaceMod;
import net.vakror.hammerspace.capability.Teleporter;
import net.vakror.hammerspace.capability.TeleporterProvider;
import net.vakror.hammerspace.item.custom.TeleporterItem;
import net.vakror.hammerspace.packet.ModPackets;
import net.vakror.hammerspace.packet.SyncTeleporterDataC2SPacket;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TeleporterScreen extends Screen {
    private EditBox editDimensionId;
    private EditBox editWidth;
    private EditBox editLength;
    private EditBox editHeight;
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
        this.editDimensionId = new ResourceLocationEditBox(this.font, this.width / 2 - 152, 40, 300, 20, Component.translatable("hammerspace.teleporter_gui.box.message"));
        this.editDimensionId.setMaxLength(128);
        this.addWidget(this.editDimensionId);

        this.editWidth = new IntegerEditBox(this.font, this.width / 2 - 152, 80, 80, 20, Component.translatable("hammerspace.teleporter_gui.edit_width.message"), 1, maxWidth, this.shouldUpdateSize, teleporter.width());
        this.editWidth.setMaxLength(3);
        this.addWidget(this.editWidth);

        this.editHeight = new IntegerEditBox(this.font, this.width / 2 - 72, 80, 80, 20, Component.translatable("hammerspace.teleporter_gui.edit_height.message"), 2, maxHeight, shouldUpdateSize, teleporter.height());
        this.editHeight.setMaxLength(3);
        this.addWidget(this.editHeight);

        this.editLength = new IntegerEditBox(this.font, this.width / 2 + 8, 80, 80, 20, Component.translatable("hammerspace.teleporter_gui.edit_length.message"), 1, maxLength, shouldUpdateSize, teleporter.length());
        this.editLength.setMaxLength(3);
        this.addWidget(this.editLength);
        
        this.setInitialFocus(editDimensionId);
        editDimensionId.setEditable(canChangeDimName);

        editDimensionId.setValue(teleporter.dimensionId());
        editWidth.setValue(String.valueOf(teleporter.width()));
        editHeight.setValue(String.valueOf(teleporter.height()));
        editLength.setValue(String.valueOf(teleporter.length()));
    }

    @Override
    public void tick() {
        super.tick();

        editDimensionId.tick();
        editWidth.tick();
        editHeight.tick();
        editLength.tick();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        int x = (width - 256) / 2;
        int y = (height - 256) / 2;

        checkWidget = new CheckWidget(x + 125, y + 125, 18, 18);


        if (editDimensionId.getValue().equals("")) {
            editDimensionId.setSuggestion("Dimension Id");
        } else {
            editDimensionId.setSuggestion("");
        }

        if (editWidth.getValue().equals("")) {
            editWidth.setSuggestion("Width");
        }
        else {
            editWidth.setSuggestion("");
        }
        if (editHeight.getValue().equals("")) {
            editHeight.setSuggestion("Height");
        }
        else {
            editHeight.setSuggestion("");
        }
        if (editLength.getValue().equals("")) {
            editLength.setSuggestion("Length");
        }
        else {
            editLength.setSuggestion("");
        }


        graphics.drawString(this.font, Component.translatable("hammerspace.teleporter_gui.box.message"), this.width / 2 - 153, 30, 10526880);
        graphics.drawString(this.font, Component.translatable("hammerspace.teleporter_gui.dimensions.message"), this.width / 2 - 153, 70, 10526880);

        if (needsToShowMessage) {
            graphics.drawString(this.font, Component.translatable("hammerspace.teleporter_gui.all_boxes_not_filled_error"), x + 115, y + 115, -65536);
        }
        if (valueIsNotLargerThanBefore && shouldUpdateSize) {
            graphics.drawString(this.font, Component.translatable("hammerspace.teleporter_gui.value_not_larger_than_before"), x + 115, y + 115, -65536);
        }

        editDimensionId.render(graphics, mouseX, mouseY, partialTick);
        editWidth.render(graphics, mouseX, mouseY, partialTick);
        editHeight.render(graphics, mouseX, mouseY, partialTick);
        editLength.render(graphics, mouseX, mouseY, partialTick);
        checkWidget.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
        if (checkWidget != null && checkWidget.isHovered()) {
            if (!this.editDimensionId.getValue().equals("") && !this.editWidth.getValue().equals("") && !this.editHeight.getValue().equals("") && !this.editLength.getValue().equals("")) {
                if ((this.teleporter.width() > Integer.parseInt(this.editWidth.getValue()) || this.teleporter.height() > Integer.parseInt(this.editHeight.getValue()) || this.teleporter.length() > Integer.parseInt(this.editLength.getValue())) && shouldUpdateSize)  {
                    valueIsNotLargerThanBefore = true;
                } else {
                    Objects.requireNonNull(this.getMinecraft().player).getItemInHand(hand).getCapability(TeleporterProvider.TELEPORTER).ifPresent((teleporter -> teleporter.setDimensionId(this.editDimensionId.getValue())));
                    this.teleporter.setDimensionId(editDimensionId.getValue());
                    this.teleporter.setHasSizeChanged(shouldUpdateSize);
                    this.teleporter.setWidth(Integer.parseInt(this.editWidth.getValue()));
                    this.teleporter.setHeight(Integer.parseInt(this.editHeight.getValue()));
                    this.teleporter.setLength(Integer.parseInt(this.editLength.getValue()));
                    ModPackets.sendToServer(new SyncTeleporterDataC2SPacket(this.teleporter, this.hand));
                    assert this.minecraft != null;
                    this.minecraft.setScreen(null);
                }
            } else {
                needsToShowMessage = true;
            }
        }
        return super.mouseClicked(p_94695_, p_94696_, p_94697_);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, 18, 18);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
        return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
    }
}