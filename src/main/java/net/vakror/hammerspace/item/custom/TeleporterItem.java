package net.vakror.hammerspace.item.custom;

import commoble.infiniverse.api.InfiniverseAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.vakror.hammerspace.HammerspaceMod;
import net.vakror.hammerspace.capability.HammerspaceProvider;
import net.vakror.hammerspace.capability.Teleporter;
import net.vakror.hammerspace.capability.TeleporterProvider;
import net.vakror.hammerspace.dimension.Dimensions;
import net.vakror.hammerspace.dimension.HammerspaceTeleporter;
import net.vakror.hammerspace.item.ITeleporterTier;
import net.vakror.hammerspace.screen.TeleporterScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class TeleporterItem extends Item {
    private final ITeleporterTier tier;
    public TeleporterItem(Properties properties, ITeleporterTier tier) {
        super(properties);
        this.tier = tier;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!player.isCrouching()) {
            if (!level.isClientSide) {
                player.getItemInHand(hand).getCapability(TeleporterProvider.TELEPORTER).ifPresent((teleporter -> {
                    if (!teleporter.dimensionId().equals("")) {
                        if (!level.dimension().location().equals(teleporter.getDimIdAsResourceLocation())) {
                            ServerLevel dimension = InfiniverseAPI.get().getOrCreateLevel(((ServerLevel) level).getServer(),
                                    ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(teleporter.dimensionId())),
                                    () -> new LevelStem(
                                            level.registryAccess().
                                                    registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY)
                                                    .getHolderOrThrow(Dimensions.HAMMERSPACE_TYPE),
                                            new FlatLevelSource(BuiltinRegistries.STRUCTURE_SETS, new FlatLevelGeneratorSettings(Optional.empty(), BuiltinRegistries.BIOME))));
                            dimension.getCapability(HammerspaceProvider.HAMMERSPACE).ifPresent((hammerspace -> {
                                hammerspace.setTick(teleporter.tickSpeed());
                                hammerspace.setRandomTick(teleporter.randomTickSpeed());
                                hammerspace.setGravity(teleporter.gravity());
                            }));
                            teleporter.setFromDimensionTypeId(level.dimensionTypeId().location().toString());
                            teleporter.setLastUsedLocation(new BlockPos((int) player.position().x, (int) player.position().y, (int) player.position().z));
                            player.changeDimension(dimension, new HammerspaceTeleporter(player.getItemInHand(hand), (ServerLevel) level, null));
                        } else {
                            AtomicReference<ServerLevel> toDimension = new AtomicReference<>(Objects.requireNonNull(level.getServer()).overworld());
                            level.getServer().getAllLevels().forEach((dim -> {
                                if (dim.dimensionTypeId().location().toString().equals(teleporter.fromDimensionTypeId())) {
                                    toDimension.set(dim);
                                }
                            }));
                            if (toDimension.get() != null) {
                                player.changeDimension(toDimension.get(), new HammerspaceTeleporter(player.getItemInHand(hand), (ServerLevel) level, teleporter.lastUsedLocation()));
                            }
                        }
                    }
                }));
            } else {
                player.getItemInHand(hand).getCapability(TeleporterProvider.TELEPORTER).ifPresent((teleporter -> {
                    if (teleporter.dimensionId().equals("")) {
                        Minecraft.getInstance().setScreen(new TeleporterScreen(player.getItemInHand(hand).getCapability(TeleporterProvider.TELEPORTER).orElse(new Teleporter()), hand, this.tier.getMaxWidth(), this.tier.getMaxHeight(), this.tier.getMaxLength(), true, false));
                    }
                }));
            }
        } else if (level.isClientSide){
            Minecraft.getInstance().setScreen(new TeleporterScreen(player.getItemInHand(hand).getCapability(TeleporterProvider.TELEPORTER).orElse(new Teleporter()), hand, tier.getMaxWidth(), tier.getMaxHeight(), tier.getMaxLength(), false, true));
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltip, tooltipFlag);
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_1", this.tier.getId()));
            tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_newline"));
            tooltip.add(Component.literal("Press §eSHIFT§r for more Information"));
        } else {
            stack.getCapability(TeleporterProvider.TELEPORTER).ifPresent((teleporter -> {
                tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_1", this.tier.getId()));
                tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_newline"));
                if (teleporter.dimensionId().equals("")) {
                    tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_2_not_set"));
                } else {
                    tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_2", teleporter.dimensionId()));
                }
                tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_newline"));


                tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_gravity", teleporter.gravity()));

                if (teleporter.width() == 0) {
                    tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_3_not_set"));
                } else {
                    tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_3", teleporter.width()));
                }

                if (teleporter.height() == 0) {
                    tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_4_not_set"));
                } else {
                    tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_4", teleporter.height()));
                }


                if (teleporter.length() == 0) {
                    tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_5_not_set"));
                } else {
                    tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_5", teleporter.length()));
                }


                tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_newline"));
                tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_7", tier.getMaxWidth()));
                tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_8", tier.getMaxHeight()));
                tooltip.add(Component.translatable("hammerspace.tooltip.teleporter_9", tier.getMaxLength()));
            }));
        }
    }
}
