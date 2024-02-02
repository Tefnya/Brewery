package net.satisfy.brewery.compat.jei.transfer;

import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.satisfy.brewery.compat.jei.category.BrewingStationCategory;
import net.satisfy.brewery.recipe.BrewingRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BrewingStationTransferInfo implements IRecipeTransferInfo<AbstractContainerMenu, BrewingRecipe> {
    @Override
    public Class<? extends AbstractContainerMenu> getContainerClass() {
        return AbstractContainerMenu.class;
    }

    @Override
    public Optional<MenuType<AbstractContainerMenu>> getMenuType() {
        return Optional.empty();
    }

    @Override
    public RecipeType<BrewingRecipe> getRecipeType() {
        return BrewingStationCategory.BREWINGSTATION;
    }

    @Override
    public boolean canHandle(AbstractContainerMenu container, BrewingRecipe recipe) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(AbstractContainerMenu container, BrewingRecipe recipe) {
        List<Slot> slots = new ArrayList<>();
        slots.add(container.getSlot(0));
        for (int i = 1; i <= recipe.getIngredients().size() && i < 5; i++) {
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(AbstractContainerMenu container, BrewingRecipe recipe) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 6; i < 6 + 36; i++) {
            Slot slot = container.getSlot(i);
            slots.add(slot);
        }
        return slots;
    }
}
