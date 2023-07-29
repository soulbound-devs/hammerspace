package net.vakror.hammerspace.mixin;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.vakror.hammerspace.capability.HammerspaceProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerChunkCache.class)
public class ServerChunkCacheMixin {
    @Shadow @Final public ServerLevel level;

    @ModifyVariable(method = "tickChunks", at = @At("STORE"), name = "k")
    public int modifyRandomTickSpeed(int randomTickSpeed) {
        final int[] randomHammerspaceTickSpeed = new int[]{0};
        level.getCapability(HammerspaceProvider.HAMMERSPACE).ifPresent((hammerspace -> randomHammerspaceTickSpeed[0] = hammerspace.randomTick()));
        return randomHammerspaceTickSpeed[0] == 0? randomTickSpeed: randomHammerspaceTickSpeed[0];
    }
}
