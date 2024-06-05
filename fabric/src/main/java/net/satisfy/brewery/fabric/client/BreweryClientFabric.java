package net.satisfy.brewery.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.satisfy.brewery.client.BreweryClient;
import net.satisfy.brewery.registry.ObjectRegistry;

public class BreweryClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BreweryClient.preInitClient();
        BreweryClient.onInitializeClient();

        ArmorRenderer.register(new BrewfestHatRenderer(), ObjectRegistry.BREWFEST_HAT.get(), ObjectRegistry.BREWFEST_HAT_RED.get());
    }
}
