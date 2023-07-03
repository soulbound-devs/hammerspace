package net.vakror.hammerspace.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.vakror.hammerspace.capability.Teleporter;
import net.vakror.hammerspace.capability.TeleporterProvider;
import net.vakror.hammerspace.item.custom.TeleporterItem;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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