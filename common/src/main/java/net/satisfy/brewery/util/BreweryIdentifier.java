package net.satisfy.brewery.util;

import net.minecraft.resources.ResourceLocation;
import net.satisfy.brewery.Brewery;

public class BreweryIdentifier extends ResourceLocation {
    public BreweryIdentifier(String id) {
        super(Brewery.MOD_ID, id);
    }
}
