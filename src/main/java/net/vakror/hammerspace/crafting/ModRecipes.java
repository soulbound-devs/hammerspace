package net.vakror.hammerspace.crafting;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.hammerspace.HammerspaceMod;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, HammerspaceMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> TELEPORTER_UPGRADE_RECIPE_SERIALIZER = SERIALIZERS.register("teleporter_upgrade", () -> TeleporterUpgradeRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<?>> SMITHING_UPGRADE_SERIALIZER = SERIALIZERS.register("smithing_upgrade", SmithingTeleporterUpgradeRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
