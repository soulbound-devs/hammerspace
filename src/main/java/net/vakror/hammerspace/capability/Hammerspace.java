package net.vakror.hammerspace.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.vakror.hammerspace.HammerspaceMod;

public class Hammerspace {
    private double gravity = 1;
    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("gravity", gravity);

        return nbt;
    }

    public void fromNbt(CompoundTag nbt) {
        gravity = nbt.getDouble("gravity");
    }

    public double gravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }
}
