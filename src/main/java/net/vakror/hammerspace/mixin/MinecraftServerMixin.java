package net.vakror.hammerspace.mixin;

import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.vakror.hammerspace.capability.HammerspaceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Shadow private ProfilerFiller profiler;

    @Redirect(method = "tickChildren", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;tick(Ljava/util/function/BooleanSupplier;)V"))
    public void fixHammerspaceTicking(ServerLevel instance, BooleanSupplier booleanSupplier) {
        instance.getCapability(HammerspaceProvider.HAMMERSPACE).ifPresent((hammerspace -> {
            for (int i = 0; i < hammerspace.tick(); i++) {
                if (i != 0) {
                    profiler.push("tick");
                    net.minecraftforge.event.ForgeEventFactory.onPreLevelTick(instance, booleanSupplier);
                    tick(instance, booleanSupplier);
                    net.minecraftforge.event.ForgeEventFactory.onPostLevelTick(instance, booleanSupplier);
                } else {
                    tick(instance, booleanSupplier);
                }
            }
        }));
    }

    public void tick(ServerLevel level, BooleanSupplier supplier) {
        level.tick(supplier);
    }
}
