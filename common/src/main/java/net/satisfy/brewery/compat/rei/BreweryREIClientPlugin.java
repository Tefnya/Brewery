package net.satisfy.brewery.compat.rei;

import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.satisfy.brewery.block.SiloBlock;
import net.satisfy.brewery.compat.rei.category.BrewingStationCategory;
import net.satisfy.brewery.compat.rei.category.SiloCategory;
import net.satisfy.brewery.compat.rei.display.BrewingStationDisplay;
import net.satisfy.brewery.compat.rei.display.SiloDisplay;
import net.satisfy.brewery.recipe.BrewingRecipe;
import net.satisfy.brewery.registry.ObjectRegistry;

import java.util.Collections;
import java.util.Map;

public class BreweryREIClientPlugin {

    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new BrewingStationCategory());
        registry.add(new SiloCategory());

        registry.addWorkstations(BrewingStationCategory.BREWING_STATION_DISPLAY, EntryStacks.of(ObjectRegistry.WOODEN_BREWINGSTATION.get()));
        registry.addWorkstations(BrewingStationCategory.BREWING_STATION_DISPLAY, EntryStacks.of(ObjectRegistry.COPPER_BREWINGSTATION.get()));
        registry.addWorkstations(BrewingStationCategory.BREWING_STATION_DISPLAY, EntryStacks.of(ObjectRegistry.NETHERITE_BREWINGSTATION.get()));

        registry.addWorkstations(SiloCategory.SILO_DISPLAY, EntryStacks.of(ObjectRegistry.SILO_WOOD.get()));
        registry.addWorkstations(SiloCategory.SILO_DISPLAY, EntryStacks.of(ObjectRegistry.SILO_COPPER.get()));
    }


    public static void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(BrewingRecipe.class, BrewingStationDisplay::new);

        for (Map.Entry<Item, Item> recipe : SiloBlock.getDryers().entrySet()) {
            registry.add(new SiloDisplay(EntryIngredients.ofIngredients(Collections.singletonList(Ingredient.of(recipe.getKey()))), EntryIngredients.ofIngredients(Collections.singletonList(Ingredient.of(recipe.getValue())))));
        }
    }
}