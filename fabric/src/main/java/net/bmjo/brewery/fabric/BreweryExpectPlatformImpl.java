package net.bmjo.brewery.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class BreweryExpectPlatformImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}