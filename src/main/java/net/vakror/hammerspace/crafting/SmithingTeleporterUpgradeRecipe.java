package net.vakror.hammerspace.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.vakror.hammerspace.capability.Teleporter;
import net.vakror.hammerspace.capability.TeleporterProvider;
import net.vakror.hammerspace.item.ModItems;
import net.vakror.hammerspace.item.custom.TeleporterItem;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class SmithingTeleporterUpgradeRecipe extends UpgradeRecipe implements IWrapperRecipe<UpgradeRecipe> {
	public static final Set<ResourceLocation> REGISTERED_RECIPES = new LinkedHashSet<>();
	private final UpgradeRecipe compose;

	public SmithingTeleporterUpgradeRecipe(UpgradeRecipe compose) {
		super(compose.getId(), Objects.requireNonNull(ObfuscationReflectionHelper.getPrivateValue(UpgradeRecipe.class, compose, "f_44518_")),
				Objects.requireNonNull(ObfuscationReflectionHelper.getPrivateValue(UpgradeRecipe.class, compose, "f_44519_")), compose.getResultItem());
		this.compose = compose;
		REGISTERED_RECIPES.add(compose.getId());
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public ItemStack assemble(Container inv) {
		ItemStack upgradedTeleporter = getCraftingResult().copy();
		upgradedTeleporter.getCapability(TeleporterProvider.TELEPORTER).ifPresent((teleporter -> teleporter.copyOf(getTeleporter(inv).get().getCapability(TeleporterProvider.TELEPORTER).orElse(new Teleporter()))));
		return upgradedTeleporter;
	}

	private ItemStack getCraftingResult() {
		return Objects.requireNonNull(ObfuscationReflectionHelper.getPrivateValue(UpgradeRecipe.class, this, "f_44520_"));
	}

	private Optional<ItemStack> getTeleporter(Container inv) {
		ItemStack slotStack = inv.getItem(0);
		if (slotStack.getItem() instanceof TeleporterItem) {
			return Optional.of(slotStack);
		}
		return Optional.empty();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.SMITHING_UPGRADE_SERIALIZER.get();
	}

	@Override
	public UpgradeRecipe getCompose() {
		return compose;
	}

	public static class Serializer extends RecipeWrapperSerializer<UpgradeRecipe, SmithingTeleporterUpgradeRecipe> {
		public Serializer() {
			super(SmithingTeleporterUpgradeRecipe::new, RecipeSerializer.SMITHING);
		}
	}
}