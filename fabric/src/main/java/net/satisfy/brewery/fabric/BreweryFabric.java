package net.satisfy.brewery.fabric;

import net.fabricmc.api.ModInitializer;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.fabric.registry.BreweryFabricVillagers;
import net.satisfy.brewery.fabric.world.BreweryBiomeModification;

public class BreweryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Brewery.init();
        BreweryFabricVillagers.init();
        BreweryBiomeModification.init();
    }
}
