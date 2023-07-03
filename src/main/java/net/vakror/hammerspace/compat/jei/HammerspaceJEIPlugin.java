package net.vakror.hammerspace.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.vakror.hammerspace.HammerspaceMod;
import net.vakror.hammerspace.crafting.TeleporterUpgradeRecipe;

@JeiPlugin
public class HammerspaceJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(HammerspaceMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IModPlugin.super.registerRecipes(registration);
        registration.addRecipes(RecipeTypes.CRAFTING, JEIRecipeHelper.getAndTransformAvailableRecipes(TeleporterUpgradeRecipe.REGISTERED_RECIPES, ShapedRecipe.class, JEIRecipeHelper::copyShapedRecipe));
    }
}
