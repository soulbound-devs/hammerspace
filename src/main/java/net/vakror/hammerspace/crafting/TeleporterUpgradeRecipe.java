package net.vakror.hammerspace.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.vakror.hammerspace.capability.Teleporter;
import net.vakror.hammerspace.capability.TeleporterProvider;
import net.vakror.hammerspace.item.custom.TeleporterItem;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class TeleporterUpgradeRecipe extends ShapedRecipe implements IWrapperRecipe<ShapedRecipe> {
	public static final Serializer SERIALIZER = new Serializer();
	public static final Set<ResourceLocation> REGISTERED_RECIPES = new LinkedHashSet<>();
	private final ShapedRecipe compose;

	public TeleporterUpgradeRecipe(ShapedRecipe compose) {
		super(compose.getId(), compose.getGroup(), compose.getRecipeWidth(), compose.getRecipeHeight(), compose.getIngredients(), compose.result);
		this.compose = compose;
		REGISTERED_RECIPES.add(compose.getId());
	}

	@Override
	public ShapedRecipe getCompose() {
		return compose;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public ItemStack assemble(CraftingContainer inv) {
		ItemStack upgradedTeleporter = super.assemble(inv);
		upgradedTeleporter.getCapability(TeleporterProvider.TELEPORTER).ifPresent((teleporter -> teleporter.copyOf(getTeleporter(inv).get().getCapability(TeleporterProvider.TELEPORTER).orElse(new Teleporter()))));
		return upgradedTeleporter;
	}

	private Optional<ItemStack> getTeleporter(CraftingContainer inv) {
		for (int slot = 0; slot < inv.getContainerSize(); slot++) {
			ItemStack slotStack = inv.getItem(slot);
			if (slotStack.getItem() instanceof TeleporterItem) {
				return Optional.of(slotStack);
			}
		}

		return Optional.empty();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends RecipeWrapperSerializer<ShapedRecipe, TeleporterUpgradeRecipe> {
		public Serializer() {
			super(TeleporterUpgradeRecipe::new, RecipeSerializer.SHAPED_RECIPE);
		}
	}
}