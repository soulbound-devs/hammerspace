package net.vakror.hammerspace.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.hammerspace.capability.HammerspaceProvider;

import java.util.function.Supplier;

public class SyncHammerspaceS2CPacket {

    private final int tickSpeed;
    private final double fluidFlowSpeed;
    private final double gravity;

    public SyncHammerspaceS2CPacket(double fluidFlowSpeed, int tickSpeed, double gravity) {
        this.fluidFlowSpeed = fluidFlowSpeed;
        this.tickSpeed = tickSpeed;
        this.gravity = gravity;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(fluidFlowSpeed);
        buf.writeInt(tickSpeed);
        buf.writeDouble(gravity);
    }

    public SyncHammerspaceS2CPacket(FriendlyByteBuf buf) {
        fluidFlowSpeed = buf.readDouble();
        tickSpeed = buf.readInt();
        gravity = buf.readDouble();
    }

    public void handle(Supplier<NetworkEvent.Context> contextGetter) {
        final NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(() -> {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.level().getCapability(HammerspaceProvider.HAMMERSPACE).ifPresent(hammerspace -> {
                hammerspace.setTick(tickSpeed);
                hammerspace.setFluidFlowSpeed(fluidFlowSpeed);
                hammerspace.setGravity(gravity);
            });
        });
        context.setPacketHandled(true);
    }
}