package net.vakror.hammerspace.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.vakror.hammerspace.HammerspaceMod;

public class Hammerspace {
    private double gravity = 1;
    private int tick = 1;
    private double fluidFlowSpeed = 1;
    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("gravity", gravity);
        nbt.putInt("tick", tick);
        nbt.putDouble("fluidFlowSpeed", fluidFlowSpeed);

        return nbt;
    }

    public void fromNbt(CompoundTag nbt) {
        gravity = nbt.getDouble("gravity");
        tick = nbt.getInt("tick");
        fluidFlowSpeed = nbt.getDouble("fluidFlowSpeed");
    }

    public double gravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public int tick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public double fluidFlowSpeed() {
        return fluidFlowSpeed;
    }

    public void setFluidFlowSpeed(double fluidFlowSpeed) {
        this.fluidFlowSpeed = fluidFlowSpeed;
    }
}
