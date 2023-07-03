package net.vakror.hammerspace.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.vakror.hammerspace.capability.Teleporter;
import net.vakror.hammerspace.capability.TeleporterProvider;
import net.vakror.hammerspace.item.ModItems;
import net.vakror.hammerspace.item.custom.TeleporterItem;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class SmithingTeleporterUpgradeRecipe extends SmithingTransformRecipe implements IWrapperRecipe<SmithingTransformRecipe> {
	public static final Set<ResourceLocation> REGISTERED_RECIPES = new LinkedHashSet<>();
	private final SmithingTransformRecipe compose;

	public SmithingTeleporterUpgradeRecipe(SmithingTransformRecipe compose) {
		super(compose.getId(), compose.template, compose.base, compose.addition, compose.result);
		this.compose = compose;
		REGISTERED_RECIPES.add(compose.getId());
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public ItemStack assemble(Container inv, RegistryAccess registryAccess) {
		ItemStack upgradedTeleporter = result.copy();
		upgradedTeleporter.getCapability(TeleporterProvider.TELEPORTER).ifPresent((teleporter -> teleporter.copyOf(getTeleporter(inv).get().getCapability(TeleporterProvider.TELEPORTER).orElse(new Teleporter()))));
		return upgradedTeleporter;
	}

	private Optional<ItemStack> getTeleporter(Container inv) {
		ItemStack slotStack = inv.getItem(1);
		if (slotStack.getItem() instanceof TeleporterItem) {
			return Optional.of(slotStack);
		}
		return Optional.empty();
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return ModRecipes.SMITHING_UPGRADE_SERIALIZER.get();
	}

	@Override
	public SmithingTransformRecipe getCompose() {
		return compose;
	}

	public static class Serializer extends RecipeWrapperSerializer<SmithingTransformRecipe, SmithingTeleporterUpgradeRecipe> {
		public Serializer() {
			super(SmithingTeleporterUpgradeRecipe::new, RecipeSerializer.SMITHING_TRANSFORM);
		}
	}
}