package net.vakror.hammerspace.packet;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.vakror.hammerspace.HammerspaceMod;

public class ModPackets {
    public static SimpleChannel INSTANCE;

    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(HammerspaceMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(PacketSyncDimensionListChanges.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncDimensionListChanges::new)
                .encoder(PacketSyncDimensionListChanges::encode)
                .consumerMainThread(PacketSyncDimensionListChanges::handle)
                .add();

        net.messageBuilder(SyncTeleporterDataC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SyncTeleporterDataC2SPacket::new)
                .encoder(SyncTeleporterDataC2SPacket::encode)
                .consumerMainThread(SyncTeleporterDataC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG packet) {
        INSTANCE.sendToServer(packet);
    }

    public static <MSG> void sendToClient(MSG packet, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}