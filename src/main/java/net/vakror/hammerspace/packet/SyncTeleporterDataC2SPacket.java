package net.vakror.hammerspace.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.hammerspace.capability.Teleporter;
import net.vakror.hammerspace.capability.TeleporterProvider;

import java.util.Objects;
import java.util.function.Supplier;

public class SyncTeleporterDataC2SPacket {

    private final Teleporter dataToSync;
    private final InteractionHand hand;

    public SyncTeleporterDataC2SPacket(final Teleporter dataToSync, InteractionHand hand) {
        this.dataToSync = dataToSync;
        this.hand = hand;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(dataToSync.toNbt());
        buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
    }

    public SyncTeleporterDataC2SPacket(FriendlyByteBuf buf) {
        dataToSync = new Teleporter();
        dataToSync.fromNbt(Objects.requireNonNull(buf.readNbt()));
        hand = buf.readBoolean() ? InteractionHand.MAIN_HAND: InteractionHand.OFF_HAND;
    }

    public void handle(Supplier<NetworkEvent.Context> contextGetter) {
        final NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            assert player != null;
            player.getItemInHand(hand).getCapability(TeleporterProvider.TELEPORTER).ifPresent((teleporter -> {
                teleporter.copyOf(dataToSync);
            }));
        });
        context.setPacketHandled(true);
    }
}