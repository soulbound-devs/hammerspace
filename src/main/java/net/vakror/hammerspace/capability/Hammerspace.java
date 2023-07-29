package net.vakror.hammerspace.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.vakror.hammerspace.HammerspaceMod;

public class Hammerspace {
    private int tick = 1;
    private int randomTick = 1;

    private double gravity = 1;
    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("tick", tick);
        nbt.putInt("randomTick", tick);

        return nbt;
    }

    public void fromNbt(CompoundTag nbt) {
        tick = nbt.getInt("tick");
        nbt.putInt("randomTick", tick);
    }

    public int tick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public double gravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public int randomTick() {
        return randomTick;
    }

    public void setRandomTick(int randomTick) {
        this.randomTick = randomTick;
    }
}
