package net.vakror.hammerspace.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.vakror.hammerspace.HammerspaceMod;

public class Teleporter {
    private String dimensionId = "";
    private int width = 0;
    private int length = 0;
    private int height = 0;
    private int previousWidth = 0;
    private int previousLength = 0;
    private int previousHeight = 0;
    private BlockPos lastUsedLocation = new BlockPos(0, 64, 0);
    private String fromDimensionTypeId = "minecraft:overworld";
    private boolean hasSizeChanged = false;
    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("dimensionId", dimensionId);

        nbt.putInt("width", width);
        nbt.putInt("length", width);
        nbt.putInt("height", width);

        nbt.putInt("previousWidth", previousWidth);
        nbt.putInt("previousLength", previousLength);
        nbt.putInt("previousHeight", previousHeight);

        nbt.putIntArray("lastUsedLocation", new int[]{lastUsedLocation.getX(), lastUsedLocation.getY(), lastUsedLocation.getZ()});
        nbt.putString("fromDimensionTypeId", fromDimensionTypeId);
        nbt.putBoolean("sizeChanged", hasSizeChanged);

        return nbt;
    }

    public void fromNbt(CompoundTag nbt) {
        dimensionId = nbt.getString("dimensionId");

        width = nbt.getInt("width");
        length = nbt.getInt("length");
        height = nbt.getInt("height");

        previousWidth = nbt.getInt("previousWidth");
        previousLength = nbt.getInt("previousLength");
        previousHeight = nbt.getInt("previousLength");

        lastUsedLocation = new BlockPos(nbt.getIntArray("lastUsedLocation")[0], nbt.getIntArray("lastUsedLocation")[1], nbt.getIntArray("lastUsedLocation")[2]);
        fromDimensionTypeId = nbt.getString("fromDimensionTypeId");
        hasSizeChanged = nbt.getBoolean("sizeChanged");
    }

    public int previousWidth() {
        return previousWidth;
    }

    public void setPreviousWidth(int previousWidth) {
        this.previousWidth = previousWidth;
    }

    public int previousLength() {
        return previousLength;
    }

    public void setPreviousLength(int previousLength) {
        this.previousLength = previousLength;
    }

    public int previousHeight() {
        return previousHeight;
    }

    public void setPreviousHeight(int previousHeight) {
        this.previousHeight = previousHeight;
    }

    public String dimensionId() {
        return dimensionId;
    }

    public void setDimensionId(String dimensionId) {
        this.dimensionId = dimensionId;
    }

    public int width() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int length() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int height() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BlockPos lastUsedLocation() {
        return lastUsedLocation;
    }

    public void setLastUsedLocation(BlockPos lastUsedLocation) {
        this.lastUsedLocation = lastUsedLocation;
    }

    public String fromDimensionTypeId() {
        return fromDimensionTypeId;
    }

    public void setFromDimensionTypeId(String fromDimensionTypeId) {
        this.fromDimensionTypeId = fromDimensionTypeId;
    }

    public boolean hasSizeChanged() {
        return hasSizeChanged;
    }

    /**
     * <b><h1>ALWAYS</h1></b> Should be called before actually changing the size so that previousSize can be set
     * @param hasSizeChanged - whether the size has changed
     */
    public void setHasSizeChanged(boolean hasSizeChanged) {
        this.hasSizeChanged = hasSizeChanged;
        this.previousWidth = width;
        this.previousLength = length;
        this.previousHeight = height;
    }

    public void copyOf(Teleporter teleporter) {
        this.dimensionId = teleporter.dimensionId;
        this.length = teleporter.length;
        this.width = teleporter.width;
        this.height = teleporter.height;
        this.hasSizeChanged = teleporter.hasSizeChanged;
        this.lastUsedLocation = teleporter.lastUsedLocation;
        this.fromDimensionTypeId = teleporter.fromDimensionTypeId;
    }

    public ResourceLocation getDimIdAsResourceLocation() {
        return dimensionId.contains(":") ? new ResourceLocation(dimensionId): new ResourceLocation(HammerspaceMod.MOD_ID, dimensionId);
    }
}
