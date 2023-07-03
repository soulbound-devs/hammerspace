package net.vakror.hammerspace.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.hammerspace.HammerspaceMod;
import net.vakror.hammerspace.item.custom.TeleporterItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HammerspaceMod.MOD_ID);

    public static final RegistryObject<Item> WOOD_TELEPORTER = ITEMS.register("wood_teleporter", () -> new TeleporterItem(new Item.Properties().fireResistant().stacksTo(1), TeleporterTiers.WOOD));
    public static final RegistryObject<Item> IRON_TELEPORTER = ITEMS.register("iron_teleporter", () -> new TeleporterItem(new Item.Properties().fireResistant().stacksTo(1), TeleporterTiers.IRON));
    public static final RegistryObject<Item> GOLD_TELEPORTER = ITEMS.register("gold_teleporter", () -> new TeleporterItem(new Item.Properties().fireResistant().stacksTo(1), TeleporterTiers.GOLD));
    public static final RegistryObject<Item> EMERALD_TELEPORTER = ITEMS.register("emerald_teleporter", () -> new TeleporterItem(new Item.Properties().fireResistant().stacksTo(1), TeleporterTiers.EMERALD));
    public static final RegistryObject<Item> DIAMOND_TELEPORTER = ITEMS.register("diamond_teleporter", () -> new TeleporterItem(new Item.Properties().fireResistant().stacksTo(1), TeleporterTiers.DIAMOND));
    public static final RegistryObject<Item> NETHERITE_TELEPORTER = ITEMS.register("netherite_teleporter", () -> new TeleporterItem(new Item.Properties().fireResistant().stacksTo(1), TeleporterTiers.NETHERITE));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
