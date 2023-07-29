package net.vakror.hammerspace.dimension;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.vakror.hammerspace.HammerspaceMod;


public class Dimensions {
    public static final ResourceKey<Level> HAMMERSPACE_DIM_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(HammerspaceMod.MOD_ID, "hammerspace"));
    public static final ResourceKey<DimensionType> HAMMERSPACE_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, HAMMERSPACE_DIM_KEY.location());

    public static void register() {
        HammerspaceMod.LOGGER.info("Dimensions are being registered for hammerspace");
    }

}
