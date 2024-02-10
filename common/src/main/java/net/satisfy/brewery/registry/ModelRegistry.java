package net.satisfy.brewery.registry;

import net.satisfy.brewery.util.BreweryIdentifier;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ModelRegistry {
    public static final ModelLayerLocation ROPE_KNOT = new ModelLayerLocation(new BreweryIdentifier("rope_knot"), "main");

    public static final ModelLayerLocation BEER_ELEMENTAL = new ModelLayerLocation(new BreweryIdentifier("beer_elemental"), "main");

    public static final ModelLayerLocation BEER_ELEMENTAL_ATTACK = new ModelLayerLocation(new BreweryIdentifier("beer_elemental_attack"), "main");

    public static final ModelLayerLocation HANGING_ROPE = new ModelLayerLocation(new BreweryIdentifier("hanging_rope"), "main");

    public static final ModelLayerLocation ROPE_COLLISION = new ModelLayerLocation(new BreweryIdentifier("rope_collision"), "main");
}