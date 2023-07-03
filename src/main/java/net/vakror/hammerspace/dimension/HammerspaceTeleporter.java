package net.vakror.hammerspace.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.vakror.hammerspace.capability.TeleporterProvider;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class HammerspaceTeleporter implements ITeleporter {
    protected ItemStack stack;
    protected ServerLevel level;
    protected BlockPos pos;

    public HammerspaceTeleporter(ItemStack stack, ServerLevel level, BlockPos pos) {
        this.stack = stack;
        this.level = level;
        this.pos = pos;
    }

    @Override
    public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        AtomicReference<BlockPos> pos = new AtomicReference<>(BlockPos.ZERO);
        if (!level.dimensionTypeId().equals(Dimensions.HAMMERSPACE_TYPE)) {
            pos.set(new BlockPos(-5, 64, -5));
        } else {
            if (this.pos != null) {
                pos.set(this.pos);
            } else {
                stack.getCapability(TeleporterProvider.TELEPORTER).ifPresent((teleporter -> {
                    pos.set(teleporter.lastUsedLocation());
                }));
            }
        }
        return new PortalInfo(pos.get().getCenter(), Vec3.ZERO, entity.getYRot(), entity.getXRot());
    }

    public static boolean loaded(ServerLevel pLevel, ChunkPos pStart, ChunkPos pEnd) {
        return ChunkPos.rangeClosed(pStart, pEnd).allMatch((pos) -> pLevel.isLoaded(pos.getWorldPosition()));
    }

    @Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
        return false;
    }
}
