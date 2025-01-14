package net.satisfy.brewery.fabric;

import net.fabricmc.api.ModInitializer;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.fabric.core.world.BreweryBiomeModification;

public class BreweryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Brewery.init();
        BreweryBiomeModification.init();
    }
}
