package net.vakror.hammerspace.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HammerspaceProvider implements ICapabilitySerializable<CompoundTag> {
    public static Capability<Hammerspace> HAMMERSPACE = CapabilityManager.get(new CapabilityToken<Hammerspace>() { });

    private Hammerspace hammerspace = null;
    private final LazyOptional<Hammerspace> optional = LazyOptional.of(this::createHammerspace);

    private @NotNull Hammerspace createHammerspace() {
        if (this.hammerspace == null) {
            this.hammerspace = new Hammerspace();
        }
        return this.hammerspace;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == HAMMERSPACE) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return createHammerspace().toNbt();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createHammerspace().fromNbt(nbt);
    }
}