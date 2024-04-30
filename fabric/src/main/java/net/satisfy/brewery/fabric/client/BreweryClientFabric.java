package net.satisfy.brewery.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.satisfy.brewery.client.BreweryClient;

public class BreweryClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BreweryClient.preInitClient();
        BreweryClient.onInitializeClient();
    }
}
