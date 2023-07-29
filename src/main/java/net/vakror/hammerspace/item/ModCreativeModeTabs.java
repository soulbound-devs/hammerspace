package net.vakror.hammerspace.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModCreativeModeTabs {

    public static final CreativeModeTab HAMMERSPACE_TAB = new CreativeModeTab("hammerspace") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return ModItems.WOOD_TELEPORTER.get().getDefaultInstance();
        }
    };
}
