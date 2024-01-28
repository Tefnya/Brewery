package net.satisfy.brewery.fabric.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import net.satisfy.brewery.compat.rei.BreweryREIClientPlugin;

public class BreweryREIClientPluginFabric implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        BreweryREIClientPlugin.registerCategories(registry);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        BreweryREIClientPlugin.registerDisplays(registry);
    }
}