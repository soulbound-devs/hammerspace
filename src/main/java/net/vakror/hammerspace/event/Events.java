package net.vakror.hammerspace.event;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.FillCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Clearable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vakror.hammerspace.HammerspaceMod;
import net.vakror.hammerspace.block.ModBlocks;
import net.vakror.hammerspace.capability.Hammerspace;
import net.vakror.hammerspace.capability.HammerspaceProvider;
import net.vakror.hammerspace.capability.Teleporter;
import net.vakror.hammerspace.capability.TeleporterProvider;
import net.vakror.hammerspace.crafting.SmithingTeleporterUpgradeRecipe;
import net.vakror.hammerspace.crafting.TeleporterUpgradeRecipe;
import net.vakror.hammerspace.dimension.DimensionUtils;
import net.vakror.hammerspace.dimension.Dimensions;
import net.vakror.hammerspace.item.custom.TeleporterItem;

import java.util.*;

public class Events {
    @Mod.EventBusSubscriber(modid = HammerspaceMod.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void addTeleporterCapability(AttachCapabilitiesEvent<ItemStack> event) {
            if (event.getObject().getItem() instanceof TeleporterItem) {
                event.addCapability(new ResourceLocation(HammerspaceMod.MOD_ID, "teleporter"), new TeleporterProvider());
            }
        }
        @SubscribeEvent
        public static void addHammerspaceCapability(AttachCapabilitiesEvent<Level> event) {
            if (event.getObject().dimensionTypeId().equals(Dimensions.HAMMERSPACE_TYPE)) {
                event.addCapability(new ResourceLocation(HammerspaceMod.MOD_ID, "hammerspace"), new HammerspaceProvider());
            }
        }

        @SubscribeEvent
        public static void onEnterHammerspace(EntityJoinLevelEvent event) {
            if (event.getEntity() instanceof Player && !event.getLevel().isClientSide && event.getLevel().dimensionTypeId().equals(Dimensions.HAMMERSPACE_TYPE)) {
                ServerPlayer player = (ServerPlayer) event.getEntity();
                InteractionHand hand = player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof TeleporterItem ? InteractionHand.MAIN_HAND: InteractionHand.OFF_HAND;
                ServerLevel level = (ServerLevel) event.getLevel();
                player.getItemInHand(hand).getCapability(TeleporterProvider.TELEPORTER).ifPresent((teleporter -> {
                    genHammerspace(level, teleporter.width(), teleporter.height(), teleporter.length());
                }));
                if (event.getEntity() instanceof LivingEntity living) {
                    DimensionUtils.setGravity(living, (ServerLevel) event.getLevel());
                }
            }
        }

        @SubscribeEvent
        public static void onLeaveHammerspace(EntityLeaveLevelEvent event) {
            if (event.getEntity() instanceof Player && !event.getLevel().isClientSide) {
                DimensionUtils.removeGravity((LivingEntity) event.getEntity());
            }
        }

        private static void genHammerspace(ServerLevel serverlevel, int width, int height, int length) {
            for (int i = 4; i >= 0; i--) {
                List<BlockPos> list = Lists.newArrayList();
                int minX = -1 - width - i - 1;
                int minY = 63 - i;
                int minZ = -1 - length - i - 1;
                int maxX = -1 + i;
                int maxY = 63 + height + i + 1;
                int maxZ = -1 + i;

                for (BlockPos blockpos : BlockPos.betweenClosed(minX, minY, minZ, maxX, maxY, maxZ)) {
                    BlockInput blockinput = FillCommand.Mode.HOLLOW.filter.filter(new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ), blockpos, new BlockInput(ModBlocks.BORDER.get().defaultBlockState(), new HashSet<>(), new CompoundTag()), serverlevel);
                    if (blockinput != null) {
                        if (serverlevel.getBlockState(blockpos).is(Blocks.AIR) || serverlevel.getBlockState(blockpos).is(ModBlocks.BORDER.get())) {
                            BlockEntity blockentity = serverlevel.getBlockEntity(blockpos);
                            Clearable.tryClear(blockentity);
                            if (blockinput.place(serverlevel, blockpos, 2)) {
                                list.add(blockpos.immutable());
                            }
                        }
                    }
                }

                for (BlockPos blockpos1 : list) {
                    Block block = serverlevel.getBlockState(blockpos1).getBlock();
                    serverlevel.blockUpdated(blockpos1, block);
                }
            }
        }

        @SubscribeEvent
        public static void onResourceReload(AddReloadListenerEvent event) {
            TeleporterUpgradeRecipe.REGISTERED_RECIPES.clear();
            SmithingTeleporterUpgradeRecipe.REGISTERED_RECIPES.clear();
        }


        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(Teleporter.class);
            event.register(Hammerspace.class);
        }
    }
}
