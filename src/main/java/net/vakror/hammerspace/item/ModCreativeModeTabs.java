package net.vakror.hammerspace.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.hammerspace.HammerspaceMod;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HammerspaceMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> HAMMERSPACE_TAB = CREATIVE_MODE_TABS.register("hammerspace_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.WOOD_TELEPORTER.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.WOOD_TELEPORTER.get());
                output.accept(ModItems.IRON_TELEPORTER.get());
                output.accept(ModItems.GOLD_TELEPORTER.get());
                output.accept(ModItems.EMERALD_TELEPORTER.get());
                output.accept(ModItems.DIAMOND_TELEPORTER.get());
                output.accept(ModItems.NETHERITE_TELEPORTER.get());
            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
