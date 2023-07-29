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
    private BlockPos lastUsedLocation = new BlockPos(0, 64, 0);
    private String fromDimensionTypeId = "minecraft:overworld";
    private double gravity = 0;
    private int tickSpeed = 0;
    private int randomTickSpeed = 1;
    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("dimensionId", dimensionId);

        nbt.putInt("width", width);
        nbt.putInt("length", width);
        nbt.putInt("height", width);

        nbt.putIntArray("lastUsedLocation", new int[]{lastUsedLocation.getX(), lastUsedLocation.getY(), lastUsedLocation.getZ()});
        nbt.putString("fromDimensionTypeId", fromDimensionTypeId);

        nbt.putDouble("gravity", gravity);
        nbt.putInt("tickSpeed", tickSpeed);
        nbt.putDouble("randomTickSpeed", randomTickSpeed);

        return nbt;
    }

    public void fromNbt(CompoundTag nbt) {
        dimensionId = nbt.getString("dimensionId");

        width = nbt.getInt("width");
        length = nbt.getInt("length");
        height = nbt.getInt("height");

        lastUsedLocation = new BlockPos(nbt.getIntArray("lastUsedLocation")[0], nbt.getIntArray("lastUsedLocation")[1], nbt.getIntArray("lastUsedLocation")[2]);
        fromDimensionTypeId = nbt.getString("fromDimensionTypeId");

        gravity = nbt.getDouble("gravity");
        tickSpeed = nbt.getInt("tickSpeed");
        randomTickSpeed = nbt.getInt("randomTickSpeed");
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

    public double gravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public int tickSpeed() {
        return tickSpeed;
    }

    public void setTickSpeed(int tickSpeed) {
        this.tickSpeed = tickSpeed;
    }

    public int randomTickSpeed() {
        return randomTickSpeed;
    }

    public void setRandomTickSpeed(int randomTickSpeed) {
        this.randomTickSpeed = randomTickSpeed;
    }

    public void copyOf(Teleporter teleporter) {
        this.dimensionId = teleporter.dimensionId;
        this.length = teleporter.length;
        this.width = teleporter.width;
        this.height = teleporter.height;
        this.lastUsedLocation = teleporter.lastUsedLocation;
        this.fromDimensionTypeId = teleporter.fromDimensionTypeId;
        this.tickSpeed = teleporter.tickSpeed;
        this.gravity = teleporter.gravity;
        this.randomTickSpeed = teleporter.randomTickSpeed;
    }

    public ResourceLocation getDimIdAsResourceLocation() {
        return dimensionId.contains(":") ? new ResourceLocation(dimensionId): new ResourceLocation(HammerspaceMod.MOD_ID, dimensionId);
    }
}
