package net.vakror.hammerspace.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeleporterProvider implements ICapabilitySerializable<CompoundTag> {
    public static Capability<Teleporter> TELEPORTER = CapabilityManager.get(new CapabilityToken<Teleporter>() { });

    private Teleporter teleporter = null;
    private final LazyOptional<Teleporter> optional = LazyOptional.of(this::createTeleporter);

    private @NotNull Teleporter createTeleporter() {
        if (this.teleporter == null) {
            this.teleporter = new Teleporter();
        }
        return this.teleporter;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == TELEPORTER) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return createTeleporter().toNbt();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createTeleporter().fromNbt(nbt);
    }
}