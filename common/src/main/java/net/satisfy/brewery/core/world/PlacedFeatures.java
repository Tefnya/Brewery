package net.satisfy.brewery.core.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.satisfy.brewery.core.util.BreweryIdentifier;

public class PlacedFeatures {
    public static final ResourceKey<PlacedFeature> WILD_HOPS_KEY = registerKey("wild_hops");


    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new BreweryIdentifier(name));
    }
}
