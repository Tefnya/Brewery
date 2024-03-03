package net.satisfy.brewery.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.satisfy.brewery.util.BreweryIdentifier;

import java.util.List;
import java.util.Set;

public class StorageTypeRegistry {
    public static final ResourceLocation WINE_BOTTLE = new BreweryIdentifier("wine_bottle");

    public static void registerBlocks(Set<Block> blocks) {
        blocks.addAll(List.of(
                ObjectRegistry.BEER_BARLEY.get(), ObjectRegistry.BEER_HALEY.get(), ObjectRegistry.BEER_HOPS.get(),
                ObjectRegistry.WHISKEY_CARRASCONLABEL.get(), ObjectRegistry.WHISKEY_CRISTELWALKER.get(),
                ObjectRegistry.WHISKEY_JOJANNIK.get(), ObjectRegistry.WHISKEY_MAGGOALLAN.get(),
                ObjectRegistry.WHISKEY_LILITUSINGLEMALT.get(), ObjectRegistry.BEER_WHEAT.get()
        ));
    }
}
